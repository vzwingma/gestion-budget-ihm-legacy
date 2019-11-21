package com.terrier.finances.gestion.services.admin.api;

import org.springframework.stereotype.Controller;

import com.terrier.finances.gestion.services.abstrait.api.AbstractHTTPClient;
import com.terrier.finances.gestion.ui.communs.config.AppConfigEnum;

/**
 * Service API vers {@link AdminService}
 * @author vzwingma
 *
 */
@Controller
public class AdminAPIService extends AbstractHTTPClient {
	


	@Override
	public AppConfigEnum getConfigServiceURI() {
		return AppConfigEnum.APP_CONFIG_URL_UTILISATEURS;
	}
	
	
}
