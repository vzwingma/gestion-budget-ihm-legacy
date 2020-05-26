package com.terrier.finances.gestion.services.parametrages.api;

import java.util.List;

import com.terrier.finances.gestion.communs.parametrages.model.v12.CategorieOperation;
import com.terrier.finances.gestion.communs.utils.exceptions.UserNotAuthorizedException;
import com.terrier.finances.gestion.services.abstrait.api.IAPIClient;

/**
 * Interface Service API vers {@link ParametrageControlleur}
 * @author vzwingma
 *
 */
public interface IParametragesAPIService extends IAPIClient  {


	/**
	 * @return liste des catégories
	 * @throws UserNotAuthorizedException  erreur d'auth
	 */
	public List<CategorieOperation> getCategories() ;

	
	
	/**
	 * @return the version
	 */
	public String getVersion();

	/**
	 * @param version the version to set
	 */
	public void setVersion(String version);

	/**
	 * @return the buildTime
	 */
	public String getBuildTime();

	/**
	 * @param utcBuildTime the buildTime to set (en UTC)
	 */
	public void setBuildTime(String utcBuildTime);

	/**
	 * @return période de rafraichissement des IHM
	 */
	public String getUiRefreshPeriod();

	/**
	 * période de rafraichissement des IHM
	 * @param uiRefreshPeriod
	 */
	public void setUiRefreshPeriod(String uiRefreshPeriod);

}
