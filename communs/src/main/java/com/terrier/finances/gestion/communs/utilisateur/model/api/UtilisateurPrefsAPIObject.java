package com.terrier.finances.gestion.communs.utilisateur.model.api;

import java.util.Map;

import com.terrier.finances.gestion.communs.abstrait.AbstractAPIObjectModel;
import com.terrier.finances.gestion.communs.utilisateur.enums.UtilisateurPrefsEnum;

import io.swagger.annotations.ApiModelProperty;

/**
 * Objet API des préférences utilisateur
 * @author vzwingma
 *
 */
public class UtilisateurPrefsAPIObject extends AbstractAPIObjectModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7746155427438885252L;
	
	@ApiModelProperty(notes = "Id de l'utilisateur associé", required=true)
	private String idUtilisateur;
	
	@ApiModelProperty(notes = "Date de dernier accès", required=false)
	private Long lastAccessTime;

	@ApiModelProperty(notes = "Liste des préférences utilisateurs", required=false)
	private Map<UtilisateurPrefsEnum, String> preferences;

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
	 * @return the lastAccessTime
	 */
	public Long getLastAccessTime() {
		return lastAccessTime;
	}


	/**
	 * @param lastAccessTime the lastAccessTime to set
	 */
	public void setLastAccessTime(Long lastAccessTime) {
		this.lastAccessTime = lastAccessTime;
	}

	

	/**
	 * @return the prefsUtilisateur
	 */
	public Map<UtilisateurPrefsEnum, String> getPreferences() {
		return preferences;
	}


	/**
	 * @param prefsUtilisateur the prefsUtilisateur to set
	 */
	public void setPreferences(Map<UtilisateurPrefsEnum, String> prefsUtilisateur) {
		this.preferences = prefsUtilisateur;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UtilisateurPrefsAPIObject [idUtilisateur=").append(idUtilisateur);
		if(lastAccessTime != null){
			builder.append(", lastAccessTime=")
				.append(lastAccessTime);
		}
		if(this.preferences != null){
			builder.append(", preferences=").append(preferences);
		}
		builder.append("]");
		return builder.toString();
	}

}
