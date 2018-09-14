package com.terrier.finances.gestion.services.comptes.api;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Controller;

import com.terrier.finances.gestion.communs.comptes.model.CompteBancaire;
import com.terrier.finances.gestion.communs.comptes.model.api.IntervallesCompteAPIObject;
import com.terrier.finances.gestion.communs.operations.model.api.LibellesOperationsAPIObject;
import com.terrier.finances.gestion.communs.utils.data.BudgetApiUrlEnum;
import com.terrier.finances.gestion.communs.utils.exceptions.DataNotFoundException;
import com.terrier.finances.gestion.services.abstrait.api.AbstractHTTPClient;

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
		String path = BudgetApiUrlEnum.COMPTES_LIST_FULL.replace("{idUtilisateur}", idUtilisateur);
		return callHTTPGetListData(path, CompteBancaire.class);
	}
	
	/**
	 * Compte d'un utilisateur
	 * @param idCompte
	 * @param idUtilisateur
	 */
	public CompteBancaire getCompte(String idCompte, String idUtilisateur){
		String path = BudgetApiUrlEnum.COMPTES_ID_FULL.replace("{idCompte}", idCompte).replace("{idUtilisateur}", idUtilisateur);
		return callHTTPGetData(path, CompteBancaire.class);
	}

	
	/**
	 * Charge l'intervalle des budgets pour ce compte pour cet utilisateur
	 * @param utilisateur utilisateur
	 * @param compte id du compte
	 * @return la date du premier budget décrit pour cet utilisateur
	 */
	public IntervallesCompteAPIObject getIntervallesBudgets(String compte, String idUtilisateur) throws DataNotFoundException{
		String path = BudgetApiUrlEnum.COMPTES_INTERVALLES_FULL.replace("{idCompte}", compte);
		return callHTTPGetData(path, IntervallesCompteAPIObject.class);
	}
	
	
	/**
	 * Retourne l'ensemble des libelles des opérations pour un compte
	 * @param idCompte compte de l'utilisateur
	 * @param idUtilisateur utilisateur
	 * @return le set des libelles des opérations
	 */
	public Set<String> getLibellesOperationsForAutocomplete(String idCompte, String idUtilisateur){
		String path = BudgetApiUrlEnum.COMPTES_OPERATIONS_LIBELLES_FULL.replace("{idCompte}", idCompte).replace("{idUtilisateur}", idUtilisateur);
		LibellesOperationsAPIObject libelles = callHTTPGetData(path, LibellesOperationsAPIObject.class);
		return libelles.getLibellesOperations();
	}
}
