package com.terrier.finances.gestion.services.comptes.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Controller;

import com.terrier.finances.gestion.communs.comptes.model.CompteBancaire;
import com.terrier.finances.gestion.communs.operations.model.api.LibellesOperationsAPIObject;
import com.terrier.finances.gestion.communs.utils.data.BudgetApiUrlEnum;
import com.terrier.finances.gestion.communs.utils.exceptions.DataNotFoundException;
import com.terrier.finances.gestion.communs.utils.exceptions.UserNotAuthorizedException;
import com.terrier.finances.gestion.services.abstrait.api.AbstractHTTPClient;
import com.terrier.finances.gestion.ui.communs.config.AppConfigEnum;

/**
 * Service API vers {@link ComptesService}
 * @author vzwingma
 *
 */
@Controller
public class ComptesAPIService extends AbstractHTTPClient {
	
	/**
	 * Comptes d'un utilisateur
	 * @param idUtilisateur
	 * @throws UserNotAuthorizedException  erreur d'authentification
	 * @throws DataNotFoundException  erreur lors de l'appel
	 */
	public List<CompteBancaire> getComptes() throws DataNotFoundException, UserNotAuthorizedException {
		return callHTTPGetListData(BudgetApiUrlEnum.COMPTES_LIST_FULL);
	}
	
	/**
	 * Compte d'un utilisateur
	 * @param idCompte id du compte
	 * @throws UserNotAuthorizedException  erreur d'authentification
	 * @throws DataNotFoundException  erreur lors de l'appel
	 */
	public CompteBancaire getCompte(String idCompte) throws UserNotAuthorizedException, DataNotFoundException{
		String path = BudgetApiUrlEnum.COMPTES_ID_FULL.replace(BudgetApiUrlEnum.URL_PARAM_ID_COMPTE, idCompte);
		return callHTTPGetData(path, CompteBancaire.class);
	}

	
	/**
	 * Retourne l'ensemble des libelles des opérations pour un compte
	 * @param idCompte compte de l'utilisateur
	 * @param idUtilisateur utilisateur
	 * @return le set des libelles des opérations
	 * @throws UserNotAuthorizedException  erreur d'authentification
	 * @throws DataNotFoundException  erreur lors de l'appel
	 */
	public Set<String> getLibellesOperationsForAutocomplete(String idCompte, int annee) throws UserNotAuthorizedException, DataNotFoundException{
		String path = BudgetApiUrlEnum.COMPTES_OPERATIONS_LIBELLES_FULL.replace(BudgetApiUrlEnum.URL_PARAM_ID_COMPTE, idCompte);
		Map<String, String> params = new HashMap<>();
		params.put("annee", Integer.toString(annee));
		LibellesOperationsAPIObject libelles = callHTTPGetData(path, params, LibellesOperationsAPIObject.class);
		return libelles.getLibellesOperations();
	}

	@Override
	public AppConfigEnum getConfigServiceURI() {
		return AppConfigEnum.APP_CONFIG_URL_COMPTES;
	}
}
