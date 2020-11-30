package com.terrier.finances.gestion.services.operations.api;

import com.terrier.finances.gestion.communs.api.config.ApiUrlConfigEnum;
import com.terrier.finances.gestion.communs.operations.model.api.LibellesOperationsAPIObject;
import com.terrier.finances.gestion.communs.utils.data.BudgetApiUrlEnum;
import com.terrier.finances.gestion.communs.utils.exceptions.DataNotFoundException;
import com.terrier.finances.gestion.services.abstrait.api.AbstractAPIClient;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * API  vers le domaine Budget
 * @author vzwingma
 *
 */
@Controller
public class LibellesOperationsAPIService extends AbstractAPIClient<LibellesOperationsAPIObject> implements ILibellesOperationsAPIService{

	public LibellesOperationsAPIService() {
		super(LibellesOperationsAPIObject.class);
	}

	/**
	 * Retourne l'ensemble des libelles des opérations pour un compte
	 * @param idCompte compte de l'utilisateur
	 * @param annee année du compte
	 * @return le set des libelles des opérations
	 * @throws DataNotFoundException  erreur lors de l'appel
	 */
	public Set<String> getForAutocomplete(String idCompte, int annee) throws DataNotFoundException{

		Map<String, String> pathParams = new HashMap<>();
		pathParams.put(BudgetApiUrlEnum.PARAM_ID_COMPTE, idCompte);

		Map<String, String> queryParams = new HashMap<>();
		queryParams.put("annee", Integer.toString(annee));
		return callHTTPGetData(BudgetApiUrlEnum.BUDGET_COMPTE_OPERATIONS_LIBELLES_FULL, pathParams, queryParams).getLibellesOperations();
	}

	@Override
	public ApiUrlConfigEnum getConfigServiceURI() {
		return ApiUrlConfigEnum.APP_CONFIG_URL_OPERATIONS;
	}
}
