package com.terrier.finances.gestion.services.operations.api;

import com.terrier.finances.gestion.communs.api.config.ApiUrlConfigEnum;
import com.terrier.finances.gestion.communs.budget.model.v12.BudgetMensuel;
import com.terrier.finances.gestion.communs.comptes.model.v12.CompteBancaire;
import com.terrier.finances.gestion.communs.operations.model.v12.LigneOperation;
import com.terrier.finances.gestion.communs.utils.data.BudgetApiUrlEnum;
import com.terrier.finances.gestion.communs.utils.data.BudgetDateTimeUtils;
import com.terrier.finances.gestion.communs.utils.exceptions.BudgetNotFoundException;
import com.terrier.finances.gestion.communs.utils.exceptions.DataNotFoundException;
import com.terrier.finances.gestion.communs.utils.exceptions.UserNotAuthorizedException;
import com.terrier.finances.gestion.services.abstrait.api.AbstractAPIClient;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.HashMap;
import java.util.Map;

/**
 * API  vers le domaine Budget
 * @author vzwingma
 *
 */
@Controller
public class OperationsAPIService extends AbstractAPIClient<BudgetMensuel> implements IOperationsAPIService {

	
	public OperationsAPIService() {
		super(BudgetMensuel.class);
	}
	
	
	/**
	 * Chargement du budget du mois courant
	 * @param idCompte compte
	 * @param mois mois 
	 * @param annee année
	 * @return budget mensuel chargé et initialisé à partir des données précédentes
	 * @throws DataNotFoundException erreur
	 */
	public BudgetMensuel chargerBudgetMensuel(String idCompte, Month mois, int annee) throws BudgetNotFoundException, DataNotFoundException {
		Map<String, String> params = new HashMap<>();
		params.put("idCompte", idCompte);
		params.put("mois", Integer.toString(mois.getValue()));
		params.put("annee", Integer.toString(annee));
		BudgetMensuel budget = callHTTPGetData(BudgetApiUrlEnum.BUDGET_QUERY_FULL, null, params);
		if(budget == null) {
			throw new BudgetNotFoundException("Impossible de trouver le budget du compte ["+idCompte+"]");
		}
		return budget;
	}
	
	
	/**
	 * @param idBudget
	 * @return état d'activité du budget
	 */
	public boolean isBudgetMensuelActif(String idBudget){
		
		Map<String, String> pathParams = new HashMap<>();
		pathParams.put(BudgetApiUrlEnum.PARAM_ID_BUDGET, idBudget);
		Map<String, String> queryParams = new HashMap<>();
		queryParams.put("actif", "true");
		return callHTTPGet(BudgetApiUrlEnum.BUDGET_ETAT_FULL, pathParams, queryParams) ;
	}
	


	
	/**
	 * Réinitialiser un budget mensuel
	 * @param budget budget mensuel
	 * @throws DataNotFoundException  erreur sur les données
	 */
	public BudgetMensuel reinitialiserBudgetMensuel(BudgetMensuel budget) throws DataNotFoundException {
		Map<String, String> pathParams = new HashMap<>();
		pathParams.put(BudgetApiUrlEnum.PARAM_ID_BUDGET, budget.getId());
		BudgetMensuel budgetInit = callHTTPDeleteData(BudgetApiUrlEnum.BUDGET_ID_FULL, pathParams);
		return budgetInit;
	}
	
	
	/**
	 * Charge la date de mise à jour du budget
	 * @param idBudget identifiant du budget
	 * @return la date de mise à jour du  budget
	 */
	public boolean isBudgetUpToDate(String idBudget, LocalDateTime dateToCompare) {
		Map<String, String> pathParams = new HashMap<>();
		pathParams.put(BudgetApiUrlEnum.PARAM_ID_BUDGET, idBudget);
		Map<String, String> queryParams = new HashMap<>();
		queryParams.put("uptodateto", BudgetDateTimeUtils.getMillisecondsFromLocalDateTime(dateToCompare).toString());
		return callHTTPGet(BudgetApiUrlEnum.BUDGET_UP_TO_DATE_FULL, pathParams, queryParams);
	}
	
