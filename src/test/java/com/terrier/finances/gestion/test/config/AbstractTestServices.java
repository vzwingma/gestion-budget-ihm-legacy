/**
 * 
 */
package com.terrier.finances.gestion.test.config;

import static org.mockito.Mockito.spy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;

import com.terrier.finances.gestion.services.operations.api.OperationsAPIService;
import com.terrier.finances.gestion.services.parametrages.api.ParametragesAPIService;

/**
 * Classe abstraite des tests d'API
 * @author vzwingma
 *
 */


public abstract class AbstractTestServices {

	/**
	 * Logger
	 */
	protected final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	

	private ParametragesAPIService paramsAPIService = new ParametragesAPIService();

	private OperationsAPIService operationsAPIService = new OperationsAPIService();
	
	@Bean public ParametragesAPIService spyParamsAPIService(){
		return spy(paramsAPIService);
	}
	
	
	@Bean public OperationsAPIService spyOperationsAPIService(){
		return spy(operationsAPIService);
	}
	
}
