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
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.terrier.finances.gestion.communs.abstrait.AbstractAPIObjectModel;
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

	protected final String URI = "http://localhost:8090/services";

	private Client clientHTTP;

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
		LOGGER.info("[API] Appel du service [{}]", wt.getUri());
		
		return wt.request(JSON_MEDIA_TYPE).header("Content-type", MediaType.APPLICATION_JSON);
	}

	/**
	 * @param path 
	 * @return invocation
	 */
	private Invocation.Builder getInvocation(String path){
		return getInvocation(path, null);
	}


	/**
	 * Appel POST 
	 * @param invocation URL appelée
	 * @param dataToSend données envoyé
	 * @param responseClassType classe de la réponse
	 * @return réponse
	 */
	protected <Q extends AbstractAPIObjectModel, R extends AbstractAPIObjectModel> R callHTTPPost(String path, Q dataToSend, Class<R> responseClassType){
		if(path != null){
			try{
				Invocation.Builder invoquer = getInvocation(path);
				R response = null;
				if(responseClassType != null){
					response = invoquer.post(getEntity(dataToSend), responseClassType);
					LOGGER.debug("[API POST][200] Réponse : {}", response);
				}
				else{
					Response res = invoquer.post(getEntity(dataToSend));
					if(res.getStatus() > 400){
						LOGGER.error("[API POST][{}]", res.getStatus());
					}
					else{
						LOGGER.debug("[API POST][{}]", res.getStatus());
					}
				}

				if(response != null){
					return response;
				}
			}
			catch(WebApplicationException e){
				LOGGER.error("[API POST][{}] Erreur lors de l'appel", e.getResponse().getStatus());
			}
			catch(Exception e){
				LOGGER.error("[API POST] Erreur lors de l'appel", e);
			}
		}
		return null;
	}


	/**
	 * Appel HTTP GET
	 * @param clientHTTP client HTTP
	 * @param url racine de l'URL
	 * @param path paramètres de l'URL
	 * @return résultat de l'appel
	 */
	protected boolean callHTTPGet(String path){
		boolean resultat = false;
		if(path != null){
			try{
				Response response = getInvocation(path).get();
				if(response != null){
					LOGGER.debug("[API GET] Réponse : [{}]", response.getStatus());
					resultat = response.getStatus() == 200;
				}
			}
			catch(Exception e){
				LOGGER.error("[API GET] Erreur lors de l'appel", e);
				resultat = false;
			}
		}
		return resultat;
	}


	
	protected <R extends AbstractAPIObjectModel> R callHTTPGetData(String path, Class<R> responseClassType){
		return callHTTPGetData(path, null, responseClassType);
	}
	/**
	 * Appel HTTP GET
	 * @param clientHTTP client HTTP
	 * @param url racine de l'URL
	 * @param urlParams paramètres de l'URL (à part pour ne pas les tracer)
	 * @return résultat de l'appel
	 */
	protected <R extends AbstractAPIObjectModel> R callHTTPGetData(String path, Map<String, String> params, Class<R> responseClassType){
		if(path != null){
			try{

				R response = getInvocation(path, params).get(responseClassType);
				LOGGER.debug("[API][GET][200] Réponse : [{}]", response);
				return response;
			}
			catch(WebApplicationException e){
				LOGGER.error("[API][GET][{}] Erreur lors de l'appel", e.getResponse().getStatus());
			}
			catch(Exception e){
				LOGGER.error("[API][GET] Erreur lors de l'appel", e);
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
			try{
				R response = getInvocation(path).delete(responseClassType);
				LOGGER.debug("[API][DEL][200] Réponse : [{}]", response);
				return response;
			}
			catch(WebApplicationException e){
				LOGGER.error("[API][DEL][{}] Erreur lors de l'appel", e.getResponse().getStatus());
			}
			catch(Exception e){
				LOGGER.error("[API][DEL] Erreur lors de l'appel", e);
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
			try{

				@SuppressWarnings("unchecked")
				List<R> response = getInvocation(path).get(List.class);
				LOGGER.debug("[API][GET][200] Réponse : [{}]", response);
				return response;
			}
			catch(WebApplicationException e){
				LOGGER.error("[API][GET][{}] Erreur lors de l'appel", e.getResponse().getStatus());
			}
			catch(Exception e){
				LOGGER.error("[API][GET] Erreur lors de l'appel", e);
			}
		}
		return null;
	}


	protected <R extends AbstractAPIObjectModel> Entity<R> getEntity(R apiObject){
		return Entity.entity(apiObject, MediaType.APPLICATION_JSON_TYPE);
	}
}
