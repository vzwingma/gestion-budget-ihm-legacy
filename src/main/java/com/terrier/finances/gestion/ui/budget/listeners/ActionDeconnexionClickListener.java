/**
 * 
 */
package com.terrier.finances.gestion.ui.budget.listeners;

import com.terrier.finances.gestion.ui.budget.ui.BudgetMensuelPage;
import com.terrier.finances.gestion.ui.communs.ConfirmDialog;
import com.terrier.finances.gestion.ui.communs.ConfirmDialog.ConfirmationDialogCallback;
import com.terrier.finances.gestion.ui.communs.abstrait.listeners.AbstractComponentListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

/**
 * Déconnexion de la page
 * @author vzwingma
 *
 */
public class ActionDeconnexionClickListener extends AbstractComponentListener implements ClickListener, ConfirmationDialogCallback {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1823872638217135776L;

	private BudgetMensuelPage page;
	
	/* (non-Javadoc)
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		Button editer = event.getButton();
		page  = (BudgetMensuelPage)editer.getParent().getParent().getParent().getParent();

		// Confirmation
		ConfirmDialog confirm = new ConfirmDialog("Déconnexion de l'application", 
				"Voulez vous vous déconnecter ?", "Oui", "Non", this);
		confirm.setWidth("400px");
		confirm.setHeight("150px");
		setPopupModale(confirm);
	}



	@Override
	public void response(boolean ok) {
		if(ok){
			page.getControleur().deconnexion();
		}
	}
}

