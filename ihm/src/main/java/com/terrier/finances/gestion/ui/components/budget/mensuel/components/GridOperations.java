package com.terrier.finances.gestion.ui.components.budget.mensuel.components;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.terrier.finances.gestion.model.business.parametrage.CategorieDepense;
import com.terrier.finances.gestion.model.data.DataUtils;
import com.terrier.finances.gestion.model.enums.EntetesTableSuiviDepenseEnum;
import com.terrier.finances.gestion.model.enums.TypeDepenseEnum;
import com.terrier.finances.gestion.model.ui.budget.LigneOperationVO;
import com.terrier.finances.gestion.ui.components.abstrait.AbstractUIGridComponent;
import com.terrier.finances.gestion.ui.components.budget.mensuel.ActionsLigneBudget;
import com.terrier.finances.gestion.ui.components.budget.mensuel.binder.LigneOperationEditorBinder;
import com.terrier.finances.gestion.ui.controler.budget.mensuel.liste.operations.GridOperationsController;
import com.terrier.finances.gestion.ui.listener.budget.mensuel.editor.GridOperationsEditorListener;
import com.terrier.finances.gestion.ui.listener.budget.mensuel.editor.GridOperationsRightClickListener;
import com.terrier.finances.gestion.ui.styles.operations.GridOperationCellActionsStyle;
import com.terrier.finances.gestion.ui.styles.operations.GridOperationCellStyle;
import com.terrier.finances.gestion.ui.styles.operations.GridOperationCellValeurStyle;
import com.terrier.finances.gestion.ui.styles.operations.OperationBudgetTypeRenderer;
import com.vaadin.contextmenu.GridContextMenu;
import com.vaadin.ui.renderers.ComponentRenderer;
import com.vaadin.ui.renderers.DateRenderer;
import com.vaadin.ui.renderers.TextRenderer;

/**
 * Tableau de suivi des opérations
 * @author vzwingma
 *
 */
public class GridOperations extends AbstractUIGridComponent<GridOperationsController, LigneOperationVO> {

	//
	private static final long serialVersionUID = -7187184070043964584L;

	public static final int TAILLE_COLONNE_DATE = 95;
	public static final int TAILLE_COLONNE_CATEGORIE = 150;
	public static final int TAILLE_COLONNE_AUTEUR = 100;
	public static final int TAILLE_COLONNE_DATE_EDITEE = 150;
	public static final int TAILLE_COLONNE_ACTIONS = 110;
	public static final int TAILLE_COLONNE_TYPE_MENSUEL = 100;
	public static final int TAILLE_COLONNE_VALEUR = 100;
	
	private final SimpleDateFormat dateFormatMaj = new SimpleDateFormat(DataUtils.DATE_DAY_HOUR_PATTERN, Locale.FRENCH);
	private final SimpleDateFormat dateFormatOperations = new SimpleDateFormat(DataUtils.DATE_DAY_PATTERN, Locale.FRENCH);

	/**
	 * Constructure : démarrage du controleur
	 */
	public GridOperations(){
		
		dateFormatMaj.setTimeZone(DataUtils.getTzParis());
		dateFormatOperations.setTimeZone(DataUtils.getTzParis());
		// Start controleur
		startControleur();
	}


	/* (non-Javadoc)
	 * @see com.terrier.finances.gestion.ui.components.AbstractUITableComponent#getControleur()
	 */
	@Override
	public GridOperationsController createControleurGrid() {
		return new GridOperationsController(this);
	}


