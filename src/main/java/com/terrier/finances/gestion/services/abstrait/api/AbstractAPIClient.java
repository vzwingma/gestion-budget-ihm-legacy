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
public abstract class AbstractAPIClient<R extends AbstractAPIObjectModel> extends AbstractHTTPReactiveClient implements IAPIClient{

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
	public Mono<Info> getInfo() throws DataNotFoundException, UserNotAuthorizedException {
		return callAPIandReturnMono(HttpMethod.GET, BudgetApiUrlEnum.ACTUATORS_INFO_FULL, null, null, null, Info.class);
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

		return callAPIandReturnResponse(HttpMethod.POST, path, null, null, dataToSend);
	}
	/**
	 * Appel POST vers les API Services
	 * @param <Q> Classe des données à envoyer
	 * @param path chemin
	 * @param pathParams paramètres d'URL
	 * @param dataToSend body à envoyer
	 * @return réponse
	 * @throws UserNotAuthorizedException  erreur d'authentification
	 * @throws DataNotFoundException  erreur lors de l'appel
	 */
	protected <Q extends AbstractAPIObjectModel> ClientResponse 
	callHTTPPostResponse(String path, Map<String, String> pathParams, Q dataToSend) throws UserNotAuthorizedException, DataNotFoundException {

		return callAPIandReturnResponse(HttpMethod.POST, path, pathParams, null, dataToSend);
	}

	/**
	 * Appel POST vers les API Services
	 * @param <Q> classe du contenu de la requête 
	 * @param path chemin
	 * @param dataToSend body à envoyer
	 * @param responseClassType réponse type
	 * @return réponse
	 * @throws UserNotAuthorizedException  erreur d'authentification
	 * @throws DataNotFoundException  erreur lors de l'appel
	 */
	protected  <Q extends AbstractAPIObjectModel>
	Mono<R> callHTTPPost(String path, Q dataToSend) throws UserNotAuthorizedException, DataNotFoundException{

		return callAPIandReturnMono(HttpMethod.POST, path, null, null, dataToSend, responseClassType);
	}

	
	/**
	 * Appel POST vers les API Services
	 * @param <Q> classe de l'enquete 
	 * @param path chemin
	 * @param params paramètres
	 * @param dataToSend body à envoyer
	 * @return réponse body
	 * @throws UserNotAuthorizedException utilisateur non authorisé
	 * @throws DataNotFoundException  donnée introuvable
	 */
	protected <Q extends AbstractAPIObjectModel> 
	Mono<R> callHTTPPost(String path,  Map<String, String> pathParams, Map<String, String> queryParams, Q dataToSend) throws UserNotAuthorizedException, DataNotFoundException{

		return callAPIandReturnMono(HttpMethod.POST, path, pathParams, queryParams, dataToSend, responseClassType);
	}

	/**
	 * Appel HTTP GET
	 * @param params params
	 * @param path paramètres de l'URL
	 * @return résultat de l'appel
	 * @throws UserNotAuthorizedException  erreur d'authentification
	 * @throws DataNotFoundException  erreur lors de l'appel
	 */
	protected boolean callHTTPGet(String path, Map<String, String> pathParams, Map<String, String> queryParams) throws UserNotAuthorizedException, DataNotFoundException{
		return callAPIandReturnResponse(HttpMethod.GET, path, pathParams, queryParams, null).statusCode().is2xxSuccessful();
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

		return callAPIandReturnMono(HttpMethod.GET, path, null, null, null, responseClassType);
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

		return callAPIandReturnFlux(HttpMethod.GET, path, null, null, null, responseClassType).collectList();
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
	protected Mono<R> callHTTPGetData(String path, Map<String, String> pathParams,  Map<String, String> queryParams) throws UserNotAuthorizedException, DataNotFoundException{
		return callAPIandReturnMono(HttpMethod.GET, path, pathParams, queryParams, null, responseClassType);
	}



	/**
	 * Appel DELETE
	 * @param path racine de l'URL
	 * @return résultat de l'appel
	 */
	protected Mono<R> callHTTPDeleteData(String path, Map<String, String> pathParams) throws UserNotAuthorizedException, DataNotFoundException{
		return callAPIandReturnMono(HttpMethod.DELETE, path, pathParams, null, null, responseClassType);
	}
	

	/**
	 * 
	 * @return CorrId suite à action de l'utilisateur
	 * ou InitCorrId pour le démarrage
	 */
	public String getCorrId() {
		if(FacadeServices.get().getServiceUserSessions().getSession() != null && FacadeServices.get().getServiceUserSessions().getSession().getActionUserCorrId() != null) {
			return FacadeServices.get().getServiceUserSessions().getSession().getActionUserCorrId();			
		}
		else {
			return FacadeServices.get().getInitCorrId();
		}
		
	}
	
	/**
	 * 
	 * @return JWT Token de l'utilisateur
	 */
	@Override
	public String getJwtToken() {
		return FacadeServices.get().getServiceUserSessions().getSession().getJwtToken();
	}
}
