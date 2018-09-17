package com.terrier.finances.gestion.services.utilisateurs.api;

import java.time.LocalDateTime;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import com.terrier.finances.gestion.communs.api.security.JwtConfig;
import com.terrier.finances.gestion.communs.utilisateur.enums.UtilisateurPrefsEnum;
import com.terrier.finances.gestion.communs.utilisateur.model.api.AuthLoginAPIObject;
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
	 * Logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(UtilisateursAPIService.class);
	/**
	 * Validation login/mdp
	 * @param login login
	 * @param motPasseEnClair mdp
	 * @return si valide
	 */
	public String authenticate(String login, String motPasseEnClair){
		
		AuthLoginAPIObject auth = new AuthLoginAPIObject(login, motPasseEnClair);
		Response resopnse = callHTTPPost(BudgetApiUrlEnum.USERS_AUTHENTICATE_FULL, auth);
		String jwtHeader = resopnse.getHeaderString(JwtConfig.JWT_AUTH_HEADER);
		LOGGER.info("[API] Authentification : {}", jwtHeader);
		return jwtHeader;
	}
	
	
	/**
	 * Déconnexion d'un utilisateur
	 * @param idUtilisateur
	 */
	public void deconnexion(){
		callHTTPPost(BudgetApiUrlEnum.USERS_DISCONNECT_FULL, null, null);
	}
	
	
	/**
	 * Déconnexion d'un utilisateur
	 * @param idUtilisateur
	 */
	public LocalDateTime getLastAccessTime(){
		UtilisateurPrefsAPIObject prefs = callHTTPGetData(BudgetApiUrlEnum.USERS_ACCESS_DATE_FULL, UtilisateurPrefsAPIObject.class);
		if(prefs != null){
			return BudgetDateTimeUtils.getLocalDateTimeFromLong(prefs.getLastAccessTime());
		}
		return null;
	}
	
	
	/**
	 * Déconnexion d'un utilisateur
	 * @param idUtilisateur
	 */
	public Map<UtilisateurPrefsEnum, String> getPreferencesUtilisateur(){
		UtilisateurPrefsAPIObject prefs = callHTTPGetData(BudgetApiUrlEnum.USERS_PREFS_FULL, UtilisateurPrefsAPIObject.class);
		if(prefs != null){
			return prefs.getPreferences();
		}
		return null;
	}
}
