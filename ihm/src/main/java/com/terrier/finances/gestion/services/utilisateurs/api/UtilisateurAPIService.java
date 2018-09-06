package com.terrier.finances.gestion.services.utilisateurs.api;

import java.time.LocalDateTime;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;

import org.springframework.stereotype.Controller;

import com.terrier.finances.gestion.communs.utilisateur.model.api.AuthLoginRestObject;
import com.terrier.finances.gestion.communs.utilisateur.model.api.AuthResponseRestObject;
import com.terrier.finances.gestion.communs.utils.data.BudgetApiUrlEnum;
import com.terrier.finances.gestion.services.utilisateurs.business.UtilisateursService;
import com.terrier.finances.gestion.ui.communs.abstrait.rest.AbstractHTTPClient;

/**
 * Service API vers {@link UtilisateursService}
 * @author vzwingma
 *
 */
@Controller
public class UtilisateurAPIService extends AbstractHTTPClient {
	
	final String URI = "http://localhost:8080/ihm/rest";

	
	/**
	 * Validation login/mdp
	 * @param login login
	 * @param motPasseEnClair mdp
	 * @return si valide
	 */
	public String authenticate(String login, String motPasseEnClair){
		
		Entity<AuthLoginRestObject> auth = Entity.entity(new AuthLoginRestObject(login, motPasseEnClair), MediaType.APPLICATION_JSON_TYPE);
		AuthResponseRestObject resultat =  callHTTPPost(URI, BudgetApiUrlEnum.USERS_AUTHENTICATE_FULL, auth, AuthResponseRestObject.class);
		return resultat != null ? resultat.getIdUtilisateur() : null;
	}
	
	
	/**
	 * Déconnexion d'un utilisateur
	 * @param idSession
	 */
	public void deconnexion(String idSession){
		callHTTPPost(URI, BudgetApiUrlEnum.USERS_DISCONNECT_FULL + "/" + idSession, null, null);
	}
	
	
	/**
	 * Déconnexion d'un utilisateur
	 * @param idSession
	 */
	public LocalDateTime getLastAccessTime(String idSession){
		return callHTTPGetData(URI, BudgetApiUrlEnum.USERS_ACCESS_DATE_FULL + "/" + idSession, LocalDateTime.class);
	}
}
