/**
 * 
 */
package com.terrier.finances.gestion.services.statut.api;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.terrier.finances.gestion.communs.utils.data.BudgetApiUrl;
import com.terrier.finances.gestion.services.statut.business.StatusApplicationService;
import com.terrier.finances.gestion.services.statut.model.DependencyName;
import com.terrier.finances.gestion.services.statut.model.StatutDependencyObject;
import com.terrier.finances.gestion.services.statut.model.StatutStateEnum;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * Controleur REST pour récupérer les budgets
 * @author vzwingma
 *
 */
@RestController
@RequestMapping(value=BudgetApiUrl.ROOT_URL)
@Api(consumes="application/json", protocols="https", value="Administration", tags={"Administration"})
public class StatutAPIController {


	/**
	 * Logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(StatutAPIController.class);

	
	@Autowired
	protected StatusApplicationService statusApplicationService;

	@PostConstruct
	public void initApplication(){
		statusApplicationService.updateDependencyStatut(DependencyName.REST_SERVICE, StatutStateEnum.OK);
	}

	/**
	 * Appel PING
	 * @return résultat du ping
	 */
	@ApiOperation(httpMethod="GET", produces=MediaType.APPLICATION_JSON_VALUE, protocols="https", value="Statut de l'opération", response=StatutDependencyObject.class)
	@ApiResponses(value = {
            @ApiResponse(code = 200, message = "Statut de l'application"),
            @ApiResponse(code = 401, message = "L'opération doit être identifiée"),
            @ApiResponse(code = 403, message = "L'opération n'est pas autorisée"),
            @ApiResponse(code = 404, message = "Ressource introuvable")
    }) 
	@GetMapping(value=BudgetApiUrl.STATUT_BASE_URL)
	public @ResponseBody StatutDependencyObject ping(){
		LOGGER.info("Appel statut : {}", this.statusApplicationService.getStatutApplication());
		return this.statusApplicationService.getStatutApplication();
	}
	
	
	@PreDestroy
	public void stopApplication(){
		statusApplicationService.updateDependencyStatut(DependencyName.REST_SERVICE, StatutStateEnum.DEGRADE);
	}
	
	
}
