package com.terrier.finances.gestion.services.comptes.api;

import java.util.List;

import com.terrier.finances.gestion.communs.comptes.model.v12.CompteBancaire;
import com.terrier.finances.gestion.communs.utils.exceptions.DataNotFoundException;
import com.terrier.finances.gestion.communs.utils.exceptions.UserNotAuthorizedException;
import com.terrier.finances.gestion.services.abstrait.api.IAPIClient;

/**
 * Service API vers {@link ComptesService}
 * @author vzwingma
 *
 */
public interface IComptesAPIService extends IAPIClient{
	
		/**
	 * Comptes d'un utilisateur
	 * @param idUtilisateur
	 * @throws UserNotAuthorizedException  erreur d'authentification
	 * @throws DataNotFoundException  erreur lors de l'appel
	 */
	public List<CompteBancaire> getComptes() throws DataNotFoundException, UserNotAuthorizedException;
	
	/**
	 * Compte d'un utilisateur
	 * @param idCompte id du compte
	 * @throws UserNotAuthorizedException  erreur d'authentification
	 * @throws DataNotFoundException  erreur lors de l'appel
	 */
	public CompteBancaire getCompte(String idCompte) throws UserNotAuthorizedException, DataNotFoundException;
}
