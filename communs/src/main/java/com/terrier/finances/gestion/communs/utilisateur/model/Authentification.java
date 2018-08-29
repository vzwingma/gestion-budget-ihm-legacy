package com.terrier.finances.gestion.communs.utilisateur.model;

import com.terrier.finances.gestion.communs.abstrait.AbstractRestObjectModel;

/**
 * Objet REST Authentification
 * @author vzwingma
 *
 */
public class Authentification extends AbstractRestObjectModel {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5381305898282373914L;

	private String login;
	
	private String motDePasse;

	
	public Authentification(String login, String motDePasse){
		this.login = login;
		this.motDePasse = motDePasse;
	}
	/**
	 * @return the login
	 */
	public String getLogin() {
		return login;
	}

	/**
	 * @return the motDePasse
	 */
	public String getMotDePasse() {
		return motDePasse;
	}
}
