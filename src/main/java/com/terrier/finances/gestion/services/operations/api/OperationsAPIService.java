package com.terrier.finances.gestion.services.operations.api;

import java.time.Month;
import java.util.Calendar;

import org.springframework.stereotype.Controller;

import com.terrier.finances.gestion.communs.budget.model.BudgetMensuel;
import com.terrier.finances.gestion.communs.comptes.model.CompteBancaire;
import com.terrier.finances.gestion.communs.operations.model.LigneOperation;
import com.terrier.finances.gestion.communs.operations.model.enums.EtatOperationEnum;
import com.terrier.finances.gestion.communs.utils.data.BudgetApiUrlEnum;
import com.terrier.finances.gestion.communs.utils.exceptions.BudgetNotFoundException;
import com.terrier.finances.gestion.communs.utils.exceptions.CompteClosedException;
import com.terrier.finances.gestion.communs.utils.exceptions.DataNotFoundException;
import com.terrier.finances.gestion.ui.communs.abstrait.api.AbstractHTTPClient;

/**
 * API  vers le domaine Budget
 * @author vzwingma
 *
 */
@Controller
public class OperationsAPIService extends AbstractHTTPClient {

	
	/**
	 * Chargement du budget du mois courant
	 * @param compte compte 
	 * @param mois mois 
	 * @param annee année
	 * @return budget mensuel chargé et initialisé à partir des données précédentes
	 */
	public BudgetMensuel chargerBudgetMensuel(String idUtilisateur, String idCompte, Month mois, int annee) throws BudgetNotFoundException, DataNotFoundException{
		String path = new StringBuilder(BudgetApiUrlEnum.BUDGET_QUERY)
				.append("?idCompte=").append(idCompte)
				.append("&mois=").append(mois)
				.append("&annee=").append(annee)
				.append("&idUtilisateur=").append(idUtilisateur).toString();
		return callHTTPGetData(URI, path, BudgetMensuel.class);
	}
	
	
	/**
	 * @param idBudget
	 * @return état d'activité du budget
	 */
	public boolean isBudgetMensuelActif(String idBudget){
		String path = new StringBuilder(BudgetApiUrlEnum.BUDGET_ETAT).append("?actif=true").toString();
		return callHTTPGet(URI, path);
	}
	
	/**
	 * Mise à jour de la ligne de dépense du budget
	 * @param ligneId ligne à modifier
	 * @param etat état de la ligne
	 * @param auteur auteur de l'action
	 * @throws DataNotFoundException erreur ligne non trouvé
	 * @throws BudgetNotFoundException not found
	 */
	public BudgetMensuel majEtatLigneOperation(BudgetMensuel budget, String ligneId, EtatOperationEnum etat, String idUtilisateur) throws DataNotFoundException, BudgetNotFoundException{
		return budget;
	}
	

	
	/**
	 * Réinitialiser un budget mensuel
	 * @param budgetMensuel budget mensuel
	 * @throws DataNotFoundException  erreur sur les données
	 * @throws BudgetNotFoundException budget introuvable
	 * @throws CompteClosedException compte clos. Impossible de réinitialiser
	 */
	public BudgetMensuel reinitialiserBudgetMensuel(BudgetMensuel budgetMensuel, String idUtilisateur) throws BudgetNotFoundException, DataNotFoundException, CompteClosedException {
		return budgetMensuel;
	}
	
	
	/**
	 * Charge la date de mise à jour du budget
	 * @param idBudget identifiant du budget
	 * @return la date de mise à jour du  budget
	 */
	public boolean isBudgetUpToDate(String idBudget, Calendar dateToCompare, String utilisateur) {
		return true;
	}
	
	/**
	 * Lock/unlock d'un budget
	 * @param budgetActif
	 */
	public BudgetMensuel setBudgetActif(BudgetMensuel budgetMensuel, boolean budgetActif, String idUtilisateur){
		return budgetMensuel;
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
	public BudgetMensuel ajoutLigneTransfertIntercompte(String idBudget, LigneOperation ligneOperation, CompteBancaire compteCrediteur, String idUtilisateur) throws BudgetNotFoundException, DataNotFoundException, CompteClosedException{
		return null;
	}
	
	/**
	 * Ajout d'une ligne de dépense
	 * @param ligneOperation ligne de dépense
	 * @param idUtilisateur auteur de l'action 
	 * @throws BudgetNotFoundException 
	 */
	public BudgetMensuel ajoutOperationEtCalcul(String idBudget, LigneOperation ligneOperation, String idUtilisateur) throws BudgetNotFoundException{
		return new BudgetMensuel();
	}
	/**
	 * Mise à jour de la ligne comme dernière opération
	 * @param ligneId
	 */
	public void setLigneDepenseAsDerniereOperation(BudgetMensuel budget, String ligneId, String idUtilisateur){
		
	}
	
	/**
	 * Calcul du budget Courant et sauvegarde
	 * @param budget budget à sauvegarder
	 */
	public BudgetMensuel calculEtSauvegardeBudget(BudgetMensuel budget, String utilisateur){
		return budget;
	}
}
