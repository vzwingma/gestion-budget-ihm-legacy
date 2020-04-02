package com.terrier.finances.gestion.services.operations.api;

import java.time.Month;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.reactive.function.client.ClientResponse;

import com.terrier.finances.gestion.communs.api.config.ApiUrlConfigEnum;
import com.terrier.finances.gestion.communs.budget.model.BudgetMensuel;
import com.terrier.finances.gestion.communs.comptes.model.CompteBancaire;
import com.terrier.finances.gestion.communs.operations.model.LigneOperation;
import com.terrier.finances.gestion.communs.utils.data.BudgetApiUrlEnum;
import com.terrier.finances.gestion.communs.utils.data.BudgetDataUtils;
import com.terrier.finances.gestion.communs.utils.exceptions.BudgetNotFoundException;
import com.terrier.finances.gestion.communs.utils.exceptions.CompteClosedException;
import com.terrier.finances.gestion.communs.utils.exceptions.DataNotFoundException;
import com.terrier.finances.gestion.communs.utils.exceptions.UserNotAuthorizedException;
import com.terrier.finances.gestion.services.abstrait.api.AbstractAPIClient;
import com.terrier.finances.gestion.services.parametrages.api.ParametragesAPIService;

/**
 * API  vers le domaine Budget
 * @author vzwingma
 *
 */
@Controller
public class OperationsAPIService extends AbstractAPIClient<BudgetMensuel> {

	@Autowired
	ParametragesAPIService parametrageAPIServices;
	
	
	public OperationsAPIService() {
		super(BudgetMensuel.class);
	}
	
	
	/**
	 * Chargement du budget du mois courant
	 * @param compte compte 
	 * @param mois mois 
	 * @param annee année
	 * @return budget mensuel chargé et initialisé à partir des données précédentes
	 * @throws UserNotAuthorizedException 
	 */
	public BudgetMensuel chargerBudgetMensuel(String idCompte, Month mois, int annee) throws BudgetNotFoundException, DataNotFoundException, UserNotAuthorizedException{
		Map<String, String> params = new HashMap<>();
		params.put("idCompte", idCompte);
		params.put("mois", Integer.toString(mois.getValue()));
		params.put("annee", Integer.toString(annee));
		BudgetMensuel budget = callHTTPGetData(BudgetApiUrlEnum.BUDGET_QUERY_FULL, null, params).block();
		completeCategoriesOnOperation(budget);
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
		try {
			return callHTTPGet(BudgetApiUrlEnum.BUDGET_ETAT_FULL, pathParams, queryParams) ;
		} catch (UserNotAuthorizedException | DataNotFoundException e) {
			return false;
		}
	}
	


	
	/**
	 * Réinitialiser un budget mensuel
	 * @param budgetMensuel budget mensuel
	 * @throws DataNotFoundException  erreur sur les données
	 * @throws BudgetNotFoundException budget introuvable
	 * @throws CompteClosedException compte clos. Impossible de réinitialiser
	 * @throws UserNotAuthorizedException erreur d'authentification
	 */
	public BudgetMensuel reinitialiserBudgetMensuel(BudgetMensuel budget) throws BudgetNotFoundException, DataNotFoundException, CompteClosedException, UserNotAuthorizedException {
		Map<String, String> pathParams = new HashMap<>();
		pathParams.put(BudgetApiUrlEnum.PARAM_ID_BUDGET, budget.getId());
		BudgetMensuel budgetInit = callHTTPDeleteData(BudgetApiUrlEnum.BUDGET_ID_FULL, pathParams).block();
		completeCategoriesOnOperation(budgetInit);
		return budgetInit;
	}
	
	
	/**
	 * Charge la date de mise à jour du budget
	 * @param idBudget identifiant du budget
	 * @return la date de mise à jour du  budget
	 */
	public boolean isBudgetUpToDate(String idBudget, Date dateToCompare) {
		Map<String, String> pathParams = new HashMap<>();
		pathParams.put(BudgetApiUrlEnum.PARAM_ID_BUDGET, idBudget);
		Map<String, String> queryParams = new HashMap<>();
		queryParams.put("uptodateto", Long.toString(dateToCompare.getTime()));
		try {
			return callHTTPGet(BudgetApiUrlEnum.BUDGET_ETAT_FULL, pathParams, queryParams);
		} catch (UserNotAuthorizedException | DataNotFoundException e) {
			return false;
		}
	}
	
