/**
 * 
 */
package com.terrier.finances.gestion.ui.operations.creation.listeners;

import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.terrier.finances.gestion.ui.communs.abstrait.listeners.AbstractComponentListener;
import com.terrier.finances.gestion.ui.operations.creation.ui.CreerDepenseForm;
import com.vaadin.ui.Window;

/**
 * Bouton de création d'une nouvelle dépense
 * @author vzwingma
 *
 */
public class ActionCreerDepenseClickListener extends AbstractComponentListener implements ClickListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1823872638217135776L;

	
	
	/* (non-Javadoc)
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		Window sub = new Window("Créer une nouvelle opération");
		sub.setWidth("600px");
		sub.setHeight("350px");
		sub.setContent(new CreerDepenseForm());
		sub.setModal(true);
		sub.setResizable(false);
		setPopupModale(sub);
	}
}

