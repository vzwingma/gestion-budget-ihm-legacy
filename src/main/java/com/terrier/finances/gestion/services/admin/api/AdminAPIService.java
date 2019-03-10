package com.terrier.finances.gestion.services.admin.api;

import org.springframework.stereotype.Controller;

import com.terrier.finances.gestion.communs.admin.model.StatutDependencyAPIObject;
import com.terrier.finances.gestion.communs.utils.data.BudgetApiUrlEnum;
import com.terrier.finances.gestion.communs.utils.exceptions.DataNotFoundException;
import com.terrier.finances.gestion.communs.utils.exceptions.UserNotAuthorizedException;
import com.terrier.finances.gestion.services.abstrait.api.AbstractHTTPClient;

/**
 * Service API vers {@link AdminService}
 * @author vzwingma
 *
 */
@Controller
public class AdminAPIService extends AbstractHTTPClient {
	
	/**
	 * Comptes d'un utilisateur
	 * @param idUtilisateur
	 * @throws UserNotAuthorizedException  erreur d'authentification
	 * @throws DataNotFoundException  erreur lors de l'appel
	 */
	public StatutDependencyAPIObject getStatut() throws DataNotFoundException, UserNotAuthorizedException {
		return callHTTPGetData(BudgetApiUrlEnum.ADMIN_STATUT_FULL, StatutDependencyAPIObject.class);
	}
	
}
