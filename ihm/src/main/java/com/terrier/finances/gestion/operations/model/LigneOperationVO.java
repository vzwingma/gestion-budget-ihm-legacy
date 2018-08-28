package com.terrier.finances.gestion.operations.model;

import java.util.UUID;

import com.terrier.finances.gestion.budget.business.OperationsService;
import com.terrier.finances.gestion.model.business.parametrage.CategorieDepense;
import com.terrier.finances.gestion.model.enums.EtatLigneOperationEnum;
import com.terrier.finances.gestion.model.enums.TypeOperationEnum;
import com.terrier.finances.gestion.ui.components.budget.mensuel.ActionsLigneBudget;

/**
 * 
 * Ligne d'une opération dans un budget mensuel
 * @author vzwingma
 *
 */
public class LigneOperationVO extends LigneOperation  {

	public LigneOperationVO(boolean budgetIsActif) {
		super(budgetIsActif);
	}


	/**
	 * @param ssCategorie
	 * @param libelle
	 * @param typeDepense
	 * @param absValeur
	 * @param etat
	 * @param periodique
	 * @param budgetActif
	 */
	public LigneOperationVO(CategorieDepense ssCategorie, String libelle, TypeOperationEnum typeDepense,
			String absValeur, EtatLigneOperationEnum etat, boolean periodique, boolean budgetActif) {
		super(ssCategorie, libelle, typeDepense, absValeur, etat, periodique, budgetActif);
	}



	/**
	 * 
	 */
	private static final long serialVersionUID = -2932267709864103657L;
	
	/**
	 * @return the actionsOperation
	 */
	public ActionsLigneBudget getActionsOperation() {
		// Pas d'action pour les réserves
		ActionsLigneBudget actionsOperation = null;
		if(!OperationsService.ID_SS_CAT_RESERVE.equals(getSsCategorie().getId())
				&& !OperationsService.ID_SS_CAT_PREVISION_SANTE.equals(getSsCategorie().getId())
				&& super.isBudgetActif()){
			actionsOperation = new ActionsLigneBudget();
			actionsOperation.getControleur().setIdOperation(UUID.fromString(getId()));
			actionsOperation.getControleur().miseAJourEtatLigne(getEtat());
		}
		return actionsOperation;
	}



}
