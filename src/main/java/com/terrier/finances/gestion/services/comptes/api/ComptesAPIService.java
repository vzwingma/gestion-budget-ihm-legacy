package com.terrier.finances.gestion.services.comptes.api;

import com.terrier.finances.gestion.communs.api.config.ApiUrlConfigEnum;
import com.terrier.finances.gestion.communs.comptes.model.v12.CompteBancaire;
import com.terrier.finances.gestion.communs.utils.data.BudgetApiUrlEnum;
import com.terrier.finances.gestion.communs.utils.exceptions.DataNotFoundException;
import com.terrier.finances.gestion.services.abstrait.api.AbstractAPIClient;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Service API vers {@link ComptesService}
 * @author vzwingma
 *
 */
@Controller
public class ComptesAPIService extends AbstractAPIClient<CompteBancaire> implements IComptesAPIService {
	
	
	public ComptesAPIService() {
		super(CompteBancaire.class);
	}
	/**
	 * Comptes d'un utilisateur
	 */
	public List<CompteBancaire> getComptes() throws DataNotFoundException {
		return callHTTPGetListData(BudgetApiUrlEnum.COMPTES_LIST_FULL);
	}
	
	/**
	 * Compte d'un utilisateur
	 * @param idCompte id du compte
	 * @throws DataNotFoundException  erreur lors de l'appel
	 */
	public CompteBancaire getCompte(String idCompte) throws DataNotFoundException{
		Map<String, String> pathParams = new HashMap<>();
		pathParams.put(BudgetApiUrlEnum.PARAM_ID_COMPTE, idCompte);

		return callHTTPGetData(BudgetApiUrlEnum.COMPTES_ID_FULL, pathParams, null);
	}

	

	@Override
	public ApiUrlConfigEnum getConfigServiceURI() {
		return ApiUrlConfigEnum.APP_CONFIG_URL_COMPTES;
	}
}
