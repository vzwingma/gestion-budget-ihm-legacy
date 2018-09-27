/**
 * 
 */
package com.terrier.finances.gestion.ui.operations.actions.ui.listeners;


import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.terrier.finances.gestion.communs.budget.model.BudgetMensuel;
import com.terrier.finances.gestion.communs.operations.model.LigneOperation;
import com.terrier.finances.gestion.communs.operations.model.enums.EtatOperationEnum;
import com.terrier.finances.gestion.communs.utils.exceptions.BudgetNotFoundException;
import com.terrier.finances.gestion.communs.utils.exceptions.DataNotFoundException;
import com.terrier.finances.gestion.communs.utils.exceptions.UserNotAuthorizedException;
import com.terrier.finances.gestion.ui.budget.ui.BudgetMensuelController;
import com.terrier.finances.gestion.ui.communs.ConfirmDialog;
import com.terrier.finances.gestion.ui.communs.abstrait.listeners.AbstractComponentListener;
import com.terrier.finances.gestion.ui.operations.actions.ui.ActionsOperation;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;

/**
 * Listener sur les actions sur la ligne de dépenses
 * @author vzwingma
 *
 */
public class ActionsOperationClickListener extends AbstractComponentListener implements Button.ClickListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9208265594447141871L;
	/**
	 * Logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(ActionsOperationClickListener.class);


	private ActionsOperation actions;

	@Override
	public void buttonClick(ClickEvent event) {
		EtatOperationEnum etat = EtatOperationEnum.PREVUE;
		actions = event.getButton().findAncestor(ActionsOperation.class);
		if(event.getButton().getId().equals("buttonReel")){
			LOGGER.trace("Action : Activation");
			etat = EtatOperationEnum.REALISEE;
		}
		else if(event.getButton().getId().equals("buttonAnnuler")){
			LOGGER.trace("Action : Annulation");
			etat = EtatOperationEnum.ANNULEE;
		}
		else if(event.getButton().getId().equals("buttonReporter")){
			LOGGER.trace("Action : Reporter");
			etat = EtatOperationEnum.REPORTEE;
		}
		else if(event.getButton().getId().equals("buttonPrevue")){
			LOGGER.trace("Action : Prevue");
			etat = EtatOperationEnum.PREVUE;
		}
		else if(event.getButton().getId().equals("buttonSupprimer")){
			LOGGER.trace("Action : Supprimé");
			etat = null;
			// Confirmation
			setPopupModale(new ConfirmDialog("Suppression de l'opération", 
					"Voulez-vous supprimer l'opération ?", "Oui", "Non", 
					ok -> {
						if(ok){
							updateLigne(null);
						}
					}
					));			
		}		


		// Si non suppression mise à jour directe. Sinon, confirm dialog
		if(etat != null){
			// MISE A Jour des boutons. Désactivation du bouton cliqué
			for (Iterator<Component> iterator = ((CssLayout)(event.getButton().getParent())).iterator(); iterator.hasNext();) {
				Component type = iterator.next();
				if(type instanceof Button){
					type.setVisible(true);
				}
			}
			event.getButton().setVisible(false);	
			updateLigne(etat);
		}
	}


	/**
	 * Mise à jour de la ligne
	 */
	private void updateLigne(EtatOperationEnum etat){

		// Mise à jour de l'état
		actions.getControleur().miseAJourEtatLigne(etat);

		// Recalcul du budget
		BudgetMensuel budget = getUserSession().getBudgetCourant();
		
		LigneOperation operationModifiee = actions.getControleur().getOperation();
		operationModifiee.setEtat(etat);
		
		try{
			BudgetMensuel budgetUpdated = getControleur(BudgetMensuelController.class).getServiceOperations().majLigneOperation(budget.getId(), operationModifiee);
			getUserSession().updateBudgetInSession(budgetUpdated);
			getControleur(BudgetMensuelController.class).miseAJourVueDonnees();
		}
		catch(DataNotFoundException|BudgetNotFoundException | UserNotAuthorizedException e){
			Notification.show("l'opération est introuvable ou n'a pas été enregistrée", Type.ERROR_MESSAGE);
		}
	}
}
