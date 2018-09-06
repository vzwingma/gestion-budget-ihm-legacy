package com.terrier.finances.gestion.communs.utilisateur.model.api;

import com.terrier.finances.gestion.communs.abstrait.AbstractRestObjectModel;

import io.swagger.annotations.ApiModelProperty;

/**
 * Objet REST Authentification
 * @author vzwingma
 *
 */
public class AuthentificationRestObject extends AbstractRestObjectModel {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5381305898282373914L;

	@ApiModelProperty(notes = "Login de l'utilisateur", required=true)
	private String login;
	
	@ApiModelProperty(notes = "Mot de passe hashé techniquement de l'utilisateur", required=true)
	private String motDePasse;

	@ApiModelProperty(notes = "Id de l'utilisateur associé", required=false)
	private String idUtilisateur;
	
	// Constructeur par défaut
	public AuthentificationRestObject(){
		super();
	}
	
	public AuthentificationRestObject(String login, String motDePasse){
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

	/**
	 * @return the idUtilisateur
	 */
	public String getIdUtilisateur() {
		return idUtilisateur;
	}

	/**
	 * @param idUtilisateur the idUtilisateur to set
	 */
	public void setIdUtilisateur(String idUtilisateur) {
		this.idUtilisateur = idUtilisateur;
	}
	
	
}
