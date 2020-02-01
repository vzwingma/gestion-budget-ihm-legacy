package com.terrier.finances.gestion.services.operations.api;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Controller;

import com.terrier.finances.gestion.communs.api.config.ApiUrlConfigEnum;
import com.terrier.finances.gestion.communs.operations.model.api.LibellesOperationsAPIObject;
import com.terrier.finances.gestion.communs.utils.data.BudgetApiUrlEnum;
import com.terrier.finances.gestion.communs.utils.exceptions.DataNotFoundException;
import com.terrier.finances.gestion.communs.utils.exceptions.UserNotAuthorizedException;
import com.terrier.finances.gestion.services.abstrait.api.AbstractAPIClient;

/**
 * API  vers le domaine Budget
 * @author vzwingma
 *
 */
@Controller
public class LibellesOperationsAPIService extends AbstractAPIClient<LibellesOperationsAPIObject> {


	/**
	 * Retourne l'ensemble des libelles des opérations pour un compte
	 * @param idCompte compte de l'utilisateur
	 * @param annee année du compte
	 * @return le set des libelles des opérations
	 * @throws UserNotAuthorizedException  erreur d'authentification
	 * @throws DataNotFoundException  erreur lors de l'appel
	 */
	public Set<String> getForAutocomplete(String idCompte, int annee) throws UserNotAuthorizedException, DataNotFoundException{
		String path = BudgetApiUrlEnum.BUDGET_COMPTE_OPERATIONS_LIBELLES_FULL.replace(BudgetApiUrlEnum.URL_PARAM_ID_COMPTE, idCompte);
		Map<String, String> params = new HashMap<>();
		params.put("annee", Integer.toString(annee));
		return callHTTPGetData(path, params).block().getLibellesOperations();
	}

	@Override
	public ApiUrlConfigEnum getConfigServiceURI() {
		return ApiUrlConfigEnum.APP_CONFIG_URL_OPERATIONS;
	}
}
