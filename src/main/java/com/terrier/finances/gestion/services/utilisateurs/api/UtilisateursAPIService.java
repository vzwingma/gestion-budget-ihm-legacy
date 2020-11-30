package com.terrier.finances.gestion.services.utilisateurs.api;

import com.terrier.finances.gestion.communs.api.config.ApiUrlConfigEnum;
import com.terrier.finances.gestion.communs.utilisateur.model.api.UtilisateurPrefsAPIObject;
import com.terrier.finances.gestion.communs.utils.data.BudgetApiUrlEnum;
import com.terrier.finances.gestion.communs.utils.data.BudgetDateTimeUtils;
import com.terrier.finances.gestion.communs.utils.exceptions.DataNotFoundException;
import com.terrier.finances.gestion.services.abstrait.api.AbstractAPIClient;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;


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
	 * Connexion de l'utilisateur
	 * @throws DataNotFoundException  erreur lors de l'appel
	 */
	public LocalDateTime getLastAccessTime() throws DataNotFoundException {
		UtilisateurPrefsAPIObject prefs = callHTTPGetData(BudgetApiUrlEnum.USERS_ACCESS_DATE_FULL);
		if(prefs != null){
			return BudgetDateTimeUtils.getLocalDateTimeFromSecond(prefs.getLastAccessTime());
		}
		return null;
	}


	/**
	 * Retourne les préférences de l'utilisateur
	 * @return retourne les préférences utilisateurs
	 * @throws DataNotFoundException  erreur lors de l'appel
	 */
	public UtilisateurPrefsAPIObject getPreferenceDroits() throws DataNotFoundException{
		UtilisateurPrefsAPIObject prefs = callHTTPGetData(BudgetApiUrlEnum.USERS_PREFS_FULL);
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
