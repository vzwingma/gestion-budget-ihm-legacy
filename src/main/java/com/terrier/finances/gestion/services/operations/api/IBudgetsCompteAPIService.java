package com.terrier.finances.gestion.services.operations.api;

import com.terrier.finances.gestion.communs.comptes.model.api.IntervallesCompteAPIObject;
import com.terrier.finances.gestion.communs.utils.exceptions.DataNotFoundException;
import com.terrier.finances.gestion.communs.utils.exceptions.UserNotAuthorizedException;
import com.terrier.finances.gestion.services.abstrait.api.IAPIClient;

/**
 * API  vers le domaine Budget
 * @author vzwingma
 *
 */
public interface IBudgetsCompteAPIService extends IAPIClient {
	
	
	/**
	 * Charge l'intervalle des budgets pour ce compte pour cet utilisateur
	 * @param utilisateur utilisateur
	 * @param compte id du compte
	 * @return la date du premier budget décrit pour cet utilisateur
	 * @throws UserNotAuthorizedException 
	 */
	public IntervallesCompteAPIObject getIntervallesBudgets(String compte) throws UserNotAuthorizedException, DataNotFoundException;
}
