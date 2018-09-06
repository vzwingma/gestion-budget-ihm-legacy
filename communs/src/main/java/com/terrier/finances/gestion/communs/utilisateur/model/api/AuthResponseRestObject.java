package com.terrier.finances.gestion.communs.utilisateur.model.api;

import com.terrier.finances.gestion.communs.abstrait.AbstractRestObjectModel;

import io.swagger.annotations.ApiModelProperty;

/**
 * Objet REST Authentification
 * @author vzwingma
 *
 */
public class AuthResponseRestObject extends AbstractRestObjectModel {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5381305898282373914L;

	@ApiModelProperty(notes = "Id de l'utilisateur associé", required=false)
	private String idUtilisateur;
	
	// Constructeur par défaut
	public AuthResponseRestObject(){
		super();
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AuthResponseRestObject [idUtilisateur=").append(idUtilisateur).append("]");
		return builder.toString();
	}


}
