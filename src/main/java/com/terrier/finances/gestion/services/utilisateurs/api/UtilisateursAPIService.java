package com.terrier.finances.gestion.services.utilisateurs.api;

import java.time.LocalDateTime;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.reactive.function.client.ClientResponse;

import com.terrier.finances.gestion.communs.api.config.ApiUrlConfigEnum;
import com.terrier.finances.gestion.communs.api.security.JwtConfigEnum;
import com.terrier.finances.gestion.communs.utilisateur.enums.UtilisateurPrefsEnum;
import com.terrier.finances.gestion.communs.utilisateur.model.api.AuthLoginAPIObject;
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
	 * Logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(UtilisateursAPIService.class);
	
	
	/**
	 * 
	 */
	public UtilisateursAPIService() {
		super(UtilisateurPrefsAPIObject.class);
	}

	
	/**
	 * Validation login/mdp
	 * @param login login
	 * @param motPasseEnClair mdp
	 * @return si valide
	 * @throws DataNotFoundException  erreur lors de l'appel
	 */
	public String authenticate(String login, String motPasseEnClair) throws DataNotFoundException{

		AuthLoginAPIObject auth = new AuthLoginAPIObject(login, motPasseEnClair);
		String jwtHeader  = null;
		try {
			ClientResponse response = callHTTPPostResponse(BudgetApiUrlEnum.USERS_AUTHENTICATE_FULL, auth);
			if(response != null) {
				jwtHeader = response.headers().header(JwtConfigEnum.JWT_HEADER_AUTH).stream().findFirst().orElse(null);
				LOGGER.debug("Authentification : {}", jwtHeader);
			}
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
	@Deprecated
	public boolean deconnexion() {
		try {
			return callHTTPPostResponse(BudgetApiUrlEnum.USERS_DISCONNECT_FULL, null).statusCode().is2xxSuccessful();
		} catch (UserNotAuthorizedException | DataNotFoundException e) {
			LOGGER.trace("Ne peut pas arriver pour cette API");
			return false;
		}
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
			return BudgetDateTimeUtils.getLocalDateTimeFromLong(prefs.getLastAccessTime());
		}
		return null;
	}


	/**
	 * Retourne les préférences de l'utilisateur
	 * @return retourne les préférences utilisateurs
	 * @throws UserNotAuthorizedException  erreur d'authentification
	 * @throws DataNotFoundException  erreur lors de l'appel
	 */
	public Map<UtilisateurPrefsEnum, String> getPreferencesUtilisateur() throws UserNotAuthorizedException, DataNotFoundException{
		UtilisateurPrefsAPIObject prefs = callHTTPGetData(BudgetApiUrlEnum.USERS_PREFS_FULL).block();
		if(prefs != null){
			return prefs.getPreferences();
		}
		return null;
	}
	
	
	@Override
	public ApiUrlConfigEnum getConfigServiceURI() {
		return ApiUrlConfigEnum.APP_CONFIG_URL_UTILISATEURS;
	}
}
