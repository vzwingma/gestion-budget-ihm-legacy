package com.terrier.finances.gestion.services.utilisateurs.api;

import java.time.LocalDateTime;

import com.terrier.finances.gestion.communs.utilisateur.model.api.UtilisateurPrefsAPIObject;
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
	public UtilisateurPrefsAPIObject getPreferenceDroits() throws UserNotAuthorizedException, DataNotFoundException;
}
