package com.terrier.finances.gestion.ui.communs.services;

import org.junit.Test;

import com.terrier.finances.gestion.services.utilisateurs.api.UtilisateursAPIService;

public class TestRestServices {

	
	@Test
	public void testAuthentication(){
		UtilisateursAPIService rest = new UtilisateursAPIService();
		rest.authenticate("test", "test");
	}
}
