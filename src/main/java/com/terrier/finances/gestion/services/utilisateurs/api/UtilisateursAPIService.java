package com.terrier.finances.gestion.services.utilisateurs.api;

import java.time.LocalDateTime;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import com.terrier.finances.gestion.communs.api.security.JwtConfigEnum;
import com.terrier.finances.gestion.communs.utilisateur.enums.UtilisateurPrefsEnum;
import com.terrier.finances.gestion.communs.utilisateur.model.api.AuthLoginAPIObject;
import com.terrier.finances.gestion.communs.utilisateur.model.api.UtilisateurPrefsAPIObject;
import com.terrier.finances.gestion.communs.utils.data.BudgetApiUrlEnum;
import com.terrier.finances.gestion.communs.utils.data.BudgetDateTimeUtils;
import com.terrier.finances.gestion.communs.utils.exceptions.UserNotAuthorizedException;
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
		String jwtHeader  = null;
		try {
			Response resopnse = callHTTPPost(BudgetApiUrlEnum.USERS_AUTHENTICATE_FULL, auth);
			jwtHeader = resopnse.getHeaderString(JwtConfigEnum.JWT_HEADER_AUTH);
			LOGGER.info("[SEC] Authentification : {}", jwtHeader);
		} catch (UserNotAuthorizedException e) {
			LOGGER.warn("Ne peut pas arriver pour cette API");
		}

		return jwtHeader;
	}


	/**
	 * Déconnexion d'un utilisateur
	 * @param idUtilisateur
	 * @throws UserNotAuthorizedException 
	 */
	public void deconnexion() {
		try {
			callHTTPPost(BudgetApiUrlEnum.USERS_DISCONNECT_FULL, null);
		} catch (UserNotAuthorizedException e) {
			LOGGER.trace("Ne peut pas arriver pour cette API");
		}
	}


	/**
	 * Déconnexion d'un utilisateur
	 * @param idUtilisateur
	 * @throws UserNotAuthorizedException 
	 */
	public LocalDateTime getLastAccessTime() throws UserNotAuthorizedException{
		UtilisateurPrefsAPIObject prefs = callHTTPGetData(BudgetApiUrlEnum.USERS_ACCESS_DATE_FULL, UtilisateurPrefsAPIObject.class);
		if(prefs != null){
			return BudgetDateTimeUtils.getLocalDateTimeFromLong(prefs.getLastAccessTime());
		}
		return null;
	}


	/**
	 * Déconnexion d'un utilisateur
	 * @param idUtilisateur
	 * @throws UserNotAuthorizedException 
	 */
	public Map<UtilisateurPrefsEnum, String> getPreferencesUtilisateur() throws UserNotAuthorizedException{
		UtilisateurPrefsAPIObject prefs = callHTTPGetData(BudgetApiUrlEnum.USERS_PREFS_FULL, UtilisateurPrefsAPIObject.class);
		if(prefs != null){
			return prefs.getPreferences();
		}
		return null;
	}
}
