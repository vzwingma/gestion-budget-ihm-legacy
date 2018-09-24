/**
 * 
 */
package com.terrier.finances.gestion.ui.resume.categories.ui.styles;

import com.terrier.finances.gestion.communs.budget.model.ResumeTotalCategories;
import com.terrier.finances.gestion.ui.operations.model.enums.EntetesGridResumeOperationsEnum;
import com.vaadin.ui.StyleGenerator;


/**
 * Style des cellules valeurs du tableau des catégories
 * @author vzwingma
 */
public class TreeGridResumeValeurCellStyle implements StyleGenerator<ResumeTotalCategories> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 827155942421701761L;
	private EntetesGridResumeOperationsEnum colonne;

	public TreeGridResumeValeurCellStyle(EntetesGridResumeOperationsEnum colonne) {
		this.colonne = colonne;
	}


	/* (non-Javadoc)
	 * @see com.vaadin.ui.StyleGenerator#apply(java.lang.Object)
	 */
	@Override
	public String apply(ResumeTotalCategories item) {
		
		// Valeur
		StringBuilder style = new StringBuilder("v-grid-cell-valeur");

		if((EntetesGridResumeOperationsEnum.VALEUR_NOW.equals(this.colonne) && item.getTotalADate() < 0)
				|| (EntetesGridResumeOperationsEnum.VALEUR_FIN.equals(this.colonne) && item.getTotalFinMois() < 0)){
			style.append("_rouge");
		}
		return style.toString();
	}
}
