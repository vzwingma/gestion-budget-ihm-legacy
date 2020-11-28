package com.terrier.finances.gestion.services.parametrages.api;

import com.terrier.finances.gestion.communs.api.config.ApiUrlConfigEnum;
import com.terrier.finances.gestion.communs.parametrages.model.v12.CategorieOperation;
import com.terrier.finances.gestion.communs.utils.data.BudgetApiUrlEnum;
import com.terrier.finances.gestion.communs.utils.data.BudgetDateTimeUtils;
import com.terrier.finances.gestion.communs.utils.exceptions.UserNotAuthorizedException;
import com.terrier.finances.gestion.services.abstrait.api.AbstractAPIClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import java.text.ParseException;
import java.util.List;

/**
 * Service API vers {@link ParametrageControlleur}
 * @author vzwingma
 *
 */
@Controller
public class ParametragesAPIService extends AbstractAPIClient<CategorieOperation> implements IParametragesAPIService {

	private String uiRefreshPeriod;
	private String version;
	private String buildTime;
	
	
	private List<CategorieOperation> listeCategories = null;
	
	public ParametragesAPIService() {
		super(CategorieOperation.class);
	}

	/**
	 * @return liste des catégories
	 * @throws UserNotAuthorizedException  erreur d'auth
	 */
	public List<CategorieOperation> getCategories() {
		if(listeCategories == null){
			List<CategorieOperation> resultatCategories = callHTTPGetListData(BudgetApiUrlEnum.PARAMS_CATEGORIES_FULL).block();
			// Recalcul des liens sur les catégories parentes
			if(resultatCategories != null){
				resultatCategories
					.parallelStream()
					.forEach(c ->
						c.getListeSSCategories()
							.parallelStream()
							.forEach(ssc -> ssc.setCategorieParente(c)));
				this.listeCategories = resultatCategories;
			}
		}
		return listeCategories;
	}

	
	
	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @param version the version to set
	 */
	@Value("${budget.version:CURRENT}")
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * @return the buildTime
	 */
	public String getBuildTime() {
		return buildTime;
	}

	/**
	 * @param utcBuildTime the buildTime to set (en UTC)
	 */
	@Value("${budget.build.time:NOW}")
	public void setBuildTime(String utcBuildTime) {
		try {
			this.buildTime = BudgetDateTimeUtils.getUtcToLocalTime(utcBuildTime);
		} catch (ParseException e) {
			this.buildTime = utcBuildTime;
		}
	}

	/**
	 * @return période de rafraichissement des IHM
	 */
	public String getUiRefreshPeriod() {
		return uiRefreshPeriod;
	}


	/**
	 * période de rafraichissement des IHM
	 * @param uiRefreshPeriod
	 */
	@Value("${budget.ui.refresh.period:1}")
	public void setUiRefreshPeriod(String uiRefreshPeriod) {
		this.uiRefreshPeriod = uiRefreshPeriod;
	}



	@Override
	public ApiUrlConfigEnum getConfigServiceURI() {
		return ApiUrlConfigEnum.APP_CONFIG_URL_PARAMS;
	}
}
