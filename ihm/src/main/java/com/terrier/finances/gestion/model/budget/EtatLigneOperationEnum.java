/**
 * 
 */
package com.terrier.finances.gestion.model.budget;

/**
 * Type de dépenses
 * @author vzwingma
 *
 */
public enum EtatLigneOperationEnum {

	// Ligne prévue
	PREVUE("prevue", "Prévue"),
	// Ligne passée
	REALISEE("realisee", "Réalisée"), 
	// Ligne reportée
	REPORTEE("reportee" , "Reportée"), 
	// Ligne annulée
	ANNULEE("annulee", "Annulée");
	
	
	private String id;
	private String libelle;
	
	/**
	 * Constructeur
	 * @param id
	 * @param libelle
	 */
	private EtatLigneOperationEnum(String id, String libelle){
		this.id = id;
		this.libelle = libelle;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return the libelle
	 */
	public String getLibelle() {
		return libelle;
	}
	
	
	public static EtatLigneOperationEnum getEnum(String idEnum){
		for (EtatLigneOperationEnum enums : values()) {
			if(enums.getId().equals(idEnum)){
				return enums;
			}
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString(){
		return getLibelle();
	}
}
