package com.terrier.finances.gestion.services.operations.api;

import java.time.Month;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.terrier.finances.gestion.communs.budget.model.BudgetMensuel;
import com.terrier.finances.gestion.communs.comptes.model.CompteBancaire;
import com.terrier.finances.gestion.communs.comptes.model.api.IntervallesCompteAPIObject;
import com.terrier.finances.gestion.communs.operations.model.LigneOperation;
import com.terrier.finances.gestion.communs.utils.data.BudgetApiUrlEnum;
import com.terrier.finances.gestion.communs.utils.data.BudgetDataUtils;
import com.terrier.finances.gestion.communs.utils.exceptions.BudgetNotFoundException;
import com.terrier.finances.gestion.communs.utils.exceptions.CompteClosedException;
import com.terrier.finances.gestion.communs.utils.exceptions.DataNotFoundException;
import com.terrier.finances.gestion.communs.utils.exceptions.UserNotAuthorizedException;
import com.terrier.finances.gestion.services.abstrait.api.AbstractHTTPClient;
import com.terrier.finances.gestion.services.parametrages.api.ParametragesAPIService;
import com.terrier.finances.gestion.ui.communs.config.AppConfigEnum;

/**
 * API  vers le domaine Budget
 * @author vzwingma
 *
 */
@Controller
public class OperationsAPIService extends AbstractHTTPClient {

	@Autowired
	ParametragesAPIService parametrageAPIServices;
	
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
		BudgetMensuel budget = callHTTPGetData(BudgetApiUrlEnum.BUDGET_QUERY_FULL, params, BudgetMensuel.class);
		completeCategoriesOnOperation(budget);
		return budget;
	}
	
	
	/**
	 * @param idBudget
	 * @return état d'activité du budget
	 */
	public boolean isBudgetMensuelActif(String idBudget){
		String path = BudgetApiUrlEnum.BUDGET_ETAT_FULL.replace(BudgetApiUrlEnum.URL_PARAM_ID_BUDGET, idBudget);
		Map<String, String> params = new HashMap<>();
		params.put("actif", "true");
		try {
			return callHTTPGet(path, params);
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
		String path = BudgetApiUrlEnum.BUDGET_ID_FULL.replace(BudgetApiUrlEnum.URL_PARAM_ID_BUDGET, budget.getId());
		BudgetMensuel budgetInit = callHTTPDeleteData(path, BudgetMensuel.class);
		completeCategoriesOnOperation(budgetInit);
		return budgetInit;
	}
	
	
	/**
	 * Charge la date de mise à jour du budget
	 * @param idBudget identifiant du budget
	 * @return la date de mise à jour du  budget
	 */
	public boolean isBudgetUpToDate(String idBudget, Date dateToCompare) {
		String path = BudgetApiUrlEnum.BUDGET_ETAT_FULL.replace(BudgetApiUrlEnum.URL_PARAM_ID_BUDGET, idBudget);
		Map<String, String> params = new HashMap<>();
		params.put("uptodateto", Long.toString(dateToCompare.getTime()));
		try {
			return callHTTPGet(path, params);
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
		String path = BudgetApiUrlEnum.BUDGET_ETAT_FULL.replace(BudgetApiUrlEnum.URL_PARAM_ID_BUDGET, idBudgetMensuel);
		Map<String, String> params = new HashMap<>();
		params.put("actif", Boolean.toString(budgetActif));
		BudgetMensuel budget = callHTTPPost(path, params, null, BudgetMensuel.class);
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
		String url = BudgetApiUrlEnum.BUDGET_OPERATION_INTERCOMPTE_FULL.replace(BudgetApiUrlEnum.URL_PARAM_ID_BUDGET, idBudget).replace(BudgetApiUrlEnum.URL_PARAM_ID_OPERATION, operation.getId()).replace("{idCompte}", compteCrediteur.getId());
		budgetUpdated =  callHTTPPost(url, operation, BudgetMensuel.class);
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
		String url = BudgetApiUrlEnum.BUDGET_OPERATION_FULL.replace(BudgetApiUrlEnum.URL_PARAM_ID_BUDGET, idBudget).replace(BudgetApiUrlEnum.URL_PARAM_ID_OPERATION, operation.getId());
		if(operation.getEtat() != null)
		{
			budgetUpdated =  callHTTPPost(url, operation, BudgetMensuel.class);
		}
		else {
			budgetUpdated =  callHTTPDeleteData(url, BudgetMensuel.class);
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
		String path = (BudgetApiUrlEnum.BUDGET_OPERATION_DERNIERE_FULL.replace(BudgetApiUrlEnum.URL_PARAM_ID_BUDGET, budget.getId()).replace(BudgetApiUrlEnum.URL_PARAM_ID_OPERATION, ligneId));
		Response response = callHTTPPost(path, budget);
		return response.getStatus() == 200;
	}
	
	/**
	 * Charge l'intervalle des budgets pour ce compte pour cet utilisateur
	 * @param utilisateur utilisateur
	 * @param compte id du compte
	 * @return la date du premier budget décrit pour cet utilisateur
	 * @throws UserNotAuthorizedException 
	 */
	public IntervallesCompteAPIObject getIntervallesBudgets(String compte) throws UserNotAuthorizedException, DataNotFoundException{
		String path = BudgetApiUrlEnum.BUDGET_COMPTE_INTERVALLES_FULL.replace(BudgetApiUrlEnum.URL_PARAM_ID_COMPTE, compte);
		return callHTTPGetData(path, IntervallesCompteAPIObject.class);
	}
	
	/**
	 * Réinjection des catégories dans les opérations du budget
	 * @param budget
	 */
	private void completeCategoriesOnOperation(BudgetMensuel budget){
		if(budget != null && budget.getListeOperations() != null && !budget.getListeOperations().isEmpty()){
			budget.getListeOperations()
				.stream()
				.forEach(op -> op.setSsCategorie(BudgetDataUtils.getCategorieById(op.getIdSsCategorie(), parametrageAPIServices.getCategories())));
		}
	}


	@Override
	public AppConfigEnum getConfigServiceURI() {
		return AppConfigEnum.APP_CONFIG_URL_OPERATIONS;
	}
}
