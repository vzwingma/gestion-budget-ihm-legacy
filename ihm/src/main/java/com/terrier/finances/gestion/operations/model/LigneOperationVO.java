package com.terrier.finances.gestion.operations.model;

import com.terrier.finances.gestion.budget.business.OperationsService;
import com.terrier.finances.gestion.model.business.budget.LigneDepense;
import com.terrier.finances.gestion.ui.components.budget.mensuel.ActionsLigneBudget;

/**
 * 
 * Ligne d'une opération dans un budget mensuel
 * @author vzwingma
 *
 */
public class LigneOperationVO extends LigneDepense {

	public LigneOperationVO(boolean budgetIsActif) {
		super(budgetIsActif);
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
				&& super.budgetIsActif){
			actionsOperation = new ActionsLigneBudget();
			actionsOperation.getControleur().setIdOperation(getId());
			actionsOperation.getControleur().miseAJourEtatLigne(getEtat());
		}
		return actionsOperation;
	}



}
