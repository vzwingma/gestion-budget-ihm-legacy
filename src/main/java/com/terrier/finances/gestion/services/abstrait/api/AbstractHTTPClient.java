/**
 * Client HTTP
 */
package com.terrier.finances.gestion.services.abstrait.api;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.client.ClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

import com.terrier.finances.gestion.communs.abstrait.AbstractAPIObjectModel;
import com.terrier.finances.gestion.communs.api.security.ApiConfigEnum;
import com.terrier.finances.gestion.communs.api.security.JwtConfigEnum;
import com.terrier.finances.gestion.communs.utils.data.BudgetApiUrlEnum;
import com.terrier.finances.gestion.communs.utils.exceptions.DataNotFoundException;
import com.terrier.finances.gestion.communs.utils.exceptions.UserNotAuthorizedException;
import com.terrier.finances.gestion.services.FacadeServices;
import com.terrier.finances.gestion.services.abstrait.api.converters.APIObjectModelReader;
import com.terrier.finances.gestion.services.abstrait.api.converters.ListAPIObjectModelReader;
import com.terrier.finances.gestion.services.abstrait.api.filters.LogApiFilter;
import com.terrier.finances.gestion.services.admin.model.Info;
import com.terrier.finances.gestion.ui.communs.config.AppConfig;
import com.terrier.finances.gestion.ui.communs.config.AppConfigEnum;

/**
 * Classe d'un client HTTP
 * @author vzwingma
 *
 */
public abstract class AbstractHTTPClient {


	protected static final Logger LOGGER = LoggerFactory.getLogger( AbstractHTTPClient.class );
	// Tout est en JSON
	private static final MediaType JSON_MEDIA_TYPE = MediaType.APPLICATION_JSON_TYPE;

	private static final String HEADER_CONTENT_TYPE = "Content-type";

	protected final String serviceURI;

	@Autowired
	private LogApiFilter logFilter;
	
	
	public AbstractHTTPClient() {
		serviceURI = AppConfig.getStringEnvVar(getConfigServiceURI());
	}

	public abstract AppConfigEnum getConfigServiceURI();

	/**
	 * Créé un client HTTP 
	 * (dans une méthode séparée pour pouvoir être mocké facilement)
	 * @return client HTTP
	 * @throws NoSuchAlgorithmException 
	 */
	private WebTarget getClient() {

		ClientConfig clientConfig = new ClientConfig();
		// Register des converters
		clientConfig.register(new ListAPIObjectModelReader<AbstractAPIObjectModel>());
		clientConfig.register(new APIObjectModelReader<AbstractAPIObjectModel>());
		// Filter
		clientConfig.register(logFilter);
		
		try {
			// Install the all-trusting trust manager
			SSLContext sslcontext = SSLContext.getInstance("TLS");
			sslcontext.init(null,  null, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sslcontext.getSocketFactory());
			return ClientBuilder.newBuilder()
					.sslContext(sslcontext)
					.withConfig(clientConfig)
					.build().target(serviceURI);
		}
		catch(Exception e){
			LOGGER.error("Erreur envoi : Erreur lors de la création du Client HTTP {}", e.getMessage());
			return ClientBuilder.newClient(clientConfig).target(serviceURI);
		}
	}

	/**
	 * 
	 * @return JWT Token de l'utilisateur
	 */
	private String getJwtToken(){
		return FacadeServices.get().getServiceUserSessions().getUserSession().getJwtToken();
	}
	/**
	 * Invvocation
	 * @param path chemin
	 * @param queryParams paramètres de requêtes (si existants)
	 * @return invocation prête
	 */
	private Invocation.Builder getInvocation(String path, Map<String, String> queryParams){
		WebTarget wt = getClient();
		if(path != null){
			wt = wt.path(path);
		}
		if(queryParams != null && !queryParams.isEmpty()){
			for (Entry<String, String> params : queryParams.entrySet()) {
				wt=wt.queryParam(params.getKey(), params.getValue());
			}
		}
		Invocation.Builder invoquer = wt.request(JSON_MEDIA_TYPE);
		// Entêtes
		invoquer.header(HEADER_CONTENT_TYPE, MediaType.APPLICATION_JSON);
		if(getJwtToken() != null){
			invoquer.header(JwtConfigEnum.JWT_HEADER_AUTH, getJwtToken());
			LOGGER.trace("[JWT Token={}]", getJwtToken());
		}
		
		// Correlation ID
		String corrID = UUID.randomUUID().toString();
		org.slf4j.MDC.put(ApiConfigEnum.HEADER_CORRELATION_ID, "["+ApiConfigEnum.LOG_CORRELATION_ID+"="+corrID+"]");
		invoquer.header(ApiConfigEnum.HEADER_CORRELATION_ID, corrID);
		return invoquer;
	}

