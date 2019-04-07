/**
 * 
 */
package com.terrier.finances.gestion.services.abstrait.api;



import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.client.ClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;

import com.terrier.finances.gestion.communs.abstrait.AbstractAPIObjectModel;
import com.terrier.finances.gestion.communs.api.security.ApiConfigEnum;
import com.terrier.finances.gestion.communs.api.security.JwtConfigEnum;
import com.terrier.finances.gestion.communs.utils.exceptions.DataNotFoundException;
import com.terrier.finances.gestion.communs.utils.exceptions.UserNotAuthorizedException;
import com.terrier.finances.gestion.services.FacadeServices;
import com.terrier.finances.gestion.services.abstrait.api.converters.APIObjectModelReader;
import com.terrier.finances.gestion.services.abstrait.api.converters.ListAPIObjectModelReader;
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

	protected final String serviceURI;

	public AbstractHTTPClient() {
		serviceURI = AppConfig.getStringEnvVar(AppConfigEnum.APP_CONFIG_URL_SERVICE);
	}



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
		int c = getCorrelationId(invoquer);
		// Entêtes
		invoquer.header("Content-type", MediaType.APPLICATION_JSON);
		if(getJwtToken() != null){
			invoquer.header(JwtConfigEnum.JWT_HEADER_AUTH, getJwtToken());
			LOGGER.trace("[API={}][JWT Token={}]", c, getJwtToken());
		}
		invoquer.header(ApiConfigEnum.HEADER_CORRELATION_ID, c);
		LOGGER.info("[API={}] Appel du service [{}]", c, wt.getUri());
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
	 * Appel POST vers les API Services
	 * @param path chemin
	 * @param params paramètres
	 * @param dataToSend body à envoyer
	 * @param responseClassType réponse type
	 * @return réponse
	 * @throws UserNotAuthorizedException  erreur d'authentification
	 * @throws DataNotFoundException  erreur lors de l'appel
	 */
	protected <Q extends AbstractAPIObjectModel> 
	Response callHTTPPost(String path, Q dataToSend) throws UserNotAuthorizedException, DataNotFoundException {
		if(path != null){
			Invocation.Builder invoquer = getInvocation(path);
			int c = getCorrelationId(invoquer);
			try{
				Response res = invoquer.post(getEntity(dataToSend));
				if(res.getStatus() > 400){
					LOGGER.error("[API={}][POST][{}]", c, res.getStatus());
				}
				else{
					LOGGER.debug("[API={}][POST][{}]", c, res.getStatus());
				}
				return res;
			}
			catch(WebApplicationException e){
				catchWebApplicationException(c, HttpMethod.POST, e);
			}
			catch(Exception e){
				LOGGER.error("[API={}][POST] Erreur lors de l'appel", c, e);
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
			Invocation.Builder invoquer = getInvocation(path, params);
			int c = getCorrelationId(invoquer);
			try{
				R response = invoquer.post(getEntity(dataToSend), responseClassType);
				LOGGER.debug("[API={}][POST][200] Réponse : {}", c, response);
				return response;
			}
			catch(WebApplicationException e){
				catchWebApplicationException(c, HttpMethod.POST, e);
			}
			catch(Exception e){
				LOGGER.error("[API={}][POST] Erreur lors de l'appel", c, e);
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
			Invocation.Builder invoquer = getInvocation(path, params);
			int c = getCorrelationId(invoquer);
			try{
				Response response = invoquer.get();
				if(response != null){
					LOGGER.debug("[API={}][GET] Réponse : [{}]", c, response.getStatus());
					resultat = response.getStatus() == 200;
				}
			}
			catch(WebApplicationException e){
				catchWebApplicationException(c, HttpMethod.GET, e);
			}
			catch(Exception e){
				LOGGER.error("[API={}][GET] Erreur lors de l'appel", c, e);
				resultat = false;
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
			Builder invoquer = getInvocation(path, params);
			int c = getCorrelationId(invoquer);
			try{
				R response = invoquer.get(responseClassType);
				LOGGER.debug("[API={}][GET][200] Réponse : [{}]", c, response);
				return response;
			}
			catch(WebApplicationException e){
				catchWebApplicationException(c, HttpMethod.GET, e);
			}
			catch(Exception e){
				LOGGER.error("[API={}][GET] Erreur lors de l'appel", c, e);
			}
		}
		return null;
	}




	/**
	 * Appel HTTP GET
	 * @param clientHTTP client HTTP
	 * @param url racine de l'URL
	 * @param urlParams paramètres de l'URL (à part pour ne pas les tracer)
	 * @return résultat de l'appel
	 */
	protected <R extends AbstractAPIObjectModel> R callHTTPDeleteData(String path, Class<R> responseClassType){
		if(path != null){
			Builder invoquer = getInvocation(path);
			int c = getCorrelationId(invoquer);
			try{
				R response = invoquer.delete(responseClassType);
				LOGGER.debug("[API={}][DEL][200] Réponse : [{}]", c, response);
				return response;
			}
			catch(WebApplicationException e){
				LOGGER.error("[API={}][DEL][{}] Erreur lors de l'appel", c, e.getResponse().getStatus());
			}
			catch(Exception e){
				LOGGER.error("[API={}][DEL] Erreur lors de l'appel",c, e);
			}
		}
		return null;
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
	protected <R extends AbstractAPIObjectModel> List<R> callHTTPGetListData(String path) throws UserNotAuthorizedException, DataNotFoundException{
		if(path != null){
			Builder invoquer = getInvocation(path);
			int c = getCorrelationId(invoquer);
			try{
				@SuppressWarnings("unchecked")
				List<R> response = invoquer.get(List.class);
				LOGGER.debug("[API={}][GET][200] Réponse : [{}]", c, response);
				return response;
			}
			catch(WebApplicationException e){
				catchWebApplicationException(c, HttpMethod.GET, e);
			}
			catch(Exception e){
				LOGGER.error("[API={}][GET] Erreur lors de l'appel", c, e);
			}
		}
		return new ArrayList<>();
	}

	/**
	 * Création d'une Entity
	 * @param apiObject object API
	 * @return entity englobant l'API Object
	 */
	protected <R extends AbstractAPIObjectModel> Entity<R> getEntity(R apiObject){
		return Entity.entity(apiObject, MediaType.APPLICATION_JSON_TYPE);
	}


	/**
	 * Catch 401 error
	 * @param c code API
	 * @param e Exception
	 * @throws UserNotAuthorizedException utilisateur non authentifié
	 * @throws DataNotFoundException  erreur lors de l'appel
	 */
	private void catchWebApplicationException(int c, HttpMethod verbe,  WebApplicationException e) throws UserNotAuthorizedException, DataNotFoundException {
		LOGGER.error("[API={}][{}] Erreur [{}] lors de l'appel ", c, verbe, e.getResponse().getStatus());
		if(e.getResponse().getStatusInfo().equals(Status.UNAUTHORIZED)) {
			throw new UserNotAuthorizedException("Utilisateur non authentifié");
		}
		else if(Status.INTERNAL_SERVER_ERROR.equals(e.getResponse().getStatusInfo()) || Status.BAD_REQUEST.equals(e.getResponse().getStatusInfo())) {
			throw new DataNotFoundException("Erreur lors de l'appel au service");
		}
	}

	/**
	 * @param invoquer
	 * @return correlation ID
	 */
	private int getCorrelationId(Invocation.Builder invoquer){
		return Math.abs(invoquer.toString().hashCode());
	}


}
