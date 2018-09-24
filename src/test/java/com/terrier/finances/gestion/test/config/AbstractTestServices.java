/**
 * 
 */
package com.terrier.finances.gestion.test.config;

import static org.mockito.Mockito.spy;

import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.terrier.finances.gestion.services.operations.api.OperationsAPIService;
import com.terrier.finances.gestion.services.parametrages.api.ParametragesAPIService;
import com.terrier.finances.gestion.ui.communs.config.AppConfig;

/**
 * Classe abstraite des tests d'API
 * @author vzwingma
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={AppConfig.class})
public abstract class AbstractTestServices {

	/**
	 * Logger
	 */
	protected final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private ParametragesAPIService paramsAPIService;
	
	@Autowired
	private OperationsAPIService operationsAPIService;
	
	@Bean public ParametragesAPIService spyParamsAPIService(){
		return spy(paramsAPIService);
	}
	
	
	@Bean public OperationsAPIService spyOperationsAPIService(){
		return spy(operationsAPIService);
	}
	
}
