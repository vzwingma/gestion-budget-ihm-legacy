package com.terrier.finances.gestion.communs.utilisateur.model.api;

import com.terrier.finances.gestion.communs.abstrait.AbstractAPIObjectModel;

import io.swagger.annotations.ApiModelProperty;

/**
 * Objet REST Authentification
 * @author vzwingma
 *
 */
public class AuthLoginRestObject extends AbstractAPIObjectModel {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5381305898282373914L;

	@ApiModelProperty(notes = "Login de l'utilisateur", required=true)
	private String login;
	
	@ApiModelProperty(notes = "Mot de passe hashé techniquement de l'utilisateur", required=true)
	private String motDePasse;

	
	// Constructeur par défaut
	public AuthLoginRestObject(){
		super();
	}
	
	public AuthLoginRestObject(String login, String motDePasse){
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AuthentificationRestObject [login=").append(login).append("]");
		return builder.toString();
	}
}
