package com.terrier.finances.gestion.services.operations.api;

import com.terrier.finances.gestion.communs.api.config.ApiUrlConfigEnum;
import com.terrier.finances.gestion.communs.comptes.model.api.IntervallesCompteAPIObject;
import com.terrier.finances.gestion.communs.utils.data.BudgetApiUrlEnum;
import com.terrier.finances.gestion.services.abstrait.api.AbstractAPIClient;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;

/**
 * API  vers le domaine Budget
 * @author vzwingma
 *
 */
@Controller
public class BudgetsCompteAPIService extends AbstractAPIClient<IntervallesCompteAPIObject> implements IBudgetsCompteAPIService {
	
	
	public BudgetsCompteAPIService() {
		super(IntervallesCompteAPIObject.class);
	}
	
	/**
	 * Charge l'intervalle des budgets pour ce compte pour cet utilisateur
	 * @param compte id du compte
	 * @return la date du premier budget décrit pour cet utilisateur
	 */
	public IntervallesCompteAPIObject getIntervallesBudgets(String compte) {
		
		Map<String, String> pathParams = new HashMap<>();
		pathParams.put(BudgetApiUrlEnum.PARAM_ID_COMPTE, compte);
		return callHTTPGetData(BudgetApiUrlEnum.BUDGET_COMPTE_INTERVALLES_FULL, pathParams, null).block();
	}


	@Override
	public ApiUrlConfigEnum getConfigServiceURI() {
		return ApiUrlConfigEnum.APP_CONFIG_URL_OPERATIONS;
	}
}
