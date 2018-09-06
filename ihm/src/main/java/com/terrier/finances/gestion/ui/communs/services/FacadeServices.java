package com.terrier.finances.gestion.ui.communs.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.terrier.finances.gestion.services.budget.business.OperationsService;
import com.terrier.finances.gestion.services.parametrages.business.ParametragesService;
import com.terrier.finances.gestion.services.utilisateurs.api.UtilisateurAPIService;
import com.terrier.finances.gestion.services.utilisateurs.business.AuthenticationService;
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
	private OperationsService serviceOperations;

	@Autowired
	private ParametragesService serviceParams;
	
    @Autowired
    private AuthenticationService serviceAuth;

	@Autowired
	private UtilisateurAPIService serviceRestAuth;
	
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
	public OperationsService getServiceOperations() {
		return serviceOperations;
	}

	/**
	 * @param serviceOperations
	 *            the serviceDepense to set
	 */
	public void setServiceOperations(OperationsService serviceOperations) {
		LOGGER.trace("[INIT] Injection du service métier Operations");
		this.serviceOperations = serviceOperations;
	}

	/**
	 * @return the serviceParams
	 */
	public ParametragesService getServiceParams() {
		return serviceParams;
	}

	/**
	 * @param serviceParams
	 *            the serviceParams to set
	 */
	public void setServiceParams(ParametragesService serviceParams) {
		LOGGER.trace("[INIT] Injection de ParametragesService");
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
		LOGGER.trace("[INIT] Injection de UserSessionsManager");
		this.serviceUserSessions = serviceUserSessions;
	}
    /**
     * @return the serviceAuth
     */
    public AuthenticationService getServiceAuth() {
        return serviceAuth;
    }

    /**
     * @param serviceAuth
     *            the serviceAuth to set
     */
    public void setServiceAuth(AuthenticationService serviceAuth) {
        LOGGER.info("Injection de AuthenticationService");
        this.serviceAuth = serviceAuth;
    }

	/**
	 * @return the serviceRestAuth
	 */
	public UtilisateurAPIService getServiceAPIAuth() {
		return serviceRestAuth;
	}

	/**
	 * @param serviceRestAuth the serviceRestAuth to set
	 */
	public void setServiceRestAuth(com.terrier.finances.gestion.services.utilisateurs.api.UtilisateurAPIService serviceRestAuth) {
		LOGGER.trace("[INIT] Injection de AuthenticationService");
		this.serviceRestAuth = serviceRestAuth;
	}

}
