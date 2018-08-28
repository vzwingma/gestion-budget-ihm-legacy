/**
 * 
 */
package com.terrier.finances.gestion.model.budget;

/**
 * Type de dépenses
 * @author vzwingma
 *
 */
public enum TypeOperationEnum {

	// Crédit
	CREDIT("CREDIT", "+"),
	// Dépense
	DEPENSE("DEPENSE", "-");
	
	

	private String id;
	private String libelle;
	
	/**
	 * Constructeur
	 * @param id
	 * @param libelle
	 */
	private TypeOperationEnum(String id, String libelle){
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
	
	/* (non-Javadoc)
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString(){
		return getLibelle();
	}
}
