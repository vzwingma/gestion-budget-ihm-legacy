/**
 * Client HTTP
 */
package com.terrier.finances.gestion.services.abstrait.api;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.ClientResponse;

import com.terrier.finances.gestion.communs.abstrait.AbstractAPIObjectModel;
import com.terrier.finances.gestion.communs.api.AbstractHTTPReactiveClient;
import com.terrier.finances.gestion.communs.utils.data.BudgetApiUrlEnum;
import com.terrier.finances.gestion.communs.utils.exceptions.DataNotFoundException;
import com.terrier.finances.gestion.communs.utils.exceptions.UserNotAuthorizedException;
import com.terrier.finances.gestion.services.FacadeServices;
import com.terrier.finances.gestion.services.admin.model.Info;

import reactor.core.publisher.Mono;

/**
 * Classe d'un client HTTP
 * @author vzwingma
 *
 */
public abstract class AbstractAPIClient<R extends AbstractAPIObjectModel> extends AbstractHTTPReactiveClient{

	protected static final Logger LOGGER = LoggerFactory.getLogger( AbstractAPIClient.class );

	/**
	 * 
	 * @param responseClassType
	 */
	public AbstractAPIClient(Class<R> responseClassType) {
		this.responseClassType = responseClassType;
	}
	
	private Class<R> responseClassType;
	/**
	 * Statut du Services 
	 * @throws DataNotFoundException  erreur lors de l'appel
	 */
	public Info getInfo() throws DataNotFoundException, UserNotAuthorizedException {
		return callAPIandReturnMono(HttpMethod.GET, BudgetApiUrlEnum.ACTUATORS_INFO_FULL, null, null, Info.class).block();
	}
	/**
	 * Appel POST vers les API Services
	 * @param <Q> Classe des données à envoyer
	 * @param path chemin
	 * @param dataToSend body à envoyer
	 * @return réponse
	 * @throws UserNotAuthorizedException  erreur d'authentification
	 * @throws DataNotFoundException  erreur lors de l'appel
	 */
	protected <Q extends AbstractAPIObjectModel> ClientResponse 
	callHTTPPostResponse(String path, Q dataToSend) throws UserNotAuthorizedException, DataNotFoundException {

		return callAPIandReturnResponse(HttpMethod.POST, path, null, dataToSend);
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
	protected  <Q extends AbstractAPIObjectModel>
	Mono<R> callHTTPPost(String path, Q dataToSend) throws UserNotAuthorizedException, DataNotFoundException{

		return callAPIandReturnMono(HttpMethod.POST, path, null, dataToSend, responseClassType);
	}

	/**
	 * Appel POST vers les API Services
	 * @param path chemin
	 * @param params paramètres
	 * @return réponse body
	 * @throws UserNotAuthorizedException utilisateur non authorisé
	 * @throws DataNotFoundException  donnée introuvable
	 */
	protected <Q extends AbstractAPIObjectModel> 
	Mono<R> callHTTPPost(String path, Map<String, String> params) throws UserNotAuthorizedException, DataNotFoundException{

		return callAPIandReturnMono(HttpMethod.POST, path, params, null, responseClassType);
	}
	/**
	 * Appel POST vers les API Services
	 * @param path chemin
	 * @param params paramètres
	 * @param dataToSend body à envoyer
	 * @return réponse body
	 * @throws UserNotAuthorizedException utilisateur non authorisé
	 * @throws DataNotFoundException  donnée introuvable
	 */
	protected <Q extends AbstractAPIObjectModel> 
	Mono<R> callHTTPPost(String path, Map<String, String> params, Q dataToSend) throws UserNotAuthorizedException, DataNotFoundException{

		return callAPIandReturnMono(HttpMethod.POST, path, params, dataToSend, responseClassType);
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
		return callAPIandReturnResponse(HttpMethod.GET, path, params, null).statusCode().is2xxSuccessful();
	}

	/**
	 * @param <R> classe de la réponse
	 * @param path chemin
	 * @param responseClassType classe <R>
	 * @return données en réponse
	 * @throws UserNotAuthorizedException  erreur d'authentification
	 * @throws DataNotFoundException  erreur lors de l'appel
	 */
	protected Mono<R> callHTTPGetData(String path) throws UserNotAuthorizedException, DataNotFoundException{

		return callAPIandReturnMono(HttpMethod.GET, path, null, null, responseClassType);
	}


	/**
	 * @param <R> classe de la réponse
	 * @param path chemin
	 * @param responseClassType classe <R>
	 * @return données en réponse
	 * @throws UserNotAuthorizedException  erreur d'authentification
	 * @throws DataNotFoundException  erreur lors de l'appel
	 */
	protected Mono<List<R>> callHTTPGetListData(String path) throws UserNotAuthorizedException, DataNotFoundException{

		return callAPIandReturnFlux(HttpMethod.GET, path, null, null, responseClassType).collectList();
	}
	/**
	 * @param <R> classe de la réponse
	 * @param path chemin
	 * @param params paramètres
	 * @param responseClassType classe <R>
	 * @return données en réponse
	 * @throws UserNotAuthorizedException  erreur d'authentification
	 * @throws DataNotFoundException  erreur lors de l'appel
	 */
	protected Mono<R> callHTTPGetData(String path, Map<String, String> params) throws UserNotAuthorizedException, DataNotFoundException{
		return callAPIandReturnMono(HttpMethod.GET, path, params, null, responseClassType);
	}



	/**
	 * Appel DELETE
	 * @param path racine de l'URL
	 * @return résultat de l'appel
	 */
	protected Mono<R> callHTTPDeleteData(String path) throws UserNotAuthorizedException, DataNotFoundException{
		return callAPIandReturnMono(HttpMethod.DELETE, path, null, null, responseClassType);
	}
	

	/**
	 * 
	 * @return JWT Token de l'utilisateur
	 */
	@Override
	public String getJwtToken() {
		return FacadeServices.get().getServiceUserSessions().getUserSession().getJwtToken();
	}
}
