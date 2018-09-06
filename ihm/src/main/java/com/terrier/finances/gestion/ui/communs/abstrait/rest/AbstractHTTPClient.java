/**
 * 
 */
package com.terrier.finances.gestion.ui.communs.abstrait.rest;



import java.security.NoSuchAlgorithmException;

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
import com.terrier.finances.gestion.services.communs.abstrait.rest.BudgetRestObjectMessageReader;

/**
 * Classe d'un client HTTP
 * @author vzwingma
 *
 */
public abstract class AbstractHTTPClient {


	private static final Logger LOGGER = LoggerFactory.getLogger( AbstractHTTPClient.class );
	// Tout est en JSON
	private static final MediaType JSON_MEDIA_TYPE = MediaType.APPLICATION_JSON_TYPE;


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
	public Client getClient(HttpAuthenticationFeature feature) {

		ClientConfig clientConfig = new ClientConfig();
		if(feature != null){
			clientConfig.register(feature);
		}

		try {
			//			// Install the all-trusting trust manager
			//			SSLContext sslcontext = SSLContext.getInstance("TLS");
			//
			//			sslcontext.init(null,  new TrustManager[] { new ClientHTTPTrustManager() }, new java.security.SecureRandom());
			//			HttpsURLConnection.setDefaultSSLSocketFactory(sslcontext.getSocketFactory());
			return ClientBuilder.newBuilder()
					//					.sslContext(sslcontext)
					.register(BudgetRestObjectMessageReader.class)
					.withConfig(clientConfig)
					.build();
		}
		catch(Exception e){
			LOGGER.error("Erreur envoi : Erreur lors de la création du Client HTTP {}", e.getMessage());
			return ClientBuilder.newClient(clientConfig);
		}
	}


	/**
	 * @param clientHTTP
	 * @param url
	 * @param path
	 * @param type
	 * @return
	 */
	private Invocation.Builder getInvocation(String url, String path){
		WebTarget wt = getClient().target(url);
		if(path != null){
			wt = wt.path(path);
		}
		return wt.request(JSON_MEDIA_TYPE).header("Content-type", MediaType.APPLICATION_JSON);
	}



	/**
	 * Appel POST 
	 * @param clientHTTP client utilisé
	 * @param url url appelée
	 * @param formData data envoyées
	 * @return
	 */
	/**
	 * @param invocation URL appelée
	 * @param entityData données envoyé
	 * @param responseClassType classe de la réponse
	 * @return réponse
	 */
	public <Q extends AbstractAPIObjectModel, R extends AbstractAPIObjectModel> R callHTTPPost(String url, String path, Entity<Q> entityData, Class<R> responseClassType){
		LOGGER.debug("[API POST] Appel du service [{}{}]", url, path);
		try{
			R response = getInvocation(url, path).header("Content-type", MediaType.APPLICATION_JSON)
					.post(entityData, responseClassType);
			LOGGER.debug("[API POST][200] Resultat : {}", response);
			if(response != null){
				return null;
			}
		}
		catch(WebApplicationException e){
			LOGGER.error("[API POST][{}] Erreur lors de l'appel", e.getResponse().getStatus());
		}
		catch(Exception e){
			LOGGER.error("[API POST][999] Erreur lors de l'appel", e);
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
	@Deprecated
	public boolean callHTTPGet(String url, String path){
		LOGGER.debug("[API GET] Appel du service {}", url, path);
		boolean resultat = false;
		try{

			Response response = getInvocation(url, path).get();
			if(response != null){
				LOGGER.debug("[API GET] Resultat : {}", response.getStatus());
				resultat = response.getStatus() == 200;
			}
		}
		catch(Exception e){
			LOGGER.error("> Resultat : Erreur lors de l'appel HTTP GET", e);
			resultat = false;
		}
		return resultat;
	}


	/**
	 * Appel HTTP GET
	 * @param clientHTTP client HTTP
	 * @param url racine de l'URL
	 * @param urlParams paramètres de l'URL (à part pour ne pas les tracer)
	 * @return résultat de l'appel
	 */
	@Deprecated
	public Response callHTTPGetData(String url, String path){
		LOGGER.debug("[API GET]  Appel du service {}", url, path);
		try{

			Response response = getInvocation(url, path).get();
			if(response != null){
				LOGGER.debug("[API GET] Resultat : {} / MediaType {}", response, response.getMediaType());
			}
			return response;
		}
		catch(Exception e){
			LOGGER.error("> Resultat : Erreur lors de l'appel HTTP GET", e);
		}
		return null;
	}

}
