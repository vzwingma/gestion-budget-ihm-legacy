package com.terrier.finances.gestion.services.parametrages.api;

import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import com.terrier.finances.gestion.communs.parametrages.model.CategorieOperation;
import com.terrier.finances.gestion.communs.utils.data.BudgetApiUrlEnum;
import com.terrier.finances.gestion.communs.utils.data.BudgetDateTimeUtils;
import com.terrier.finances.gestion.ui.communs.abstrait.api.AbstractHTTPClient;

/**
 * Service API vers {@link ParametrageControlleur}
 * @author vzwingma
 *
 */
@Controller
public class ParametragesAPIService extends AbstractHTTPClient {

	private String uiRefreshPeriod;
	private String version;
	private String buildTime;
	
	
	private List<CategorieOperation> listeCategories = null;
	

	/**
	 * @return liste des catégories
	 */
	public List<CategorieOperation> getCategories(){
		if(listeCategories == null){
			List<CategorieOperation> resultatCategories = callHTTPGetListData(URI, BudgetApiUrlEnum.PARAMS_CATEGORIES, CategorieOperation.class);
			// Recalcul des liens
			if(resultatCategories != null){
				resultatCategories
					.parallelStream()
					.forEach(c -> {
						c.getListeSSCategories()
							.parallelStream()
							.forEach(ssc -> ssc.setCategorieParente(c));
					});
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
}