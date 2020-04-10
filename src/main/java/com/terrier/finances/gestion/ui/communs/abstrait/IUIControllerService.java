/**
 * 
 */
package com.terrier.finances.gestion.ui.communs.abstrait;

import com.terrier.finances.gestion.services.FacadeServices;
import com.terrier.finances.gestion.services.admin.api.IAdminAPIService;
import com.terrier.finances.gestion.services.comptes.api.IComptesAPIService;
import com.terrier.finances.gestion.services.operations.api.IBudgetsCompteAPIService;
import com.terrier.finances.gestion.services.operations.api.ILibellesOperationsAPIService;
import com.terrier.finances.gestion.services.operations.api.IOperationsAPIService;
import com.terrier.finances.gestion.services.parametrages.api.IParametragesAPIService;
import com.terrier.finances.gestion.services.utilisateurs.api.IUtilisateursAPIService;
import com.terrier.finances.gestion.ui.login.business.IUserUISessionsService;
import com.terrier.finances.gestion.ui.login.model.UserUISession;
import com.vaadin.ui.Window;

/**
 * Méthodes génériques à tous les controleurs UI
 * @author vzwingma
 *
 */
public interface IUIControllerService  {


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
	public default UserUISession getUserSession(){
		return getServiceUserSessions().getSession();
	}

	/**
	 * @return service métier dépense
	 */
	public default IOperationsAPIService getServiceOperations(){
		return FacadeServices.get().getServiceOperations();
	}
	
	/**
	 * @return service métier budget comptes
	 */
	public default IBudgetsCompteAPIService getServiceBudgetsCompte(){
		return FacadeServices.get().getServiceBudgetsCompte();
	}
	
	
	/**
	 * @return service métier dépense
	 */
	public default ILibellesOperationsAPIService getServiceLibellesOperations(){
		return FacadeServices.get().getServiceLibellesOperations();
	}
	
	/**
	 * @return service paramétrage
	 */
	public default IParametragesAPIService getServiceParams(){
		return FacadeServices.get().getServiceParams();
	}
	/**
	 * @return service User
	 */
	default IUserUISessionsService getServiceUserSessions(){
		return FacadeServices.get().getServiceUserSessions();
	}

	/**
	 * @return service Utilisateurs
	 */
	public default IUtilisateursAPIService getServiceUtilisateurs(){
		return FacadeServices.get().getServiceUtilisateurs();
	}
	
	/**
	 * @return service Comptes
	 */
	public default IComptesAPIService getServiceComptes(){
		return FacadeServices.get().getServiceComptes();
	}
	
	/**
	 * @return service Admin
	 */
	public default IAdminAPIService getServiceAdmin(){
		return FacadeServices.get().getServiceAdmin();
	}
}
