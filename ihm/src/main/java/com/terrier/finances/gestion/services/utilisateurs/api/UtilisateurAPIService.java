package com.terrier.finances.gestion.services.utilisateurs.api;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import com.terrier.finances.gestion.communs.utilisateur.model.api.AuthLoginRestObject;
import com.terrier.finances.gestion.communs.utilisateur.model.api.AuthResponseRestObject;
import com.terrier.finances.gestion.communs.utils.data.BudgetApiUrlEnum;
import com.terrier.finances.gestion.services.utilisateurs.business.AuthenticationService;
import com.terrier.finances.gestion.ui.communs.abstrait.rest.AbstractHTTPClient;

/**
 * Service API vers {@link AuthenticationService}
 * @author vzwingma
 *
 */
@Controller
public class UtilisateurAPIService extends AbstractHTTPClient {
	
	final String URI = "http://localhost:8080/ihm/rest";
	/**
	 * Logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(UtilisateurAPIService.class);
	
	/**
	 * Validation login/mdp
	 * @param login login
	 * @param motPasseEnClair mdp
	 * @return si valide
	 */
	public String authenticate(String login, String motPasseEnClair){
		
		Entity<AuthLoginRestObject> auth = Entity.entity(new AuthLoginRestObject(login, motPasseEnClair), MediaType.APPLICATION_JSON_TYPE);
		AuthResponseRestObject resultat =  callHTTPPost(URI, BudgetApiUrlEnum.AUTH_AUTHENTICATE_FULL, auth, AuthResponseRestObject.class);
		LOGGER.info("Résultat : {}", resultat);
		return resultat.getIdUtilisateur();
	}
}
