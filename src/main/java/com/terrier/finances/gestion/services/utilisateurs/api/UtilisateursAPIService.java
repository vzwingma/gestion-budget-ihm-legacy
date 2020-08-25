package com.terrier.finances.gestion.services.utilisateurs.api;

import java.time.LocalDateTime;

import org.springframework.stereotype.Controller;

import com.terrier.finances.gestion.communs.api.config.ApiUrlConfigEnum;
import com.terrier.finances.gestion.communs.utilisateur.model.api.UtilisateurPrefsAPIObject;
import com.terrier.finances.gestion.communs.utils.data.BudgetApiUrlEnum;
import com.terrier.finances.gestion.communs.utils.data.BudgetDateTimeUtils;
import com.terrier.finances.gestion.communs.utils.exceptions.DataNotFoundException;
import com.terrier.finances.gestion.communs.utils.exceptions.UserNotAuthorizedException;
import com.terrier.finances.gestion.services.abstrait.api.AbstractAPIClient;


/**
 * Implémentation du service API Utilisateurs
 * @author vzwingma
 *
 */
@Controller
public class UtilisateursAPIService extends AbstractAPIClient<UtilisateurPrefsAPIObject> implements IUtilisateursAPIService {

	
	/**
	 * 
	 */
	public UtilisateursAPIService() {
		super(UtilisateurPrefsAPIObject.class);
	}


	/**
	 * Déconnexion d'un utilisateur
	 * @param idUtilisateur
	 * @throws UserNotAuthorizedException  erreur d'authentification
	 * @throws DataNotFoundException  erreur lors de l'appel
	 */
	public LocalDateTime getLastAccessTime() throws UserNotAuthorizedException, DataNotFoundException{
		UtilisateurPrefsAPIObject prefs = callHTTPGetData(BudgetApiUrlEnum.USERS_ACCESS_DATE_FULL).block();
		if(prefs != null){
			return BudgetDateTimeUtils.getLocalDateTimeFromSecond(prefs.getLastAccessTime());
		}
		return null;
	}


	/**
	 * Retourne les préférences de l'utilisateur
	 * @return retourne les préférences utilisateurs
	 * @throws UserNotAuthorizedException  erreur d'authentification
	 * @throws DataNotFoundException  erreur lors de l'appel
	 */
	public UtilisateurPrefsAPIObject getPreferenceDroits() throws UserNotAuthorizedException, DataNotFoundException{
		UtilisateurPrefsAPIObject prefs = callHTTPGetData(BudgetApiUrlEnum.USERS_PREFS_FULL).block();
		if(prefs != null){
			return prefs;
		}
		return null;
	}
	
	
	@Override
	public ApiUrlConfigEnum getConfigServiceURI() {
		return ApiUrlConfigEnum.APP_CONFIG_URL_UTILISATEURS;
	}
}
