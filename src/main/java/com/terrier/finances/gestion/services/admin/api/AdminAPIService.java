package com.terrier.finances.gestion.services.admin.api;

import org.springframework.stereotype.Controller;

import com.terrier.finances.gestion.communs.utils.data.BudgetApiUrlEnum;
import com.terrier.finances.gestion.communs.utils.exceptions.DataNotFoundException;
import com.terrier.finances.gestion.communs.utils.exceptions.UserNotAuthorizedException;
import com.terrier.finances.gestion.services.abstrait.api.AbstractHTTPClient;
import com.terrier.finances.gestion.services.admin.model.Info;
import com.terrier.finances.gestion.ui.communs.config.AppConfigEnum;

/**
 * Service API vers {@link AdminService}
 * @author vzwingma
 *
 */
@Controller
public class AdminAPIService extends AbstractHTTPClient {
	
	/**
	 * Statut du Services 
	 * @throws UserNotAuthorizedException  erreur d'authentification
	 * @throws DataNotFoundException  erreur lors de l'appel
	 */
	public Info getInfo() throws DataNotFoundException, UserNotAuthorizedException {
		return callHTTPGetData(BudgetApiUrlEnum.ACTUATORS_INFO_FULL, Info.class);
	}

	@Override
	public AppConfigEnum getConfigServiceURI() {
		return AppConfigEnum.APP_CONFIG_URL_UTILISATEURS;
	}
	
	
}
