package com.terrier.finances.gestion.services.utilisateurs.api;

import java.time.LocalDateTime;
import java.util.Map;

import com.terrier.finances.gestion.communs.utilisateur.enums.UtilisateurPrefsEnum;
import com.terrier.finances.gestion.communs.utils.exceptions.DataNotFoundException;
import com.terrier.finances.gestion.communs.utils.exceptions.UserNotAuthorizedException;
import com.terrier.finances.gestion.services.abstrait.api.IAPIClient;

/**
 * Service API vers {@link UtilisateursService}
 * @author vzwingma
 *
 */
public interface IUtilisateursAPIService extends IAPIClient{

	
	/**
	 * Déconnexion d'un utilisateur
	 * @param idUtilisateur
	 * @throws UserNotAuthorizedException  erreur d'authentification
	 * @throws DataNotFoundException  erreur lors de l'appel
	 */
	public LocalDateTime getLastAccessTime() throws UserNotAuthorizedException, DataNotFoundException;
	

	/**
	 * Retourne les préférences de l'utilisateur
	 * @return retourne les préférences utilisateurs
	 * @throws UserNotAuthorizedException  erreur d'authentification
	 * @throws DataNotFoundException  erreur lors de l'appel
	 */
	public Map<UtilisateurPrefsEnum, String> getPreferencesUtilisateur() throws UserNotAuthorizedException, DataNotFoundException;
}
