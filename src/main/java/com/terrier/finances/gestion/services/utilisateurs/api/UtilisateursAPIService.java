package com.terrier.finances.gestion.services.utilisateurs.api;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.stereotype.Controller;

import com.terrier.finances.gestion.communs.utilisateur.enums.UtilisateurPrefsEnum;
import com.terrier.finances.gestion.communs.utilisateur.model.api.AuthLoginAPIObject;
import com.terrier.finances.gestion.communs.utilisateur.model.api.AuthResponseAPIObject;
import com.terrier.finances.gestion.communs.utilisateur.model.api.UtilisateurPrefsAPIObject;
import com.terrier.finances.gestion.communs.utils.data.BudgetApiUrlEnum;
import com.terrier.finances.gestion.communs.utils.data.BudgetDateTimeUtils;
import com.terrier.finances.gestion.services.abstrait.api.AbstractHTTPClient;

/**
 * Service API vers {@link UtilisateursService}
 * @author vzwingma
 *
 */
@Controller
public class UtilisateursAPIService extends AbstractHTTPClient {
	
	/**
	 * Validation login/mdp
	 * @param login login
	 * @param motPasseEnClair mdp
	 * @return si valide
	 */
	public AuthResponseAPIObject authenticate(String login, String motPasseEnClair){
		
		AuthLoginAPIObject auth = new AuthLoginAPIObject(login, motPasseEnClair);
		return callHTTPPost(BudgetApiUrlEnum.USERS_AUTHENTICATE_FULL, auth, AuthResponseAPIObject.class);
	}
	
	
	/**
	 * Déconnexion d'un utilisateur
	 * @param idUtilisateur
	 */
	public void deconnexion(String idUtilisateur){
		String path = BudgetApiUrlEnum.USERS_DISCONNECT_FULL.replace("{idUtilisateur}", idUtilisateur);
		callHTTPPost(path, null, null);
	}
	
	
	/**
	 * Déconnexion d'un utilisateur
	 * @param idUtilisateur
	 */
	public LocalDateTime getLastAccessTime(String idUtilisateur){
		String path = BudgetApiUrlEnum.USERS_ACCESS_DATE_FULL.replace("{idUtilisateur}", idUtilisateur);
		UtilisateurPrefsAPIObject prefs = callHTTPGetData(path, UtilisateurPrefsAPIObject.class);
		if(prefs != null){
			return BudgetDateTimeUtils.getLocalDateTimeFromLong(prefs.getLastAccessTime());
		}
		return null;
	}
	
	
	/**
	 * Déconnexion d'un utilisateur
	 * @param idUtilisateur
	 */
	public Map<UtilisateurPrefsEnum, String> getPreferencesUtilisateur(String idUtilisateur){
		String path = BudgetApiUrlEnum.USERS_PREFS_FULL.replace("{idUtilisateur}", idUtilisateur);
		UtilisateurPrefsAPIObject prefs = callHTTPGetData(path, UtilisateurPrefsAPIObject.class);
		if(prefs != null){
			return prefs.getPreferences();
		}
		return null;
	}
}