	/**
	 * @param path 
	 * @return invocation
	 */
	private Invocation.Builder getInvocation(String path){
		return getInvocation(path, null);
	}

	/**
	 * Statut du Services 
	 * @throws DataNotFoundException  erreur lors de l'appel
	 */
	public Info getInfo() throws DataNotFoundException, UserNotAuthorizedException {
		return callHTTPGetData(BudgetApiUrlEnum.ACTUATORS_INFO_FULL, Info.class);
	}
	/**
	 * Appel POST vers les API Services
	 * @param path chemin
	 * @param params paramètres
	 * @param dataToSend body à envoyer
	 * @param responseClassType réponse type
	 * @return réponse
	 * @throws UserNotAuthorizedException  erreur d'authentification
	 * @throws DataNotFoundException  erreur lors de l'appel
	 */
	protected <Q extends AbstractAPIObjectModel> Response callHTTPPost(String path, Q dataToSend) throws UserNotAuthorizedException, DataNotFoundException {
		if(path != null){
			try{
				Response response = getInvocation(path).post(getEntity(dataToSend));
				LOGGER.debug("Réponse : {}", response);
				return response;
			}
			catch(Exception e){
				catchWebApplicationException(HttpMethod.POST, e);
			}
		}
		return null;
	}

	/**
	 * Appel POST vers les API Services
	 * @param path chemin
	 * @param dataToSend body à envoyer
	 * @param responseClassType réponse type
	 * @return réponse
	 * @throws UserNotAuthorizedException  erreur d'authentification
	 * @throws DataNotFoundException  erreur lors de l'appel
	 */
	protected  <Q extends AbstractAPIObjectModel, R extends AbstractAPIObjectModel>
	R callHTTPPost(String path, Q dataToSend, Class<R> responseClassType) throws UserNotAuthorizedException, DataNotFoundException{
		return callHTTPPost(path, null, dataToSend, responseClassType);
	}


	/**
	 * Appel POST vers les API Services
	 * @param path chemin
	 * @param params paramètres
	 * @param dataToSend body à envoyer
	 * @param responseClassType réponse type
	 * @return réponse
	 * @throws UserNotAuthorizedException 
	 * @throws DataNotFoundException 
	 */
	protected <Q extends AbstractAPIObjectModel, R extends AbstractAPIObjectModel> 
	R callHTTPPost(String path, Map<String, String> params, Q dataToSend, Class<R> responseClassType) throws UserNotAuthorizedException, DataNotFoundException{
		if(path != null){
			try{
				R response = getInvocation(path, params).post(getEntity(dataToSend), responseClassType);
				LOGGER.debug("Réponse : {}", response);
				return response;
			}
			catch(Exception e){
				catchWebApplicationException(HttpMethod.POST, e);
			}
		}
		return null;
	}
	/**
	 * Appel HTTP GET
	 * @param path paramètres de l'URL
	 * @return résultat de l'appel
	 * @throws UserNotAuthorizedException  erreur d'authentification
	 * @throws DataNotFoundException  erreur lors de l'appel
	 */
	protected boolean callHTTPGet(String path) throws UserNotAuthorizedException, DataNotFoundException{
		return callHTTPGet(path, null);
	}


	/**
	 * Appel HTTP GET
	 * @param params params
	 * @param path paramètres de l'URL
	 * @return résultat de l'appel
	 * @throws UserNotAuthorizedException  erreur d'authentification
	 * @throws DataNotFoundException  erreur lors de l'appel
	 */
	protected boolean callHTTPGet(String path, Map<String, String> params) throws UserNotAuthorizedException, DataNotFoundException{
		boolean resultat = false;
		if(path != null){
			try{
				Response response = getInvocation(path, params).get();
				if(response != null){
					LOGGER.debug("Réponse : [{}]", response.getStatus());
					resultat = response.getStatus() == 200;
				}
			}
			catch(Exception e){
				catchWebApplicationException(HttpMethod.GET, e);
			}
		}
		return resultat;
	}



