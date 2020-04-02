package com.terrier.finances.gestion.services.operations.api;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;

import com.terrier.finances.gestion.communs.api.config.ApiUrlConfigEnum;
import com.terrier.finances.gestion.communs.comptes.model.api.IntervallesCompteAPIObject;
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
public class BudgetsCompteAPIService extends AbstractAPIClient<IntervallesCompteAPIObject> {
	
	
	public BudgetsCompteAPIService() {
		super(IntervallesCompteAPIObject.class);
	}
	
	/**
	 * Charge l'intervalle des budgets pour ce compte pour cet utilisateur
	 * @param utilisateur utilisateur
	 * @param compte id du compte
	 * @return la date du premier budget décrit pour cet utilisateur
	 * @throws UserNotAuthorizedException 
	 */
	public IntervallesCompteAPIObject getIntervallesBudgets(String compte) throws UserNotAuthorizedException, DataNotFoundException{
		
		Map<String, String> pathParams = new HashMap<>();
		pathParams.put(BudgetApiUrlEnum.PARAM_ID_COMPTE, compte);
		return callHTTPGetData(BudgetApiUrlEnum.BUDGET_COMPTE_INTERVALLES_FULL, pathParams, null).block();
	}


	@Override
	public ApiUrlConfigEnum getConfigServiceURI() {
		return ApiUrlConfigEnum.APP_CONFIG_URL_OPERATIONS;
	}
}
