package com.terrier.finances.gestion.communs.utilisateur.model.api;

import java.util.EnumMap;
import java.util.Map;

import com.terrier.finances.gestion.communs.abstrait.AbstractAPIObjectModel;
import com.terrier.finances.gestion.communs.utilisateur.enums.UtilisateurDroitsEnum;

import io.swagger.annotations.ApiModelProperty;

/**
 * Objet REST Authentification
 * @author vzwingma
 *
 */
public class AuthResponseAPIObject extends AbstractAPIObjectModel {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5381305898282373914L;

	@ApiModelProperty(notes = "Id de l'utilisateur associé", required=true)
	private String idUtilisateur;
	
	/**
	 * Droits
	 */
	@ApiModelProperty(notes = "Droits de l'utilisateur associé", required=true)
	private Map<UtilisateurDroitsEnum, Boolean> droits = new EnumMap<>(UtilisateurDroitsEnum.class);
	
	
	// Constructeur par défaut
	public AuthResponseAPIObject(){
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
	
	

	/**
	 * @return the droits
	 */
	public Map<UtilisateurDroitsEnum, Boolean> getDroits() {
		return droits;
	}

	/**
	 * @param droits the droits to set
	 */
	public void setDroits(Map<UtilisateurDroitsEnum, Boolean> droits) {
		this.droits = droits;
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
