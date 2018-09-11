package com.terrier.finances.gestion.services.utilisateurs.api;

import java.time.LocalDateTime;
import java.util.Map;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;

import org.springframework.stereotype.Controller;

import com.terrier.finances.gestion.communs.utilisateur.enums.UtilisateurPrefsEnum;
import com.terrier.finances.gestion.communs.utilisateur.model.api.AuthLoginAPIObject;
import com.terrier.finances.gestion.communs.utilisateur.model.api.AuthResponseAPIObject;
import com.terrier.finances.gestion.communs.utilisateur.model.api.UtilisateurPrefsAPIObject;
import com.terrier.finances.gestion.communs.utils.data.BudgetApiUrlEnum;
import com.terrier.finances.gestion.communs.utils.data.DataUtils;
import com.terrier.finances.gestion.ui.communs.abstrait.api.AbstractHTTPClient;

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
		
		Entity<AuthLoginAPIObject> auth = Entity.entity(new AuthLoginAPIObject(login, motPasseEnClair), MediaType.APPLICATION_JSON_TYPE);
		return callHTTPPost(URI, BudgetApiUrlEnum.USERS_AUTHENTICATE_FULL, auth, AuthResponseAPIObject.class);
	}
	
	
	/**
	 * Déconnexion d'un utilisateur
	 * @param idUtilisateur
	 */
	public void deconnexion(String idUtilisateur){
		callHTTPPost(URI, BudgetApiUrlEnum.USERS_DISCONNECT_FULL + "/" + idUtilisateur, null, null);
	}
	
	
	/**
	 * Déconnexion d'un utilisateur
	 * @param idUtilisateur
	 */
	public LocalDateTime getLastAccessTime(String idUtilisateur){
		UtilisateurPrefsAPIObject prefs = callHTTPGetData(URI, BudgetApiUrlEnum.USERS_ACCESS_DATE_FULL + "/" + idUtilisateur, UtilisateurPrefsAPIObject.class);
		if(prefs != null){
			return DataUtils.getLocalDateTimeFromLong(prefs.getLastAccessTime());
		}
		return null;
	}
	
	
	/**
	 * Déconnexion d'un utilisateur
	 * @param idUtilisateur
	 */
	public Map<UtilisateurPrefsEnum, String> getPreferencesUtilisateur(String idUtilisateur){
		UtilisateurPrefsAPIObject prefs = callHTTPGetData(URI, BudgetApiUrlEnum.USERS_PREFS_FULL + "/" + idUtilisateur, UtilisateurPrefsAPIObject.class);
		if(prefs != null){
			return prefs.getPreferences();
		}
		return null;
	}
}
