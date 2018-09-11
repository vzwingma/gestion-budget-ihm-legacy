package com.terrier.finances.gestion.ui.communs.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.terrier.finances.gestion.services.comptes.api.ComptesAPIService;
import com.terrier.finances.gestion.services.operations.api.OperationsAPIService;
import com.terrier.finances.gestion.services.parametrages.api.ParametragesAPIService;
import com.terrier.finances.gestion.services.utilisateurs.api.UtilisateurAPIService;
import com.terrier.finances.gestion.ui.login.business.UserUISessionsService;

/**
 * Facade des services pour appels depuis l'IHM
 * 
 * @author vzwingma
 *
 */
@Controller
public class FacadeServices {

	/**
	 * Logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(FacadeServices.class);

	// Gestionnaire des composants UI
	private static FacadeServices facadeServices;

	public FacadeServices() {
		LOGGER.debug("[INIT] UI FacadeServices");
		synchronized (FacadeServices.class) {
			facadeServices = this;
		}
	}

	/**
	 * 
	 * Liens vers les services métiers
	 */
	@Autowired
	private OperationsAPIService serviceOperations;

	@Autowired
	private ParametragesAPIService serviceParams;

	@Autowired
	private UtilisateurAPIService serviceUtilisateurs;
	
	@Autowired
	private ComptesAPIService serviceComptes;

	@Autowired
	private UserUISessionsService serviceUserSessions;

	/**
	 * @return l'instance de la façade des services
	 */
	public static FacadeServices get() {
		return facadeServices;
	}

	/**
	 * @return the serviceDepense
	 */
	public OperationsAPIService getServiceOperations() {
		return serviceOperations;
	}

	/**
	 * @param serviceOperations
	 *            the serviceDepense to set
	 */
	public void setServiceOperations(OperationsAPIService serviceOperations) {
		LOGGER.trace("[INIT] OperationsAPIService");
		this.serviceOperations = serviceOperations;
	}

	/**
	 * @return the serviceParams
	 */
	public ParametragesAPIService getServiceParams() {
		return serviceParams;
	}

	/**
	 * @param serviceParams
	 *            the serviceParams to set
	 */
	public void setServiceParams(ParametragesAPIService serviceParams) {
		LOGGER.trace("[INIT] ParametragesAPIService");
		this.serviceParams = serviceParams;
	}

	/**
	 * @return the serviceUserSessions
	 */
	public UserUISessionsService getServiceUserSessions() {
		return serviceUserSessions;
	}

	/**
	 * @param serviceUserSessions
	 *            the serviceUserSessions to set
	 */
	public void setServiceUserSessions(UserUISessionsService serviceUserSessions) {
		LOGGER.trace("[INIT] UsersSessionsService");
		this.serviceUserSessions = serviceUserSessions;
	}


	/**
	 * @return the serviceUtilisateurs
	 */
	public UtilisateurAPIService getServiceUtilisateurs() {
		return serviceUtilisateurs;
	}

	/**
	 * @param serviceUtilisateurs the serviceUtilisateurs to set
	 */
	public void setServiceUtilisateurs(UtilisateurAPIService serviceUtilisateurs) {
		LOGGER.trace("[INIT] UtilisateurService");
		this.serviceUtilisateurs = serviceUtilisateurs;
	}

	/**
	 * @return the serviceComptes
	 */
	public ComptesAPIService getServiceComptes() {
		return serviceComptes;
	}

	/**
	 * @param serviceComptes the serviceComptes to set
	 */
	public void setServiceComptes(ComptesAPIService serviceComptes) {
		LOGGER.trace("[INIT] ComptesService");
		this.serviceComptes = serviceComptes;
	}
}
