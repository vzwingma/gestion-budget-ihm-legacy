package com.terrier.finances.gestion.services;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.terrier.finances.gestion.communs.utils.config.CorrelationsIdUtils;
import com.terrier.finances.gestion.services.admin.api.IAdminAPIService;
import com.terrier.finances.gestion.services.comptes.api.IComptesAPIService;
import com.terrier.finances.gestion.services.operations.api.IBudgetsCompteAPIService;
import com.terrier.finances.gestion.services.operations.api.ILibellesOperationsAPIService;
import com.terrier.finances.gestion.services.operations.api.IOperationsAPIService;
import com.terrier.finances.gestion.services.parametrages.api.IParametragesAPIService;
import com.terrier.finances.gestion.services.utilisateurs.api.IUtilisateursAPIService;
import com.terrier.finances.gestion.ui.login.business.IUserUISessionsService;

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

	private String initCorrId;
	
	public FacadeServices() {
		// CorrId d'initialisation pour faciliter le parsing ELK
		this.initCorrId = UUID.randomUUID().toString();
		CorrelationsIdUtils.putCorrIdOnMDC(this.initCorrId);
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
	private IOperationsAPIService serviceOperations;

	@Autowired
	private IParametragesAPIService serviceParams;

	@Autowired
	private IUtilisateursAPIService serviceUtilisateurs;
	
	@Autowired
	private IComptesAPIService serviceComptes;
	
	@Autowired
	private IBudgetsCompteAPIService budgetsComptes;
	
	@Autowired
	private ILibellesOperationsAPIService libellesOperations;
	
	
	@Autowired
	private IAdminAPIService serviceAdmin;

	@Autowired
	private IUserUISessionsService serviceUserSessions;

	/**
	 * @return l'instance de la façade des services
	 */
	public static FacadeServices get() {
		return facadeServices;
	}

	/**
	 * @return the serviceDepense
	 */
	public IOperationsAPIService getServiceOperations() {
		return serviceOperations;
	}

	/**
	 * @return the serviceParams
	 */
	public IParametragesAPIService getServiceParams() {
		return serviceParams;
	}

	/**
	 * @return the serviceUserSessions
	 */
	public IUserUISessionsService getServiceUserSessions() {
		return serviceUserSessions;
	}

	/**
	 * @return the serviceUtilisateurs
	 */
	public IUtilisateursAPIService getServiceUtilisateurs() {
		return serviceUtilisateurs;
	}

	/**
	 * @return the serviceComptes
	 */
	public IComptesAPIService getServiceComptes() {
		return serviceComptes;
	}

	/**
	 * @return the serviceAdmin
	 */
	public IAdminAPIService getServiceAdmin() {
		return serviceAdmin;
	}

	public IBudgetsCompteAPIService getServiceBudgetsCompte() {
		return budgetsComptes;
	}

	public ILibellesOperationsAPIService getServiceLibellesOperations() {
		return libellesOperations;
	}

	public String getInitCorrId() {
		return initCorrId;
	}
}
