package com.terrier.finances.gestion.services.abstrait.api;

import com.terrier.finances.gestion.communs.api.model.Info;
import reactor.core.publisher.Mono;


/**
 * Classe d'un client HTTP
 * @author vzwingma
 *
 */
public interface IAPIClient {
	/**
	 * Statut du Services
	 */
	public Mono<Info> getInfo();
}
