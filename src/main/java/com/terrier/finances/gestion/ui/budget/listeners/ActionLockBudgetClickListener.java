/**
 * 
 */
package com.terrier.finances.gestion.ui.budget.listeners;

import com.terrier.finances.gestion.communs.utils.exceptions.DataNotFoundException;
import com.terrier.finances.gestion.ui.budget.ui.BudgetMensuelPage;
import com.terrier.finances.gestion.ui.communs.ConfirmDialog;
import com.terrier.finances.gestion.ui.communs.abstrait.listeners.AbstractComponentListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Notification;

/**
 * Lock du mois
 * @author vzwingma
 *
 */
public class ActionLockBudgetClickListener extends AbstractComponentListener implements ClickListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1823872638217135776L;

	/* (non-Javadoc)
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		Button editer = event.getButton();
		BudgetMensuelPage page  = (BudgetMensuelPage)editer.getParent().getParent().getParent().getParent().getParent();

		boolean budgetActif = getUserSession().getBudgetCourant().isActif();

		// Confirmation
		setPopupModale(new ConfirmDialog((budgetActif ? "Clôture" : "Ouverture") + " du budget mensuel", 
				"Voulez vous "+(budgetActif ? "cloturer" : "réouvrir")+" le budget mensuel ?", "Oui", "Non", 
				ok -> {
					if(ok){
						try {
							page.getControleur().lockBudget(!budgetActif);
						} catch (DataNotFoundException e) {
							Notification.show("Erreur lors de la cloture du budget. Veuillez réessayer ultérieurement", Notification.Type.ERROR_MESSAGE);
						}
					}
				}
				));
	}

}

