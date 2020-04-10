package com.terrier.finances.gestion.services.admin.api;

import com.terrier.finances.gestion.communs.api.config.ApiUrlConfigEnum;
import com.terrier.finances.gestion.services.abstrait.api.IAPIClient;

/**
 * Service API vers {@link AdminService}
 * @author vzwingma
 *
 */
public interface IAdminAPIService extends IAPIClient  {

	public ApiUrlConfigEnum getConfigServiceURI() ;

}