	/**
	 * Lock/unlock d'un budget
	 * @param  idBudgetMensuel budget à (dé)locker
	 * @param budgetActif flag
	 */
	public BudgetMensuel setBudgetActif(String idBudgetMensuel, boolean budgetActif) throws DataNotFoundException{
		
		Map<String, String> pathParams = new HashMap<>();
		pathParams.put(BudgetApiUrlEnum.PARAM_ID_BUDGET, idBudgetMensuel);
		
		Map<String, String> queryParams = new HashMap<>();
		queryParams.put("actif", Boolean.toString(budgetActif));
		return callHTTPPost(BudgetApiUrlEnum.BUDGET_ETAT_FULL, pathParams, queryParams, null);
	}
	
	/**
	 * Ajout d'une ligne transfert intercompte
	 * @param operation ligne de dépense de transfert
	 * @param compteCrediteur compte créditeur
	 * @param idBudget id budget
	 * @throws DataNotFoundException erreur données
	 * @throws UserNotAuthorizedException  user not
	 */
	public BudgetMensuel ajoutLigneTransfertIntercompte(String idBudget, LigneOperation operation, CompteBancaire compteCrediteur) throws DataNotFoundException {
		BudgetMensuel budgetUpdated = null;
		Map<String, String> pathParams = new HashMap<>();
		pathParams.put(BudgetApiUrlEnum.PARAM_ID_BUDGET, idBudget);
		pathParams.put(BudgetApiUrlEnum.PARAM_ID_OPERATION, operation.getId());
		pathParams.put(BudgetApiUrlEnum.PARAM_ID_COMPTE, compteCrediteur.getId());
		
		return callHTTPPost(BudgetApiUrlEnum.BUDGET_OPERATION_INTERCOMPTE_FULL, pathParams, null, operation);
	}

	
	/**
	 * Mise à jour de la ligne de dépense du budget
	 * @param operation ligne à modifier (ou à créer si elle n'existe pas
	 * @param idBudget budget
	 * @throws DataNotFoundException erreur ligne non trouvé
	 * @throws BudgetNotFoundException not found
	 * @throws UserNotAuthorizedException 
	 */
	public BudgetMensuel majLigneOperation(String idBudget, LigneOperation operation) throws DataNotFoundException{
	
		BudgetMensuel budgetUpdated = null;
		
		Map<String, String> pathParams = new HashMap<>();
		pathParams.put(BudgetApiUrlEnum.PARAM_ID_BUDGET, idBudget);
		pathParams.put(BudgetApiUrlEnum.PARAM_ID_OPERATION, operation.getId());
		
		if(operation.getEtat() != null)
		{
			budgetUpdated =  callHTTPPost(BudgetApiUrlEnum.BUDGET_OPERATION_FULL, pathParams, null, operation);
		}
		else {
			budgetUpdated =  callHTTPDeleteData(BudgetApiUrlEnum.BUDGET_OPERATION_FULL, pathParams);
		}
		return budgetUpdated;
	}
	
	
	
	/**
	 * Mise à jour de la ligne comme dernière opération
	 * @param ligneId
	 * @throws UserNotAuthorizedException  erreur d'authentification
	 * @throws DataNotFoundException  erreur lors de l'appel
	 */
	public boolean setLigneDepenseAsDerniereOperation(BudgetMensuel budget, String ligneId) throws UserNotAuthorizedException, DataNotFoundException{
		
		Map<String, String> pathParams = new HashMap<>();
		pathParams.put(BudgetApiUrlEnum.PARAM_ID_BUDGET, budget.getId());
		pathParams.put(BudgetApiUrlEnum.PARAM_ID_OPERATION, ligneId);
		return callHTTPPostStatus(BudgetApiUrlEnum.BUDGET_OPERATION_DERNIERE_FULL, pathParams, budget).is2xxSuccessful();
	}
	

	@Override
	public ApiUrlConfigEnum getConfigServiceURI() {
		return ApiUrlConfigEnum.APP_CONFIG_URL_OPERATIONS;
	}
}
