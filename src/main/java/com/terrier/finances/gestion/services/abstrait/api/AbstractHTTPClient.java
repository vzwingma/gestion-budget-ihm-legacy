/**
 * 
 */
package com.terrier.finances.gestion.services.abstrait.api;



import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.terrier.finances.gestion.communs.abstrait.AbstractAPIObjectModel;
import com.terrier.finances.gestion.communs.api.security.JwtConfig;
import com.terrier.finances.gestion.communs.utils.exceptions.UserNotAuthorizedException;
import com.terrier.finances.gestion.services.FacadeServices;
import com.terrier.finances.gestion.services.ServicesConfigEnum;
import com.terrier.finances.gestion.services.abstrait.api.converters.APIObjectModelReader;
import com.terrier.finances.gestion.services.abstrait.api.converters.ListAPIObjectModelReader;

/**
 * Classe d'un client HTTP
 * @author vzwingma
 *
 */
public abstract class AbstractHTTPClient {


	private static final Logger LOGGER = LoggerFactory.getLogger( AbstractHTTPClient.class );
	// Tout est en JSON
	private static final MediaType JSON_MEDIA_TYPE = MediaType.APPLICATION_JSON_TYPE;

	protected final String URI;

	private Client clientHTTP;

	
	public AbstractHTTPClient() {
		URI = getStringEnvVar(ServicesConfigEnum.SERVICE_CONFIG_URL, "http://localhost:8090/services");
	}
	
	/**
	 * @return client HTTP
	 */
	private Client getClient(){
		if(clientHTTP == null){
			this.clientHTTP = getClient(null);
		}
		return this.clientHTTP;
	}

	/**
	 * Créé un client HTTP 
	 * (dans une méthode séparée pour pouvoir être mocké facilement)
	 * @return client HTTP
	 * @throws NoSuchAlgorithmException 
	 */
	private Client getClient(HttpAuthenticationFeature feature) {

		ClientConfig clientConfig = new ClientConfig();
		if(feature != null){
			clientConfig.register(feature);

		}
		// Register des converters
		clientConfig.register(new ListAPIObjectModelReader<AbstractAPIObjectModel>());
		clientConfig.register(new APIObjectModelReader<AbstractAPIObjectModel>());
		try {
			//			// Install the all-trusting trust manager
			//			SSLContext sslcontext = SSLContext.getInstance("TLS");
			//
			//			sslcontext.init(null,  new TrustManager[] { new ClientHTTPTrustManager() }, new java.security.SecureRandom());
			//			HttpsURLConnection.setDefaultSSLSocketFactory(sslcontext.getSocketFactory());
			return ClientBuilder.newBuilder()
					//	.sslContext(sslcontext)
					.withConfig(clientConfig)
					.build();
		}
		catch(Exception e){
			LOGGER.error("Erreur envoi : Erreur lors de la création du Client HTTP {}", e.getMessage());
			return ClientBuilder.newClient(clientConfig);
		}
	}


	private WebTarget wt;


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
		wt = getClient().target(URI);
		if(path != null){
			wt = wt.path(path);
		}
		
