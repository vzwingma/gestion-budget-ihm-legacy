package com.terrier.finances.gestion.services.comptes.api;

import java.util.List;

import org.springframework.stereotype.Controller;

import com.terrier.finances.gestion.communs.comptes.model.CompteBancaire;
import com.terrier.finances.gestion.communs.comptes.model.api.IntervallesCompteAPIObject;
import com.terrier.finances.gestion.communs.utils.data.BudgetApiUrlEnum;
import com.terrier.finances.gestion.communs.utils.exceptions.DataNotFoundException;
import com.terrier.finances.gestion.ui.communs.abstrait.api.AbstractHTTPClient;

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
	 */
	public List<CompteBancaire> getComptes(String idUtilisateur){
		return callHTTPGetListData(URI, BudgetApiUrlEnum.COMPTES_LIST_FULL + "/" + idUtilisateur, CompteBancaire.class);
	}
	
	/**
	 * Compte d'un utilisateur
	 * @param idCompte
	 * @param idUtilisateur
	 */
	public CompteBancaire getCompte(String idCompte, String idUtilisateur){
		return callHTTPGetData(URI, BudgetApiUrlEnum.COMPTES_ID_FULL + "/" + idCompte + "/" + idUtilisateur, CompteBancaire.class);
	}

	
	/**
	 * Charge l'intervalle des budgets pour ce compte pour cet utilisateur
	 * @param utilisateur utilisateur
	 * @param compte id du compte
	 * @return la date du premier budget décrit pour cet utilisateur
	 */
	public IntervallesCompteAPIObject getIntervallesBudgets(String compte, String idUtilisateur) throws DataNotFoundException{
		String path = BudgetApiUrlEnum.COMPTES_INTERVALLES_FULL.replace("{idCompte}", compte);
		return callHTTPGetData(URI, path, IntervallesCompteAPIObject.class);
	}
}
