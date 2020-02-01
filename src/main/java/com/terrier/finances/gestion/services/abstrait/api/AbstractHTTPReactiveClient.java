/**
 * Client HTTP
 */
package com.terrier.finances.gestion.services.abstrait.api;

import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestBodySpec;

import com.terrier.finances.gestion.communs.abstrait.AbstractAPIObjectModel;
import com.terrier.finances.gestion.communs.api.config.ApiUrlConfigEnum;
import com.terrier.finances.gestion.communs.api.security.ApiHeaderIdEnum;
import com.terrier.finances.gestion.communs.api.security.JwtConfigEnum;
import com.terrier.finances.gestion.communs.utils.config.EnvVarConfigUtils;
import com.terrier.finances.gestion.communs.utils.exceptions.DataNotFoundException;
import com.terrier.finances.gestion.communs.utils.exceptions.UserNotAuthorizedException;
import com.terrier.finances.gestion.services.FacadeServices;
import com.terrier.finances.gestion.services.abstrait.api.filters.LogApiFilter;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

/**
 * Classe d'un client HTTP
 * @author vzwingma
 *
 */
public abstract class AbstractHTTPReactiveClient{


	protected static final Logger LOGGER = LoggerFactory.getLogger( AbstractHTTPReactiveClient.class );


	protected final String serviceURI;

	@Autowired
	private LogApiFilter logFilter;


	public AbstractHTTPReactiveClient() {
		serviceURI = EnvVarConfigUtils.getStringEnvVar(getConfigServiceURI());
	}

	public abstract ApiUrlConfigEnum getConfigServiceURI();

	/**
	 * Créé un client HTTP 
	 * (dans une méthode séparée pour pouvoir être mocké facilement)
	 * @return client HTTP
	 * @throws NoSuchAlgorithmException 
	 */
	private WebClient getClient() {

		//		// Register des converters
		//		clientConfig.register(new ListAPIObjectModelReader<AbstractAPIObjectModel>());
		//		clientConfig.register(new APIObjectModelReader<AbstractAPIObjectModel>());

		try {
			// Install the all-trusting trust manager		
			SslContext sslContext = SslContextBuilder
					.forClient()
//					.trustManager(InsecureTrustManagerFactory.INSTANCE)
					.build();
				HttpClient httpClient = HttpClient.create()
					.secure(sslContextSpec -> sslContextSpec.sslContext(sslContext));
				ClientHttpConnector sslConnector = new ReactorClientHttpConnector(httpClient);
			
			return WebClient.builder()
					.clientConnector(sslConnector)
					.filter(logFilter)
					.baseUrl(serviceURI)
					.build();
		}
		catch(Exception e){
			LOGGER.error("Erreur envoi : Erreur lors de la création du Client HTTP {}", e.getMessage());
			return WebClient.builder().baseUrl(serviceURI).build();
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
	 * Spec du client
	 * @param method méthodes HTTP
	 * @param path chemin à appeler
	 * @param queryParams paramètres de requêtes (si existants)
	 * @return Config
	 */
	private RequestBodySpec getInvocation(HttpMethod method, String path, Map<String, String> queryParams){

		RequestBodySpec spec = getClient()
				.method(method)
				// URI & Params
				.uri(path, queryParams)
				// Tout est en JSON
				.accept(MediaType.APPLICATION_JSON)	   
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.acceptCharset(Charset.forName("UTF-8"));

		if(getJwtToken() != null){
			spec = spec.header(JwtConfigEnum.JWT_HEADER_AUTH, getJwtToken());
			LOGGER.trace("[JWT Token={}]", getJwtToken());
		}
		// Correlation ID
		String corrID = UUID.randomUUID().toString();
		org.slf4j.MDC.put(ApiHeaderIdEnum.HEADER_CORRELATION_ID, "["+ApiHeaderIdEnum.LOG_CORRELATION_ID+"="+corrID+"]");
		spec = spec.header(ApiHeaderIdEnum.HEADER_CORRELATION_ID, corrID);
		return spec;
	}



	/**
	 * @param <R> classe de la réponse
	 * @param method méthode HTTP
	 * @param path chemin
	 * @param params paramètres
	 * @param apiBodyObject body
	 * @param responseClassType classe <R>
	 * @return données en réponse
	 * @throws UserNotAuthorizedException  erreur d'authentification
	 * @throws DataNotFoundException  erreur lors de l'appel
	 */
	protected <Q extends AbstractAPIObjectModel, R extends AbstractAPIObjectModel> 
	Mono<R> callAPIandReturnMono(HttpMethod method, String path, Map<String, String> params, Q apiBodyObject, Class<R> responseClassType) throws UserNotAuthorizedException, DataNotFoundException{
		return callAPIandReturnResponse(method, path, params, apiBodyObject)
				.bodyToMono(responseClassType);
	}


	/**
	 * @param method méthode HTTP
	 * @param path chemin
	 * @param params paramètres
	 * @return données en réponse
	 * @throws UserNotAuthorizedException  erreur d'authentification
	 * @throws DataNotFoundException  erreur lors de l'appel
	 */
	protected <R extends AbstractAPIObjectModel> 
	ClientResponse callAPIandReturnResponse(HttpMethod method, String path, Map<String, String> params, R apiBodyObject) throws UserNotAuthorizedException, DataNotFoundException{
		if(path != null){
			ClientResponse response = getInvocation(method, path, params)
					.bodyValue(BodyInserters.fromValue(apiBodyObject))
					.exchange()
					.block();
			LOGGER.debug("Réponse : [{}]", response);
			return response;
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
	/**
	 * @param <Q> type de l'objet envoyé
	 * @param <R> type de la réponse
	 * @param method méthode HTTP
	 * @param path chemin
	 * @param params paramètres
	 * @param apiBodyObject objet à envoyer
	 * @param responseClassType réponse
	 * @return
	 * @throws UserNotAuthorizedException
	 * @throws DataNotFoundException
	 */
	protected <Q extends AbstractAPIObjectModel, R extends AbstractAPIObjectModel> 
		Flux<R> callAPIandReturnFlux(HttpMethod method, String path, Map<String, String> params, Q apiBodyObject, Class<R> responseClassType) throws UserNotAuthorizedException, DataNotFoundException{
		
		Flux<R> response = callAPIandReturnResponse(method, path, params, apiBodyObject).bodyToFlux(responseClassType);
		LOGGER.debug("Réponse : [{}]", response);
		return response;
	}



	/**
	 * Catch 401 error
	 * @param c code API
	 * @param e Exception
	 * @throws UserNotAuthorizedException utilisateur non authentifié
	 * @throws DataNotFoundException  erreur lors de l'appel
	 */
	private void catchWebApplicationException(HttpMethod verbe,  Exception ex) throws UserNotAuthorizedException, DataNotFoundException {
		//		if(ex instanceof WebApplicationException) {
		//			WebApplicationException e = (WebApplicationException)ex;
		//			LOGGER.error("[{}] Erreur [{}] lors de l'appel ", verbe, e.getResponse().getStatus());
		//			if(e.getResponse().getStatusInfo().equals(Status.UNAUTHORIZED)) {
		//				throw new UserNotAuthorizedException("Utilisateur non authentifié");
		//			}
		//			else if(Status.INTERNAL_SERVER_ERROR.equals(e.getResponse().getStatusInfo()) || Status.BAD_REQUEST.equals(e.getResponse().getStatusInfo())) {
		//				throw new DataNotFoundException("Erreur lors de l'appel au service");
		//			}
		//		}
		//		else {
		//			LOGGER.error("[{}] Erreur lors de l'appel", verbe, ex);
		//		}
	}
}
