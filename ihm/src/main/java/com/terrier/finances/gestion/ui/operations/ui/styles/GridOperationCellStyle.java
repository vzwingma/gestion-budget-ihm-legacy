/**
 * 
 */
package com.terrier.finances.gestion.ui.operations.ui.styles;

import com.terrier.finances.gestion.communs.operations.model.LigneOperation;
import com.terrier.finances.gestion.communs.parametrages.model.enums.IdsCategoriesEnum;
import com.vaadin.ui.StyleGenerator;

/**
 * Style des lignes du tableau de dépense
 * @author vzwingma
 */
public class GridOperationCellStyle implements StyleGenerator<LigneOperation> {


	private static final long serialVersionUID = -6709397765771547573L;


	@Override
	public String apply(LigneOperation depense) {

		// Style de la ligne
		StringBuilder style = new StringBuilder("v-grid-row-");
		//  Sauf pour les dépenses réalisées, et celle réserve

		if(!IdsCategoriesEnum.RESERVE.getId().equals(depense.getSsCategorie().getId())){
			style.append(depense.getEtat().getId());
		}

		// Style de la dernière opération
		if(depense.isDerniereOperation()){
			style.append(" last-depense");
		}
		return style.toString();
	}
}