	/* (non-Javadoc)
	 * @see com.terrier.finances.gestion.ui.components.abstrait.AbstractUIGridComponent#paramComponentsOnPage()
	 */
	@Override
	public void paramComponentsOnGrid() {
		
		/**
		 *  Editor
		 */
		LigneOperationEditorBinder binderLD = new LigneOperationEditorBinder(getControleur().getServiceParams().getCategories());
		getEditor().setBinder(binderLD);
		
		GridOperationsEditorListener editorListener = new GridOperationsEditorListener(getControleur());
		getEditor().addOpenListener(editorListener);
		getEditor().addSaveListener(editorListener);
		getEditor().addCancelListener(editorListener);
		setSelectionMode(SelectionMode.SINGLE);
		
		/**
		 * Columns
		 */		
		Column<LigneOperationVO, Date> c = addColumn(LigneOperationVO::getDateOperation);
		c.setId(EntetesTableSuiviDepenseEnum.DATE_OPERATION.name())
			.setCaption(EntetesTableSuiviDepenseEnum.DATE_OPERATION.getLibelle())
			.setWidth(TAILLE_COLONNE_DATE)
			.setHidable(true)
			.setResizable(false);
		c.setRenderer(new DateRenderer(dateFormatMaj));
		c.setStyleGenerator(new GridOperationCellStyle());
		// Pas éditable

		Column<LigneOperationVO, CategorieDepense> c2 = addColumn(LigneOperationVO::getCategorie);
		c2.setId(EntetesTableSuiviDepenseEnum.CATEGORIE.name())
			.setCaption(EntetesTableSuiviDepenseEnum.CATEGORIE.getLibelle())
			.setWidth(TAILLE_COLONNE_CATEGORIE)
			.setHidable(true)
			.setResizable(false);
		c2.setRenderer(new TextRenderer(""));
		c2.setStyleGenerator(new GridOperationCellStyle());
		// Pas éditable
		c2.setEditorBinding(binderLD.bindCategories());
		
		Column<LigneOperationVO, CategorieDepense> c3 = addColumn(LigneOperationVO::getSsCategorie);
		c3.setId(EntetesTableSuiviDepenseEnum.SSCATEGORIE.name())
			.setCaption(EntetesTableSuiviDepenseEnum.SSCATEGORIE.getLibelle())
			.setWidth(TAILLE_COLONNE_CATEGORIE)
			.setHidable(true)
			.setResizable(false);
		c3.setRenderer(new TextRenderer(""));
		c3.setStyleGenerator(new GridOperationCellStyle());
		c3.setEditorBinding(binderLD.bindSSCategories());
		
		Column<LigneOperationVO, String> c5 = addColumn(LigneOperationVO::getLibelle);
		c5.setId(EntetesTableSuiviDepenseEnum.LIBELLE.name())
			.setCaption(EntetesTableSuiviDepenseEnum.LIBELLE.getLibelle())
			.setHidable(true)
			.setResizable(false);
		c5.setRenderer(new TextRenderer(""));
		c5.setStyleGenerator(new GridOperationCellStyle());
		// Binding Edition
		c5.setEditorBinding(binderLD.bindLibelle());
			
		Column<LigneOperationVO, TypeDepenseEnum> c6 = addColumn(LigneOperationVO::getTypeDepense);
		c6.setId(EntetesTableSuiviDepenseEnum.TYPE.name())
			.setCaption(EntetesTableSuiviDepenseEnum.TYPE.getLibelle())
			.setWidth(TAILLE_COLONNE_TYPE_MENSUEL)
			.setHidden(true)
			.setHidable(true)
			.setResizable(false);
		c6.setRenderer(new OperationBudgetTypeRenderer());
		c6.setStyleGenerator(new GridOperationCellStyle());
		// Binding Edition	
		c6.setEditorBinding(binderLD.bindTypeDepense());
		
		Column<LigneOperationVO, Double> c7 = addColumn(LigneOperationVO::getValeur);
		c7.setId(EntetesTableSuiviDepenseEnum.VALEUR.name())
			.setCaption(EntetesTableSuiviDepenseEnum.VALEUR.getLibelle())
			.setWidth(TAILLE_COLONNE_VALEUR)
			.setHidable(true)
			.setResizable(false);
		c7.setRenderer(new OperationBudgetTypeRenderer());
		c7.setStyleGenerator(new GridOperationCellValeurStyle());
		c7.setEditorBinding(binderLD.bindValeur());
		
		Column<LigneOperationVO, Boolean> c8 = addColumn(LigneOperationVO::isPeriodique);
		c8.setId(EntetesTableSuiviDepenseEnum.PERIODIQUE.name())
			.setCaption(EntetesTableSuiviDepenseEnum.PERIODIQUE.getLibelle())
			.setWidth(TAILLE_COLONNE_TYPE_MENSUEL)
			.setHidden(true)
			.setHidable(true)
			.setResizable(false);
		c8.setRenderer(new OperationBudgetTypeRenderer());
		c8.setStyleGenerator(new GridOperationCellStyle());
		c8.setEditorBinding(binderLD.bindPeriodique());

		Column<LigneOperationVO, ActionsLigneBudget> c9 = addColumn(LigneOperationVO::getActionsOperation);
		c9.setId(EntetesTableSuiviDepenseEnum.ACTIONS.name())
			.setCaption(EntetesTableSuiviDepenseEnum.ACTIONS.getLibelle())
			.setWidth(TAILLE_COLONNE_ACTIONS)
			.setHidable(true)
			.setResizable(false);
		c9.setStyleGenerator(new GridOperationCellActionsStyle());
		c9.setRenderer(new ComponentRenderer());
		
		Column<LigneOperationVO, Date> c10 = addColumn(LigneOperationVO::getDateMaj);
		c10.setId(EntetesTableSuiviDepenseEnum.DATE_MAJ.name())
			.setCaption(EntetesTableSuiviDepenseEnum.DATE_MAJ.getLibelle())
			.setWidth(TAILLE_COLONNE_DATE + 10D)
			.setHidable(true)
			.setResizable(false);
		c10.setStyleGenerator(new GridOperationCellStyle());
		c10.setRenderer(new DateRenderer(dateFormatMaj));
		c10.setEditorBinding(binderLD.bindDate());
		// Not editable
		
		Column<LigneOperationVO, String> c11 = addColumn(LigneOperationVO::getAuteur);
		c11.setId(EntetesTableSuiviDepenseEnum.AUTEUR.name())
			.setCaption(EntetesTableSuiviDepenseEnum.AUTEUR.getLibelle())
			.setWidth(TAILLE_COLONNE_AUTEUR)
			.setHidden(true)
			.setHidable(true)
			.setResizable(false);
		c11.setRenderer(new TextRenderer(""));
		c11.setStyleGenerator(new GridOperationCellStyle());
		// Not editable		

		getEditor().setEnabled(true);
		
		/**
		 * Context Menu
		 */
		GridContextMenu<LigneOperationVO> contextMenu = new GridContextMenu<>(this);
		GridOperationsRightClickListener menuCommand = new GridOperationsRightClickListener(getControleur());
		contextMenu.addGridBodyContextMenuListener(menuCommand);
	}
}
