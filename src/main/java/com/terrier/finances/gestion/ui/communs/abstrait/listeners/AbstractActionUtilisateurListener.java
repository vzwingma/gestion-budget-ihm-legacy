/**
 * 
 */
package com.terrier.finances.gestion.ui.communs.abstrait.listeners;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.terrier.finances.gestion.communs.utils.config.CorrelationsIdUtils;
import com.terrier.finances.gestion.ui.communs.abstrait.AbstractUIController;
import com.terrier.finances.gestion.ui.communs.abstrait.IUIControllerService;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Window;

/**
 * Controleur de listeners
 * @author vzwingma
 *
 */
public abstract class AbstractActionUtilisateurListener implements IUIControllerService, ClickListener {

	
	
	// Version UID
	private static final long serialVersionUID = -4479313296882504369L;
	/**
	 * Logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractActionUtilisateurListener.class);


	/**
	 * Controleur
	 * @param classNameControleur
	 * @return controleur correspondant
	 */
	public <C extends AbstractUIController<? extends CustomComponent>> C getControleur(Class<C> classNameControleur) {
		return getUserSession().getControleur(classNameControleur);
	}
	
	
	
	
	@Override
	public void buttonClick(ClickEvent event) {
		String actionCorrId = UUID.randomUUID().toString();
		CorrelationsIdUtils.putCorrIdOnMDC(actionCorrId);
		LOGGER.info("Action utilisateur");
		getUserSession().setActionUserCorrId(actionCorrId);
		boutonClick(event);
	}


	public abstract void boutonClick(ClickEvent event) ;


	/**
	 * Set popup modale
	 * @param popupModale enregistre la popup
	 */
	@Override
	public void setPopupModale(Window popupModale){
		getUserSession().setPopupModale(popupModale);
	}
}
