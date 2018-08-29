/**
 * 
 */
package com.terrier.finances.gestion.ui.communs.abstrait.ui;

import com.terrier.finances.gestion.communs.budget.model.BudgetMensuel;
import com.terrier.finances.gestion.communs.utilisateur.model.Utilisateur;
import com.terrier.finances.gestion.services.budget.business.OperationsService;
import com.terrier.finances.gestion.services.parametrages.business.ParametragesService;
import com.terrier.finances.gestion.services.utilisateurs.business.AuthenticationService;
import com.terrier.finances.gestion.ui.communs.services.FacadeServices;
import com.terrier.finances.gestion.ui.login.business.UserSession;
import com.terrier.finances.gestion.ui.login.business.UserSessionsService;
import com.vaadin.ui.Window;

/**
 * Méthodes génériques à tous les controleurs UI
 * @author vzwingma
 *
 */
public interface IUIService  {


	/**
	 * Set popup modale
	 * @param popupModale enregistre la popup
	 */
	public default void setPopupModale(Window popupModale){
		getUserSession().setPopupModale(popupModale);
	}


	/**
	 * @return la session de l'UI
	 */
	public default UserSession getUserSession(){
		return getServiceUserSessions().getSession();
	}

	/**
	 * @return service métier dépense
	 */
	public default OperationsService getServiceOperations(){
		return FacadeServices.get().getServiceOperations();
	}
	
	/**
	 * @return service paramétrage
	 */
	public default ParametragesService getServiceParams(){
		return FacadeServices.get().getServiceParams();
	}
	/**
	 * @return service User
	 */
	default UserSessionsService getServiceUserSessions(){
		return FacadeServices.get().getServiceUserSessions();
	}
	/**
	 * @return service auth
	 */
	public default AuthenticationService getServiceAuthentification(){
		return FacadeServices.get().getServiceAuth();
	}
}
