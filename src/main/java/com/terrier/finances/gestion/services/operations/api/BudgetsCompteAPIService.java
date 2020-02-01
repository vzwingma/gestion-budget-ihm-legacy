package com.terrier.finances.gestion.services.operations.api;

import org.springframework.stereotype.Controller;

import com.terrier.finances.gestion.communs.api.config.ApiUrlConfigEnum;
import com.terrier.finances.gestion.communs.comptes.model.api.IntervallesCompteAPIObject;
import com.terrier.finances.gestion.communs.utils.data.BudgetApiUrlEnum;
import com.terrier.finances.gestion.communs.utils.exceptions.DataNotFoundException;
import com.terrier.finances.gestion.communs.utils.exceptions.UserNotAuthorizedException;
import com.terrier.finances.gestion.services.abstrait.api.AbstractHTTPClient;

/**
 * API  vers le domaine Budget
 * @author vzwingma
 *
 */
@Controller
public class BudgetsCompteAPIService extends AbstractHTTPClient<IntervallesCompteAPIObject> {
	
	/**
	 * Charge l'intervalle des budgets pour ce compte pour cet utilisateur
	 * @param utilisateur utilisateur
	 * @param compte id du compte
	 * @return la date du premier budget décrit pour cet utilisateur
	 * @throws UserNotAuthorizedException 
	 */
	public IntervallesCompteAPIObject getIntervallesBudgets(String compte) throws UserNotAuthorizedException, DataNotFoundException{
		String path = BudgetApiUrlEnum.BUDGET_COMPTE_INTERVALLES_FULL.replace(BudgetApiUrlEnum.URL_PARAM_ID_COMPTE, compte);
		return callHTTPGetData(path).block();
	}


	@Override
	public ApiUrlConfigEnum getConfigServiceURI() {
		return ApiUrlConfigEnum.APP_CONFIG_URL_OPERATIONS;
	}
}