	/**
	 * Lock/unlock d'un budget
	 * @param budgetActif
	 * @throws UserNotAuthorizedException  erreur d'authentification
	 * @throws DataNotFoundException  erreur lors de l'appel
	 */
	public BudgetMensuel setBudgetActif(String idBudgetMensuel, boolean budgetActif) throws UserNotAuthorizedException, DataNotFoundException{
		
		Map<String, String> pathParams = new HashMap<>();
		pathParams.put(BudgetApiUrlEnum.PARAM_ID_BUDGET, idBudgetMensuel);
		
		Map<String, String> queryParams = new HashMap<>();
		queryParams.put("actif", Boolean.toString(budgetActif));
		BudgetMensuel budget = callHTTPPost(BudgetApiUrlEnum.BUDGET_ETAT_FULL, pathParams, queryParams, null).block();
		completeCategoriesOnOperation(budget);
		return budget;
	}
	
	/**
	 * Ajout d'une ligne transfert intercompte
	 * @param ligneOperation ligne de dépense de transfert
	 * @param compteCrediteur compte créditeur
	 * @param auteur auteur de l'action
	 * @throws BudgetNotFoundException erreur budget introuvable
	 * @throws DataNotFoundException erreur données
	 * @throws CompteClosedException 
	 * @throws UserNotAuthorizedException 
	 */
	public BudgetMensuel ajoutLigneTransfertIntercompte(String idBudget, LigneOperation operation, CompteBancaire compteCrediteur) throws BudgetNotFoundException, DataNotFoundException, CompteClosedException, UserNotAuthorizedException{
		BudgetMensuel budgetUpdated = null;
		Map<String, String> pathParams = new HashMap<>();
		pathParams.put(BudgetApiUrlEnum.PARAM_ID_BUDGET, idBudget);
		pathParams.put(BudgetApiUrlEnum.PARAM_ID_OPERATION, operation.getId());
		pathParams.put(BudgetApiUrlEnum.PARAM_ID_COMPTE, compteCrediteur.getId());
		
		budgetUpdated =  callHTTPPost(BudgetApiUrlEnum.BUDGET_OPERATION_INTERCOMPTE_FULL, pathParams, null, operation).block();
		completeCategoriesOnOperation(budgetUpdated);
		return budgetUpdated;
	}

	
	/**
	 * Mise à jour de la ligne de dépense du budget
	 * @param ligneId ligne à modifier (ou à créer si elle n'existe pas
	 * @param etat état de la ligne
	 * @param auteur auteur de l'action
	 * @throws DataNotFoundException erreur ligne non trouvé
	 * @throws BudgetNotFoundException not found
	 * @throws UserNotAuthorizedException 
	 */
	public BudgetMensuel majLigneOperation(String idBudget, LigneOperation operation) throws DataNotFoundException, BudgetNotFoundException, UserNotAuthorizedException{
	
		BudgetMensuel budgetUpdated = null;
		
		Map<String, String> pathParams = new HashMap<>();
		pathParams.put(BudgetApiUrlEnum.PARAM_ID_BUDGET, idBudget);
		pathParams.put(BudgetApiUrlEnum.PARAM_ID_OPERATION, operation.getId());
		
		if(operation.getEtat() != null)
		{
			budgetUpdated =  callHTTPPost(BudgetApiUrlEnum.BUDGET_OPERATION_FULL, pathParams, null, operation).block();
		}
		else {
			budgetUpdated =  callHTTPDeleteData(BudgetApiUrlEnum.BUDGET_OPERATION_FULL, pathParams).block();
		}
		completeCategoriesOnOperation(budgetUpdated);
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
		
		ClientResponse response = callHTTPPostResponse(BudgetApiUrlEnum.BUDGET_OPERATION_DERNIERE_FULL, pathParams, budget);
		return response.statusCode().is2xxSuccessful();
	}
	
	
	/**
	 * Réinjection des catégories dans les opérations du budget
	 * @param budget
	 */
	private void completeCategoriesOnOperation(BudgetMensuel budget){
		if(budget != null && budget.getListeOperations() != null && !budget.getListeOperations().isEmpty()){
			budget.getListeOperations()
				.stream()
				.forEach(op -> op.setSsCategorie(BudgetDataUtils.getCategorieById(op.getIdSsCategorie(), 
						parametrageAPIServices.getCategories())));
		}
	}


	@Override
	public ApiUrlConfigEnum getConfigServiceURI() {
		return ApiUrlConfigEnum.APP_CONFIG_URL_OPERATIONS;
	}
}
