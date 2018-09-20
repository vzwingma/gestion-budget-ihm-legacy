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
import com.terrier.finances.gestion.communs.operations.model.LigneOperation;
import com.terrier.finances.gestion.communs.utils.data.BudgetApiUrlEnum;
import com.terrier.finances.gestion.communs.utils.data.BudgetDataUtils;
import com.terrier.finances.gestion.communs.utils.exceptions.BudgetNotFoundException;
import com.terrier.finances.gestion.communs.utils.exceptions.CompteClosedException;
import com.terrier.finances.gestion.communs.utils.exceptions.DataNotFoundException;
import com.terrier.finances.gestion.communs.utils.exceptions.UserNotAuthorizedException;
import com.terrier.finances.gestion.services.abstrait.api.AbstractHTTPClient;
import com.terrier.finances.gestion.services.parametrages.api.ParametragesAPIService;

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
		String path = BudgetApiUrlEnum.BUDGET_ETAT_FULL.replace("{idBudget}", idBudget);
		Map<String, String> params = new HashMap<>();
		params.put("actif", "true");
		return callHTTPGet(path, params);
	}
	


	
	/**
	 * Réinitialiser un budget mensuel
	 * @param budgetMensuel budget mensuel
	 * @throws DataNotFoundException  erreur sur les données
	 * @throws BudgetNotFoundException budget introuvable
	 * @throws CompteClosedException compte clos. Impossible de réinitialiser
	 */
	public BudgetMensuel reinitialiserBudgetMensuel(BudgetMensuel budget) throws BudgetNotFoundException, DataNotFoundException, CompteClosedException {
		String path = BudgetApiUrlEnum.BUDGET_ID_FULL.replace("{idBudget}", budget.getId());
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
		String path = BudgetApiUrlEnum.BUDGET_ETAT_FULL.replace("{idBudget}", idBudget);
		Map<String, String> params = new HashMap<>();
		params.put("uptodateto", Long.toString(dateToCompare.getTime()));
		return callHTTPGet(path, params);
	}
	
	/**
	 * Lock/unlock d'un budget
	 * @param budgetActif
	 * @throws UserNotAuthorizedException 
	 */
	public BudgetMensuel setBudgetActif(String idBudgetMensuel, boolean budgetActif) throws UserNotAuthorizedException{
		String path = BudgetApiUrlEnum.BUDGET_ETAT_FULL.replace("{idBudget}", idBudgetMensuel);
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
	 */
	public BudgetMensuel ajoutLigneTransfertIntercompte(String idBudget, LigneOperation ligneOperation, CompteBancaire compteCrediteur) throws BudgetNotFoundException, DataNotFoundException, CompteClosedException{
		return null;
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
		String path = BudgetApiUrlEnum.BUDGET_OPERATIONS_FULL.replace("{idBudget}", idBudget);
		BudgetMensuel budgetUpdated =  callHTTPPost(path, operation, BudgetMensuel.class);
		completeCategoriesOnOperation(budgetUpdated);
		return budgetUpdated;
	}
	
	
	
	/**
	 * Mise à jour de la ligne comme dernière opération
	 * @param ligneId
	 * @throws UserNotAuthorizedException erreur auth
	 */
	public boolean setLigneDepenseAsDerniereOperation(BudgetMensuel budget, String ligneId) throws UserNotAuthorizedException{
		String path = (BudgetApiUrlEnum.BUDGET_OPERATIONS_ID_DERNIERE_FULL.replace("{idBudget}", budget.getId()).replace("{idOperation}", ligneId));
		Response response = callHTTPPost(path, budget);
		return response.getStatus() == 200;
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
}
