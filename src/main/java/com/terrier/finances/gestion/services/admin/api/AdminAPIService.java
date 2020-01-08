package com.terrier.finances.gestion.services.admin.api;

import org.springframework.stereotype.Controller;

import com.terrier.finances.gestion.communs.api.config.ApiUrlConfigEnum;
import com.terrier.finances.gestion.services.abstrait.api.AbstractHTTPClient;

/**
 * Service API vers {@link AdminService}
 * @author vzwingma
 *
 */
@Controller
public class AdminAPIService extends AbstractHTTPClient {
	


	@Override
	public ApiUrlConfigEnum getConfigServiceURI() {
		return ApiUrlConfigEnum.APP_CONFIG_URL_UTILISATEURS;
	}
	
	
}
