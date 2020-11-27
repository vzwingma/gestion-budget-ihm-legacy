/**
 * Client HTTP
 */
package com.terrier.finances.gestion.services.abstrait.api;

import com.terrier.finances.gestion.communs.abstrait.AbstractAPIObjectModel;
import com.terrier.finances.gestion.communs.api.AbstractHTTPReactiveClient;
import com.terrier.finances.gestion.communs.api.model.Info;
import com.terrier.finances.gestion.communs.utils.data.BudgetApiUrlEnum;
import com.terrier.finances.gestion.services.FacadeServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

/**
 * Classe d'un client HTTP
 * @author vzwingma
 *
 */
public abstract class AbstractAPIClient<R extends AbstractAPIObjectModel> extends AbstractHTTPReactiveClient implements IAPIClient{

	protected static final Logger LOGGER = LoggerFactory.getLogger( AbstractAPIClient.class );

	/**
	 * 
	 * @param responseClassType classe de l'objet
	 */
	public AbstractAPIClient(Class<R> responseClassType) {
		this.responseClassType = responseClassType;
	}
	
	private final Class<R> responseClassType;
	/**
	 * Statut du Services
	 */
	public Mono<Info> getInfo() {
		return callAPIandReturnResponse(HttpMethod.GET, BudgetApiUrlEnum.ACTUATORS_INFO_FULL, null, null, null, Info.class);
	}
	
	
	/**
	 * Appel POST vers les API Services
	 * @param <Q> Classe des données à envoyer
	 * @param path chemin
	 * @param dataToSend body à envoyer
	 * @return réponse
	 */
	protected <Q extends AbstractAPIObjectModel, R extends  AbstractAPIObjectModel>
		Mono<R> callHTTPPostResponse(String path, Q dataToSend, Class<R> classResponse) {
		return callAPIandReturnResponse(HttpMethod.POST, path, null, null, dataToSend, classResponse);
	}

	protected <Q extends AbstractAPIObjectModel>
	HttpStatus callHTTPPostStatus(String path,  Map<String, String> pathParams, Q dataToSend) {
		return callAPIandReturnStatus(HttpMethod.POST, path, pathParams, null, dataToSend);
	}
	/**
	 * Appel POST vers les API Services
	 * @param <Q> Classe des données à envoyer
	 * @param path chemin
	 * @param pathParams paramètres d'URL
	 * @param dataToSend body à envoyer
	 * @return réponse
	 */
	protected <Q extends AbstractAPIObjectModel, R extends  AbstractAPIObjectModel>
	Mono<R> callHTTPPostResponse(String path, Map<String, String> pathParams, Q dataToSend, Class<R> classResponse) {
		return callAPIandReturnResponse(HttpMethod.POST, path, pathParams, null, dataToSend, classResponse);
	}

	/**
	 * Appel POST vers les API Services
	 * @param <Q> classe du contenu de la requête 
	 * @param path chemin
	 * @param dataToSend body à envoyer
	 * @return réponse
	 */
	protected  <Q extends AbstractAPIObjectModel>
	Mono<R> callHTTPPost(String path, Q dataToSend) {
		return callAPIandReturnResponse(HttpMethod.POST, path, null, null, dataToSend, responseClassType);
	}

	
	/**
	 * Appel POST vers les API Services
	 * @param <Q> classe de l'enquete 
	 * @param path chemin
	 * @param dataToSend body à envoyer
	 * @return réponse body
	 */
	protected <Q extends AbstractAPIObjectModel> 
	Mono<R> callHTTPPost(String path,  Map<String, String> pathParams, Map<String, String> queryParams, Q dataToSend) {
		return callAPIandReturnResponse(HttpMethod.POST, path, pathParams, queryParams, dataToSend, responseClassType);
	}

	/**
	 * Appel HTTP GET
	 * @param path paramètres de l'URL
	 * @return résultat de l'appel
	 */
	protected boolean callHTTPGet(String path, Map<String, String> pathParams, Map<String, String> queryParams) {
		return callAPIandReturnStatus(HttpMethod.GET, path, pathParams, queryParams, null).is2xxSuccessful();
	}

	/**
	 * @param path chemin
	 * @return données en réponse
	 */
	protected Mono<R> callHTTPGetData(String path) {
		return callAPIandReturnResponse(HttpMethod.GET, path, null, null, null, responseClassType);
	}


	/**
	 * @param path chemin
	 * @return données en réponse
	 */
	protected Mono<List<R>> callHTTPGetListData(String path) {
		return callAPIandReturnResponses(HttpMethod.GET, path, responseClassType).collectList();
	}
	/**
	 * @param path chemin
	 * @return données en réponse
	 */
	protected Mono<R> callHTTPGetData(String path, Map<String, String> pathParams,  Map<String, String> queryParams) {
		return callAPIandReturnResponse(HttpMethod.GET, path, pathParams, queryParams, null, responseClassType);
	}



	/**
	 * Appel DELETE
	 * @param path racine de l'URL
	 * @return résultat de l'appel
	 */
	protected Mono<R> callHTTPDeleteData(String path, Map<String, String> pathParams) {
		return callAPIandReturnResponse(HttpMethod.DELETE, path, pathParams, null, null, responseClassType);
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


	@Override
	public String getAccessToken() {
		return FacadeServices.get().getServiceUserSessions().getSession().getAccessToken();
	}

}
