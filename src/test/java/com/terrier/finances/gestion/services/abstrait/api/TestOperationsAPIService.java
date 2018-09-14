package com.terrier.finances.gestion.services.abstrait.api;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.terrier.finances.gestion.services.operations.api.OperationsAPIService;
import com.terrier.finances.gestion.test.config.AbstractTestsAPI;

/**
 * Test des opérations
 * @author vzwingma
 *
 */
public class TestOperationsAPIService extends AbstractTestsAPI {

	
	@Autowired
	private OperationsAPIService service;
	
	@Test
	public void test(){
		assertNotNull(service);
		
		service.callHTTPGet("/", "/");
	}
}
