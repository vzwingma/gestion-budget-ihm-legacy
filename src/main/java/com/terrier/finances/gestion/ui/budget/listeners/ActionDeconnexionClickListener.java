/**
 * 
 */
package com.terrier.finances.gestion.ui.budget.listeners;

import com.terrier.finances.gestion.ui.budget.ui.BudgetMensuelPage;
import com.terrier.finances.gestion.ui.communs.ConfirmDialog;
import com.terrier.finances.gestion.ui.communs.abstrait.listeners.AbstractActionUtilisateurListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

/**
 * Déconnexion de la page
 * @author vzwingma
 *
 */
public class ActionDeconnexionClickListener extends AbstractActionUtilisateurListener implements ClickListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1823872638217135776L;

	
	/* (non-Javadoc)
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void boutonClick(ClickEvent event) {
		Button editer = event.getButton();
		BudgetMensuelPage page  = (BudgetMensuelPage)editer.getParent().getParent().getParent().getParent();

		// Confirmation
		setPopupModale(new ConfirmDialog("Déconnexion de l'application", 
				"Voulez vous vous déconnecter ?", "Oui", "Non", 
				ok -> {
					if(ok){
						page.getControleur().deconnexion();
					}
				}));
	}
}

