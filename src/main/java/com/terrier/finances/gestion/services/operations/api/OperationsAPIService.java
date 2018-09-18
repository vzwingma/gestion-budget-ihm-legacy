package com.terrier.finances.gestion.services.operations.api;

import java.time.Month;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.terrier.finances.gestion.communs.budget.model.BudgetMensuel;
import com.terrier.finances.gestion.communs.comptes.model.CompteBancaire;
import com.terrier.finances.gestion.communs.operations.model.LigneOperation;
import com.terrier.finances.gestion.communs.operations.model.enums.EtatOperationEnum;
import com.terrier.finances.gestion.communs.utils.data.BudgetApiUrlEnum;
import com.terrier.finances.gestion.communs.utils.data.BudgetDataUtils;
import com.terrier.finances.gestion.communs.utils.exceptions.BudgetNotFoundException;
import com.terrier.finances.gestion.communs.utils.exceptions.CompteClosedException;
import com.terrier.finances.gestion.communs.utils.exceptions.DataNotFoundException;
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
	 */
	public BudgetMensuel chargerBudgetMensuel(String idCompte, Month mois, int annee) throws BudgetNotFoundException, DataNotFoundException{
		String path = new StringBuilder(BudgetApiUrlEnum.BUDGET_QUERY_FULL).toString();

		Map<String, String> params = new HashMap<>();
		params.put("idCompte", idCompte);
		params.put("mois", Integer.toString(mois.getValue()));
		params.put("annee", Integer.toString(annee));
		BudgetMensuel budget = callHTTPGetData(path, params, BudgetMensuel.class);
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
		String path = new StringBuilder(BudgetApiUrlEnum.BUDGET_ID_FULL.replace("{idBudget}", budget.getId())).toString();
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
	 */
	public BudgetMensuel setBudgetActif(String idBudgetMensuel, boolean budgetActif){
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
	 * Ajout d'une ligne de dépense
	 * @param ligneOperation ligne de dépense
	 * @param idUtilisateur auteur de l'action 
	 * @throws BudgetNotFoundException 
	 */
	public BudgetMensuel ajoutOperationEtCalcul(String idBudget, LigneOperation ligneOperation) throws BudgetNotFoundException{
		return new BudgetMensuel();
	}
	
	
	/**
	 * Mise à jour de la ligne de dépense du budget
	 * @param ligneId ligne à modifier
	 * @param etat état de la ligne
	 * @param auteur auteur de l'action
	 * @throws DataNotFoundException erreur ligne non trouvé
	 * @throws BudgetNotFoundException not found
	 */
	public BudgetMensuel majEtatLigneOperation(BudgetMensuel budget, String ligneId, EtatOperationEnum etat) throws DataNotFoundException, BudgetNotFoundException{
		return budget;
	}
	
	
	
	/**
	 * Mise à jour de la ligne comme dernière opération
	 * @param ligneId
	 */
	public void setLigneDepenseAsDerniereOperation(BudgetMensuel budget, String ligneId){
		
	}
	
	/**
	 * Calcul du budget Courant et sauvegarde
	 * @param budget budget à sauvegarder
	 */
	public BudgetMensuel miseAJourBudget(BudgetMensuel budget){
		String path = new StringBuilder(BudgetApiUrlEnum.BUDGET_ID_FULL.replace("{idBudget}", budget.getId())).toString();
		BudgetMensuel budgetUpdated =  callHTTPPost(path, budget, BudgetMensuel.class);
		completeCategoriesOnOperation(budgetUpdated);
		return budgetUpdated;
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
