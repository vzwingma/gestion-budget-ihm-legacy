package com.terrier.finances.gestion.ui.components.budget.mensuel.components;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.terrier.finances.gestion.model.business.budget.ResumeTotalCategories;
import com.terrier.finances.gestion.model.enums.EntetesTreeResumeDepenseEnum;
import com.terrier.finances.gestion.ui.components.abstrait.AbstractUITreeGridComponent;
import com.terrier.finances.gestion.ui.controler.budget.mensuel.resume.TreeGridResumeCategoriesController;
import com.terrier.finances.gestion.ui.styles.total.GridTotalCellStyle;
import com.vaadin.shared.MouseEventDetails.MouseButton;

/**
 * Tableau de suivi des dépenses
 * @author vzwingma
 *
 */
public class TreeGridResumeCategories extends AbstractUITreeGridComponent<ResumeTotalCategories, TreeGridResumeCategoriesController> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7187184070043964584L;

	public static final int TAILLE_COLONNE_VALEUR = 90;
	
	
	public TreeGridResumeCategories(){
		// Start controleur
		startControleur();
	}

	
	@Override
	public void paramComponentsOnTreeGrid() {
		
		addColumn(ResumeTotalCategories::getTypeTotal)
		.setId(EntetesTreeResumeDepenseEnum.CATEGORIE.getId())
		.setCaption(EntetesTreeResumeDepenseEnum.CATEGORIE.getLibelle());
		
		addColumn(ResumeTotalCategories::getTotalADate)
		.setId(EntetesTreeResumeDepenseEnum.VALEUR_NOW.getId())
		.setCaption(EntetesTreeResumeDepenseEnum.VALEUR_NOW.getLibelle())
		.setWidth(TAILLE_COLONNE_VALEUR)
		.setRenderer(new GridTotalCellStyle(EntetesTreeResumeDepenseEnum.VALEUR_NOW));
		
		addColumn(ResumeTotalCategories::getTotalFinMois)
		.setId(EntetesTreeResumeDepenseEnum.VALEUR_FIN.getId())
		.setCaption(EntetesTreeResumeDepenseEnum.VALEUR_FIN.getLibelle())
		.setWidth(TAILLE_COLONNE_VALEUR)
		.setRenderer(new GridTotalCellStyle(EntetesTreeResumeDepenseEnum.VALEUR_FIN));
		
		// Pas de sélection de lignes
		setSelectionMode(SelectionMode.NONE);
		/**

		getComponent().setColumnAlignment(EntetesTreeResumeDepenseEnum.VALEUR_NOW.getId(), Align.RIGHT);
		getComponent().setColumnAlignment(EntetesTreeResumeDepenseEnum.VALEUR_FIN.getId(), Align.RIGHT);
		 */
		addItemClickListener(event -> {
			// Double click / collapse/expend all
			if(MouseButton.LEFT.equals(event.getMouseEventDetails().getButton()) && event.getMouseEventDetails().isDoubleClick()){
				getControleur().collapseExpendTreeGrid();
			}
		});
	}

	/* (non-Javadoc)
	 * @see com.terrier.finances.gestion.ui.components.AbstractUITreeTableComponent#createControleur()
	 */
	@Override
	public TreeGridResumeCategoriesController createControleurTreeGrid() {
		return new TreeGridResumeCategoriesController(this);
	}
}
