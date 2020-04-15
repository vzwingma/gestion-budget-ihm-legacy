package com.terrier.finances.gestion.services.operations.api;

import java.util.Set;

import com.terrier.finances.gestion.communs.utils.exceptions.DataNotFoundException;
import com.terrier.finances.gestion.communs.utils.exceptions.UserNotAuthorizedException;
import com.terrier.finances.gestion.services.abstrait.api.IAPIClient;

/**
 * API  vers le domaine Budget
 * @author vzwingma
 *
 */

public interface ILibellesOperationsAPIService extends IAPIClient {


	/**
	 * Retourne l'ensemble des libelles des opérations pour un compte
	 * @param idCompte compte de l'utilisateur
	 * @param annee année du compte
	 * @return le set des libelles des opérations
	 * @throws UserNotAuthorizedException  erreur d'authentification
	 * @throws DataNotFoundException  erreur lors de l'appel
	 */
	public Set<String> getForAutocomplete(String idCompte, int annee) throws UserNotAuthorizedException, DataNotFoundException;
}
