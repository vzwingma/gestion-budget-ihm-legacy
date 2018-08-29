/**
 * 
 */
package com.terrier.finances.gestion.ui.communs.abstrait.listeners;

import com.terrier.finances.gestion.ui.communs.abstrait.ui.AbstractUIController;
import com.terrier.finances.gestion.ui.communs.abstrait.ui.IUIControleurService;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Window;

/**
 * Controleur de listeners
 * @author vzwingma
 *
 */
public abstract class AbstractComponentListener implements IUIControleurService {

	
	
	/**
	 * Controleur
	 * @param classNameControleur
	 * @return controleur correspondant
	 */
	public <C extends AbstractUIController<? extends CustomComponent>> C getControleur(Class<C> classNameControleur) {
		return getUserSession().getControleur(classNameControleur);
	}
	
	
	/**
	 * Set popup modale
	 * @param popupModale enregistre la popup
	 */
	@Override
	public void setPopupModale(Window popupModale){
		getUserSession().setPopupModale(popupModale);
	}
}
