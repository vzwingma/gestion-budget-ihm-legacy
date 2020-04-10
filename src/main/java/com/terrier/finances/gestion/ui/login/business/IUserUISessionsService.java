package com.terrier.finances.gestion.ui.login.business;

import com.terrier.finances.gestion.ui.login.model.UserUISession;

/**
 * Gestionnaire des UI par Session utilisateur
 * @author vzwingma
 *
 */

public interface IUserUISessionsService {


	/**
	 * @return la session utilisateur
	 */
	public UserUISession getSession();



	/**
	 * Déconnexion de l'utilisateur
	 */
	public void deconnexionUtilisateur(String idSession);

	/**
	 * @return le nombre de sessions actives soit utilisateur authentifié
	 */
	public long getNombreSessionsActives();

}
