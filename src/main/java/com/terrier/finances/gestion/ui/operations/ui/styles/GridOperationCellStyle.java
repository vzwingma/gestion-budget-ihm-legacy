/**
 * 
 */
package com.terrier.finances.gestion.ui.operations.ui.styles;

import com.terrier.finances.gestion.communs.operations.model.v12.LigneOperation;
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
		StringBuilder style = new StringBuilder("v-grid-row-").append(depense.getEtat().getId());
		// Style de la dernière opération
		if(depense.isTagDerniereOperation()){
			style.append(" last-depense");
		}
		return style.toString();
	}
}
