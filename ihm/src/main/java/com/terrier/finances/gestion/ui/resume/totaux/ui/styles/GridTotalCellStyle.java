/**
 * 
 */
package com.terrier.finances.gestion.ui.resume.totaux.ui.styles;


import com.terrier.finances.gestion.model.business.budget.TotalBudgetMensuel;
import com.terrier.finances.gestion.ui.operations.model.enums.EntetesGridResumeOperationsEnum;
import com.vaadin.ui.StyleGenerator;


/**
 * Style des cellules du tableau des catégories
 * @author vzwingma
 *
 */
public class GridTotalCellStyle implements StyleGenerator<TotalBudgetMensuel> {


	private static final long serialVersionUID = -2438700237527871644L;


	private EntetesGridResumeOperationsEnum colonne;

	public GridTotalCellStyle(EntetesGridResumeOperationsEnum colonne) {
		this.colonne = colonne;
	}

	@Override
	public String apply(TotalBudgetMensuel item) {
		StringBuilder style = new StringBuilder("v-grid-cell-content-totaux");
		
		if((EntetesGridResumeOperationsEnum.VALEUR_NOW.equals(this.colonne) && item.getTotalADate() < 0)
				|| (EntetesGridResumeOperationsEnum.VALEUR_FIN.equals(this.colonne) && item.getTotalFinMois() < 0)){
			style.append("_rouge");
		}
		return style.toString();
	}
}
