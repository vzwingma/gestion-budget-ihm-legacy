package com.terrier.finances.gestion.ui.communs.services;

import org.junit.Test;

import com.terrier.finances.gestion.services.utilisateurs.api.UtilisateurAPIService;

public class TestRestServices {

	
	@Test
	public void testAuthentication(){
		UtilisateurAPIService rest = new UtilisateurAPIService();
		rest.authenticate("test", "test");
	}
}
