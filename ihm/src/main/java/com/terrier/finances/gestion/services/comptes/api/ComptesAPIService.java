package com.terrier.finances.gestion.services.comptes.api;

import java.util.List;

import org.springframework.stereotype.Controller;

import com.terrier.finances.gestion.communs.comptes.model.CompteBancaire;
import com.terrier.finances.gestion.communs.utils.data.BudgetApiUrlEnum;
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
	@SuppressWarnings("unchecked")
	public List<CompteBancaire> getComptes(String idUtilisateur){
		return callHTTPGetData(URI, BudgetApiUrlEnum.COMPTES_LIST_FULL + "/" + idUtilisateur, List.class);
	}
	
	/**
	 * Compte d'un utilisateur
	 * @param idCompte
	 * @param idUtilisateur
	 */
	public CompteBancaire getCompte(String idCompte, String idUtilisateur){
		return callHTTPGetData(URI, BudgetApiUrlEnum.COMPTES_ID_FULL + "/" + idCompte + "/" + idUtilisateur, CompteBancaire.class);
	}
	
}
