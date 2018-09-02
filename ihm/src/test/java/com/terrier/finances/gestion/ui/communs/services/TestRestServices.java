package com.terrier.finances.gestion.ui.communs.services;

import org.junit.Test;

public class TestRestServices {

	
	@Test
	public void testAuthentication(){
		AuthenticationRestService rest = new AuthenticationRestService();
		rest.authenticate("test", "test");
	}
}
