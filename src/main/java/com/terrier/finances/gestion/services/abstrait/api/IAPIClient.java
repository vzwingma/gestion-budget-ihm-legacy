package com.terrier.finances.gestion.services.abstrait.api;

import com.terrier.finances.gestion.communs.utils.exceptions.DataNotFoundException;
import com.terrier.finances.gestion.communs.utils.exceptions.UserNotAuthorizedException;
import com.terrier.finances.gestion.services.admin.model.Info;

import reactor.core.publisher.Mono;


/**
 * Classe d'un client HTTP
 * @author vzwingma
 *
 */
public interface IAPIClient {
	/**
	 * Statut du Services 
	 * @throws DataNotFoundException  erreur lors de l'appel
	 */
	public Mono<Info> getInfo() throws DataNotFoundException, UserNotAuthorizedException;
	
	
	/**
	 * 
	 * @return JWT Token de l'utilisateur
	 */
	public String getJwtToken();
}
