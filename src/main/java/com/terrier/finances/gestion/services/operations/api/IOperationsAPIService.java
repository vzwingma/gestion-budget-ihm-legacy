package com.terrier.finances.gestion.services.operations.api;

import com.terrier.finances.gestion.communs.budget.model.v12.BudgetMensuel;
import com.terrier.finances.gestion.communs.comptes.model.v12.CompteBancaire;
import com.terrier.finances.gestion.communs.operations.model.v12.LigneOperation;
import com.terrier.finances.gestion.communs.utils.exceptions.BudgetNotFoundException;
import com.terrier.finances.gestion.communs.utils.exceptions.CompteClosedException;
import com.terrier.finances.gestion.communs.utils.exceptions.DataNotFoundException;
import com.terrier.finances.gestion.communs.utils.exceptions.UserNotAuthorizedException;
import com.terrier.finances.gestion.services.abstrait.api.IAPIClient;

import java.time.LocalDateTime;
import java.time.Month;

/**
 * API  vers le domaine Budget
 * @author vzwingma
 *
 */
public interface IOperationsAPIService extends IAPIClient {

	/**
	 * Chargement du budget du mois courant
	 * @param compte compte 
	 * @param mois mois 
	 * @param annee année
	 * @return budget mensuel chargé et initialisé à partir des données précédentes
	 * @throws UserNotAuthorizedException 
	 */
	public BudgetMensuel chargerBudgetMensuel(String idCompte, Month mois, int annee) throws BudgetNotFoundException, DataNotFoundException, UserNotAuthorizedException;
	
	
	/**
	 * @param idBudget
	 * @return état d'activité du budget
	 */
	public boolean isBudgetMensuelActif(String idBudget);
	


	
	/**
	 * Réinitialiser un budget mensuel
	 * @param budgetMensuel budget mensuel
	 * @throws DataNotFoundException  erreur sur les données
	 * @throws BudgetNotFoundException budget introuvable
	 * @throws CompteClosedException compte clos. Impossible de réinitialiser
	 * @throws UserNotAuthorizedException erreur d'authentification
	 */
	public BudgetMensuel reinitialiserBudgetMensuel(BudgetMensuel budget) throws BudgetNotFoundException, DataNotFoundException, CompteClosedException, UserNotAuthorizedException;
	
	
	/**
	 * Charge la date de mise à jour du budget
	 * @param idBudget identifiant du budget
	 * @return la date de mise à jour du  budget
	 */
	public boolean isBudgetUpToDate(String idBudget, LocalDateTime dateToCompare);
	
	/**
	 * Lock/unlock d'un budget
	 * @param budgetActif
	 * @throws UserNotAuthorizedException  erreur d'authentification
	 * @throws DataNotFoundException  erreur lors de l'appel
	 */
	public BudgetMensuel setBudgetActif(String idBudgetMensuel, boolean budgetActif) throws UserNotAuthorizedException, DataNotFoundException;
	
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
	public BudgetMensuel ajoutLigneTransfertIntercompte(String idBudget, LigneOperation operation, CompteBancaire compteCrediteur) throws BudgetNotFoundException, DataNotFoundException, CompteClosedException, UserNotAuthorizedException;

	
	/**
	 * Mise à jour de la ligne de dépense du budget
	 * @param idBudget id budget
	 * @param operation opération à mettre à jour
	 * @throws DataNotFoundException erreur ligne non trouvé
	 * @throws BudgetNotFoundException not found
	 * @throws UserNotAuthorizedException 
	 */
	public BudgetMensuel majLigneOperation(String idBudget, LigneOperation operation) throws DataNotFoundException, BudgetNotFoundException, UserNotAuthorizedException;
	
	
	
	/**
	 * Mise à jour de la ligne comme dernière opération
	 * @param ligneId
	 * @throws UserNotAuthorizedException  erreur d'authentification
	 * @throws DataNotFoundException  erreur lors de l'appel
	 */
	public boolean setLigneDepenseAsDerniereOperation(BudgetMensuel budget, String ligneId) throws UserNotAuthorizedException, DataNotFoundException;
	
}
