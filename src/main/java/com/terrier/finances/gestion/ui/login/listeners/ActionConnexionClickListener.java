package com.terrier.finances.gestion.ui.login.listeners;

import com.terrier.finances.gestion.ui.communs.abstrait.listeners.AbstractActionUtilisateurListener;
import com.terrier.finances.gestion.ui.login.ui.LoginController;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

/**
 * Click sur connexion
 * @author vzwingma
 *
 */
public class ActionConnexionClickListener extends AbstractActionUtilisateurListener implements ClickListener {


	/**
	 * 
	 */
	private static final long serialVersionUID = 3264919051225309981L;
	private LoginController controler;

	public ActionConnexionClickListener(LoginController controler) {
		this.controler = controler;
	}

	/* (non-Javadoc)
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void boutonClick(ClickEvent event) {
		this.controler.authenticateUser(
				this.controler.getComponent().getTextLogin().getValue(), 
				this.controler.getComponent().getPasswordField().getValue());
	}

}
