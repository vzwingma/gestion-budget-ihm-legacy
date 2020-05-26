/**
 * 
 */
package com.terrier.finances.gestion.ui.comptes.ui.styles;

import com.terrier.finances.gestion.communs.comptes.model.v12.CompteBancaire;
import com.vaadin.ui.StyleGenerator;

/**
 * @author vzwingma
 *
 */
public class ComptesItemStyle implements StyleGenerator<CompteBancaire> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 682039604448023201L;

	/* (non-Javadoc)
	 * @see com.vaadin.ui.StyleGenerator#apply(java.lang.Object)
	 */
	@Override
	public String apply(CompteBancaire compteBancaire) {
		if(Boolean.FALSE.equals(compteBancaire.isActif())){
			return "barre";
		}
		return null;
	}
}