	/**
	 * Appel HTTP GET
	 * @param path chemin
	 * @param responseClassType type de la réponse
	 * @return résultat
	 * @throws UserNotAuthorizedException  erreur d'authentification
	 * @throws DataNotFoundException  erreur lors de l'appel
	 */
	protected <R extends AbstractAPIObjectModel> R callHTTPGetData(String path, Class<R> responseClassType) throws UserNotAuthorizedException, DataNotFoundException{
		return callHTTPGetData(path, null, responseClassType);
	}
	/**
	 * Appel HTTP GET
	 * @param clientHTTP client HTTP
	 * @param url racine de l'URL
	 * @param urlParams paramètres de l'URL (à part pour ne pas les tracer)
	 * @return résultat de l'appel
	 * @throws UserNotAuthorizedException  erreur d'authentification
	 * @throws DataNotFoundException  erreur lors de l'appel
	 */
	protected <R extends AbstractAPIObjectModel> R callHTTPGetData(String path, Map<String, String> params, Class<R> responseClassType) throws UserNotAuthorizedException, DataNotFoundException{
		if(path != null){
			try{
				R response = getInvocation(path, params).get(responseClassType);
				LOGGER.debug("Réponse : [{}]", response);
				return response;
			}
			catch(Exception e){
				catchWebApplicationException(HttpMethod.GET, e);
			}
		}
		return null;
	}




	/**
	 * Appel DELETE
	 * @param path racine de l'URL
	 * @param responseClassType reponse
	 * @return résultat de l'appel
	 */
	protected <R extends AbstractAPIObjectModel> R callHTTPDeleteData(String path, Class<R> responseClassType) throws UserNotAuthorizedException, DataNotFoundException{
		if(path != null){
			try{
				R response = getInvocation(path).delete(responseClassType);
				LOGGER.debug("Réponse : [{}]", response);
				return response;
			}
			catch(Exception e){
				catchWebApplicationException(HttpMethod.DELETE, e);
			}
		}
		return null;
	}


	/**
	 * Appel HTTP GET List
	 * @param path racine de l'URL
	 * @return résultat de l'appel
	 * @throws UserNotAuthorizedException  erreur d'authentification
	 * @throws DataNotFoundException  erreur lors de l'appel
	 */
	protected <R extends AbstractAPIObjectModel> List<R> callHTTPGetListData(String path) throws UserNotAuthorizedException, DataNotFoundException{
		if(path != null){
			try{
				@SuppressWarnings("unchecked")
				List<R> response = getInvocation(path).get(List.class);
				LOGGER.debug("Réponse : [{}]", response);
				return response;
			}
			catch(Exception e){
				catchWebApplicationException(HttpMethod.GET, e);
			}
		}
		return new ArrayList<>();
	}

	/**
	 * Création d'une Entity
	 * @param apiObject object API
	 * @return entity englobant l'API Object
	 */
	private <R extends AbstractAPIObjectModel> Entity<R> getEntity(R apiObject){
		return Entity.entity(apiObject, MediaType.APPLICATION_JSON_TYPE);
	}


	/**
	 * Catch 401 error
	 * @param c code API
	 * @param e Exception
	 * @throws UserNotAuthorizedException utilisateur non authentifié
	 * @throws DataNotFoundException  erreur lors de l'appel
	 */
	private void catchWebApplicationException(HttpMethod verbe,  Exception ex) throws UserNotAuthorizedException, DataNotFoundException {
		if(ex instanceof WebApplicationException) {
			WebApplicationException e = (WebApplicationException)ex;
			LOGGER.error("[{}] Erreur [{}] lors de l'appel ", verbe, e.getResponse().getStatus());
			if(e.getResponse().getStatusInfo().equals(Status.UNAUTHORIZED)) {
				throw new UserNotAuthorizedException("Utilisateur non authentifié");
			}
			else if(Status.INTERNAL_SERVER_ERROR.equals(e.getResponse().getStatusInfo()) || Status.BAD_REQUEST.equals(e.getResponse().getStatusInfo())) {
				throw new DataNotFoundException("Erreur lors de l'appel au service");
			}
		}
		else {
			LOGGER.error("[{}] Erreur lors de l'appel", verbe, ex);
		}
	}
}
