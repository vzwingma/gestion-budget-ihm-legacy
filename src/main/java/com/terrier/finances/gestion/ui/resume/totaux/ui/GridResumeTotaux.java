package com.terrier.finances.gestion.ui.resume.totaux.ui;

import com.terrier.finances.gestion.communs.budget.model.v12.TotauxCategorie;
import com.terrier.finances.gestion.ui.communs.abstrait.AbstractUIGridComponent;
import com.terrier.finances.gestion.ui.operations.model.enums.EntetesGridResumeOperationsEnum;
import com.terrier.finances.gestion.ui.operations.ui.renderers.OperationBudgetTypeRenderer;
import com.terrier.finances.gestion.ui.resume.totaux.ui.styles.GridTotalCellStyle;

/**
 * Tableau de suivi des dépenses
 * @author vzwingma
 *
 */
public class GridResumeTotaux extends AbstractUIGridComponent<GridResumeTotauxController, TotauxCategorie> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7187184070043964584L;


	public GridResumeTotaux(){
		// Start controleur
		startControleur();
	}



	@Override
	public void paramComponentsOnGrid() {

		setSelectionMode(SelectionMode.NONE);

		/**
		 * Total resume
		 */
		addColumn(TotauxCategorie::getLibelleCategorie)
		.setId(EntetesGridResumeOperationsEnum.CATEGORIE.getId())
		.setSortable(false)
		.setResizable(false)
		.setHidable(false);

		addColumn(TotauxCategorie::getTotalAtMaintenant)
		.setId(EntetesGridResumeOperationsEnum.VALEUR_NOW.getId())
		.setSortable(false)
		.setResizable(false)
		.setHidable(false)
		.setStyleGenerator(new GridTotalCellStyle(EntetesGridResumeOperationsEnum.VALEUR_NOW))
		.setRenderer(new OperationBudgetTypeRenderer());

		addColumn(TotauxCategorie::getTotalAtFinMoisCourant)
		.setId(EntetesGridResumeOperationsEnum.VALEUR_FIN.getId())
		.setSortable(false)
		.setResizable(false)
		.setHidable(false)
		.setStyleGenerator(new GridTotalCellStyle(EntetesGridResumeOperationsEnum.VALEUR_FIN))
		.setRenderer(new OperationBudgetTypeRenderer());
	}

	/* (non-Javadoc)
	 * @see com.terrier.finances.gestion.ui.components.AbstractUITableComponent#createControleur()
	 */
	@Override
	public GridResumeTotauxController createControleurGrid() {
		return new GridResumeTotauxController(this);
	}
}