		if(queryParams != null && !queryParams.isEmpty()){
			queryParams.entrySet().stream()
			.forEach(e -> {
				wt=wt.queryParam(e.getKey(), e.getValue());
			});
		}
		Invocation.Builder invoquer = wt.request(JSON_MEDIA_TYPE)
											.header("Content-type", MediaType.APPLICATION_JSON);
		int c = getCodeInvoquer(invoquer);
		if(getJwtToken() != null){
			invoquer.header(JwtConfig.JWT_AUTH_HEADER, getJwtToken());
			LOGGER.trace("[API={}][JWT Token={}]", c, getJwtToken());
		}
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
	 * @throws UserNotAuthorizedException  erreur d'auth
	 */
	protected <Q extends AbstractAPIObjectModel> 
	Response callHTTPPost(String path, Q dataToSend) throws UserNotAuthorizedException {
		if(path != null){
			Invocation.Builder invoquer = getInvocation(path);
			int c = invoquer.toString().hashCode();
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
				LOGGER.error("[API={}][POST][{}] Erreur lors de l'appel", c, e.getResponse().getStatus());
				if(e.getResponse().getStatusInfo().equals(Status.UNAUTHORIZED)) {
					throw new UserNotAuthorizedException("Utilisateur non authentifié");
				}
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
	 * @throws UserNotAuthorizedException 
	 */
	protected  <Q extends AbstractAPIObjectModel, R extends AbstractAPIObjectModel>
	R callHTTPPost(String path, Q dataToSend, Class<R> responseClassType) throws UserNotAuthorizedException{
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
	 */
	protected <Q extends AbstractAPIObjectModel, R extends AbstractAPIObjectModel> 
	R callHTTPPost(String path, Map<String, String> params, Q dataToSend, Class<R> responseClassType) throws UserNotAuthorizedException{
		if(path != null){
			Invocation.Builder invoquer = getInvocation(path, params);
			int c = getCodeInvoquer(invoquer);
			try{
				R response = invoquer.post(getEntity(dataToSend), responseClassType);
				LOGGER.debug("[API={}][POST][200] Réponse : {}", c, response);
				return response;
			}
			catch(WebApplicationException e){
				LOGGER.error("[API={}][POST][{}] Erreur lors de l'appel", c, e.getResponse().getStatus());
				if(e.getResponse().getStatusInfo().equals(Status.UNAUTHORIZED)) {
					throw new UserNotAuthorizedException("Utilisateur non authentifié");
				}
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
	 */
	protected boolean callHTTPGet(String path){
		return callHTTPGet(path, null);
	}


	private int getCodeInvoquer(Invocation.Builder invoquer){
		return Math.abs(invoquer.toString().hashCode());
	}

	/**
	 * Appel HTTP GET
	 * @param params params
	 * @param path paramètres de l'URL
	 * @return résultat de l'appel
	 */
	protected boolean callHTTPGet(String path, Map<String, String> params){
		boolean resultat = false;
		if(path != null){
			Invocation.Builder invoquer = getInvocation(path, params);
			int c = getCodeInvoquer(invoquer);
			try{
				Response response = invoquer.get();
				if(response != null){
					LOGGER.debug("[API={}][GET] Réponse : [{}]", c, response.getStatus());
					resultat = response.getStatus() == 200;
					if(response.getStatusInfo().equals(Status.UNAUTHORIZED)) {
						throw new UserNotAuthorizedException("Utilisateur non authentifié");
					}
				}
			}
			catch(Exception e){
				LOGGER.error("[API={}][GET] Erreur lors de l'appel", c, e);
				resultat = false;
			}
		}
		return resultat;
	}



	protected <R extends AbstractAPIObjectModel> R callHTTPGetData(String path, Class<R> responseClassType) throws UserNotAuthorizedException{
		return callHTTPGetData(path, null, responseClassType);
	}
	/**
	 * Appel HTTP GET
	 * @param clientHTTP client HTTP
	 * @param url racine de l'URL
	 * @param urlParams paramètres de l'URL (à part pour ne pas les tracer)
	 * @return résultat de l'appel
	 * @throws UserNotAuthorizedException 
	 */
	protected <R extends AbstractAPIObjectModel> R callHTTPGetData(String path, Map<String, String> params, Class<R> responseClassType) throws UserNotAuthorizedException{
		if(path != null){
			Builder invoquer = getInvocation(path, params);
			int c = getCodeInvoquer(invoquer);
			try{
				R response = invoquer.get(responseClassType);
				LOGGER.debug("[API={}][GET][200] Réponse : [{}]", c, response);
				return response;
			}
			catch(WebApplicationException e){
				LOGGER.error("[API={}][GET][{}] Erreur lors de l'appel", c, e.getResponse().getStatus());
				if(e.getResponse().getStatusInfo().equals(Status.UNAUTHORIZED)) {
					throw new UserNotAuthorizedException("Utilisateur non authentifié");
				}
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
			int c = getCodeInvoquer(invoquer);
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
	 */
	protected <R extends AbstractAPIObjectModel> List<R> callHTTPGetListData(String path, Class<R> responseClassType){
		if(path != null){
			Builder invoquer = getInvocation(path);
			int c = getCodeInvoquer(invoquer);
			try{
				@SuppressWarnings("unchecked")
				List<R> response = getInvocation(path).get(List.class);
				LOGGER.debug("[API={}][GET][200] Réponse : [{}]", c, response);
				return response;
			}
			catch(WebApplicationException e){
				LOGGER.error("[API={}][GET][{}] Erreur lors de l'appel", c, e.getResponse().getStatus());
			}
			catch(Exception e){
				LOGGER.error("[API={}][GET] Erreur lors de l'appel", c, e);
			}
		}
		return null;
	}


	protected <R extends AbstractAPIObjectModel> Entity<R> getEntity(R apiObject){
		return Entity.entity(apiObject, MediaType.APPLICATION_JSON_TYPE);
	}
	

	/**
	 * Retourne la valeur string de la variable d'environnement
	 * @param cle
	 * @return valeur de la clé
	 */
	private String getStringEnvVar(ServicesConfigEnum cle, String defaultVar){
		String envVar = System.getenv(cle.name());
		if(envVar != null) {
			return envVar;
		}
		else {
			LOGGER.warn("La clé {} n'est définie. Utilisation de la valeur par défaut : {} ", cle.name(), defaultVar);
			 return defaultVar;
		}
	}
}
