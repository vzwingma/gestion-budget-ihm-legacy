package com.terrier.finances.gestion.ui.components.budget.mensuel;

import com.terrier.finances.gestion.model.business.parametrage.CompteBancaire;
import com.terrier.finances.gestion.ui.components.abstrait.AbstractUIComponent;
import com.terrier.finances.gestion.ui.components.budget.mensuel.components.GridResumeTotaux;
import com.terrier.finances.gestion.ui.components.budget.mensuel.components.GridOperations;
import com.terrier.finances.gestion.ui.components.budget.mensuel.components.TreeResumeCategories;
import com.terrier.finances.gestion.ui.controler.budget.mensuel.liste.operations.BudgetMensuelController;
import com.vaadin.annotations.AutoGenerated;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.InlineDateField;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * Page de gestion d'un budet
 * @author vzwingma
 *
 */
public class BudgetMensuelPage extends AbstractUIComponent<BudgetMensuelController> {

	/*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */

	@AutoGenerated
	private VerticalLayout mainLayout;


	@AutoGenerated
	private AbsoluteLayout absoluteLayout_1;


	@AutoGenerated
	private HorizontalLayout budgetMensuel;


	@AutoGenerated
	private VerticalLayout verticalLayoutDepenses;


	@AutoGenerated
	private HorizontalLayout horizontalLayout_2;


	@AutoGenerated
	private Button buttonLock;


	@AutoGenerated
	private Button buttonRefreshMonth;


	@AutoGenerated
	private HorizontalLayout horizontalLayoutActions;


	@AutoGenerated
	private Button buttonCreate;


	@AutoGenerated
	private Button buttonValider;


	@AutoGenerated
	private Button buttonAnnuler;


	@AutoGenerated
	private Button buttonEditer;


	@AutoGenerated
	private GridOperations gridOperations;


	@AutoGenerated
	private VerticalLayout verticalLayoutResume;


	@AutoGenerated
	private GridResumeTotaux tableTotalResume;


	@AutoGenerated
	private TreeResumeCategories treeResume;


	@AutoGenerated
	private HorizontalLayout menu;


	@AutoGenerated
	private HorizontalLayout horizontalLayout_1;


	@AutoGenerated
	private Button buttonDeconnexion;


	@AutoGenerated
	private Label labelLastConnected;


	@AutoGenerated
	private InlineDateField mois;


	@AutoGenerated
	private ComboBox<CompteBancaire> comboBoxComptes;

	//@AutoGenerated
	//private Button buttonStatis;


	private CompteBancaire compteSelectionne;
	/**
	 * 
	 */
	private static final long serialVersionUID = -5059425148801750290L;

	/**
	 * The constructor should first build the main layout, set the
	 * composition root and then do any custom initialization.
	 *
	 * The constructor will not be automatically regenerated by the
	 * visual editor.
	 */
	public BudgetMensuelPage() {
		buildMainLayout();
		setCompositionRoot(mainLayout);

		// Démarrage
		startControleur();
	}

	/**
	 * The constructor should first build the main layout, set the
	 * composition root and then do any custom initialization.
	 *
	 * The constructor will not be automatically regenerated by the
	 * visual editor.
	 */
	public BudgetMensuelPage(CompteBancaire compteSelectionne) {
		buildMainLayout();
		setCompositionRoot(mainLayout);
		this.compteSelectionne = compteSelectionne;
		// Démarrage
		startControleur();
	}

	
	/**
	 * @return the buttonLock
	 */
	public Button getButtonLock() {
		return buttonLock;
	}



	/**
	 * @return the buttonRefreshMonth
	 */
	public Button getButtonRefreshMonth() {
		return buttonRefreshMonth;
	}



	/**
	 * @param buttonLock the buttonLock to set
	 */
	public void setButtonLock(Button buttonLock) {
		this.buttonLock = buttonLock;
	}



	/**
	 * @return the tableSuiviDepense
	 */
	public GridOperations getGridOperations() {
		return gridOperations;
	}

	/**
	 * @return the treeResume
	 */
	public TreeResumeCategories getTreeResume() {
		return treeResume;
	}

	/**
	 * @return the inlineDateField_1
	 */
	public InlineDateField getMois() {
		return mois;
	}
	
	
	/**
	 * @return the tableTotalResume
	 */
	public GridResumeTotaux getGridResumeTotaux() {
		return tableTotalResume;
	}

	/**
	 * @return the buttonCreate
	 */
	public Button getButtonCreate() {
		return buttonCreate;
	}

	/**
	 * @return the buttonAnnuler
	 */
	public Button getButtonAnnuler() {
		return buttonAnnuler;
	}

	/**
	 * @return the buttonValider
	 */
	public Button getButtonValider() {
		return buttonValider;
	}

	/**
	 * @return the buttonEditer
	 */
	public Button getButtonEditer() {
		return buttonEditer;
	}

	/**
	 * @return the button_2
	
	public Button getButtonStatistique() {
		return buttonStatis;
	}
	 */
	
	/**
	 * @return the comboBoxComptes
	 */
	public ComboBox<CompteBancaire> getComboBoxComptes() {
		return comboBoxComptes;
	}
	
	
	/**
	 * @return the buttonDeconnexion
	 */
	public Button getButtonDeconnexion() {
		return buttonDeconnexion;
	}

	@Override
	public BudgetMensuelController createControleur() {
		return new BudgetMensuelController(this);
	}

	/**
	 * @return the idCompteSelectionne
	 */
	public CompteBancaire getCompteSelectionne() {
		return compteSelectionne;
	}

	/**
	 * @return the labelLastConnected
	 */
	public Label getLabelLastConnected() {
		return labelLastConnected;
	}

	@AutoGenerated
	private VerticalLayout buildMainLayout() {
		// common part: create layout
		mainLayout = new VerticalLayout();
		mainLayout.setWidth("100%");
		mainLayout.setHeight("100%");
		mainLayout.setMargin(true);
		
		// top-level component properties
		setWidth("100.0%");
		setHeight("100.0%");
		
		// menu
		menu = buildMenu();
		mainLayout.addComponent(menu);
		mainLayout.setComponentAlignment(menu, new Alignment(20));
		
		// budgetMensuel
		budgetMensuel = buildBudgetMensuel();
		mainLayout.addComponent(budgetMensuel);
		mainLayout.setExpandRatio(budgetMensuel, 1.0f);
		mainLayout.setComponentAlignment(budgetMensuel, new Alignment(48));
		
		// absoluteLayout_1
		absoluteLayout_1 = new AbsoluteLayout();
		absoluteLayout_1.setWidth("-1px");
		absoluteLayout_1.setHeight("-1px");
		mainLayout.addComponent(absoluteLayout_1);
		
		return mainLayout;
	}

	@AutoGenerated
	private HorizontalLayout buildMenu() {
		// common part: create layout
		menu = new HorizontalLayout();
		menu.setWidth("100.0%");
		menu.setHeight("40px");
		menu.setMargin(false);
		
		// comboBoxComptes
		comboBoxComptes = new ComboBox<CompteBancaire>();
		comboBoxComptes.setWidth("300px");
		comboBoxComptes.setHeight("26px");
		menu.addComponent(comboBoxComptes);
		menu.setComponentAlignment(comboBoxComptes, new Alignment(33));
		
		// mois
		mois = new InlineDateField();
		mois.setWidth("-1px");
		mois.setHeight("-1px");
		menu.addComponent(mois);
		menu.setComponentAlignment(mois, new Alignment(48));
		
		// horizontalLayout_1
		horizontalLayout_1 = buildHorizontalLayout_1();
		menu.addComponent(horizontalLayout_1);
		menu.setComponentAlignment(horizontalLayout_1, new Alignment(34));
		
		return menu;
	}

	@AutoGenerated
	private HorizontalLayout buildHorizontalLayout_1() {
		// common part: create layout
		horizontalLayout_1 = new HorizontalLayout();
		horizontalLayout_1.setWidth("-1px");
		horizontalLayout_1.setHeight("-1px");
		horizontalLayout_1.setMargin(false);
		horizontalLayout_1.setSpacing(true);
		
		// labelLastConnected
		labelLastConnected = new Label();
		labelLastConnected.setWidth("-1px");
		labelLastConnected.setHeight("-1px");
		labelLastConnected.setValue("Label");
		horizontalLayout_1.addComponent(labelLastConnected);
		horizontalLayout_1.setComponentAlignment(labelLastConnected,
				new Alignment(34));
		
		// buttonDeconnexion
		buttonDeconnexion = new Button();
		buttonDeconnexion.setStyleName("logout");
		buttonDeconnexion.setCaption("Button");
		buttonDeconnexion.setWidth("30px");
		buttonDeconnexion.setHeight("30px");
		horizontalLayout_1.addComponent(buttonDeconnexion);
		horizontalLayout_1.setComponentAlignment(buttonDeconnexion,
				new Alignment(33));
		
		return horizontalLayout_1;
	}

	@AutoGenerated
	private HorizontalLayout buildBudgetMensuel() {
		// common part: create layout
		budgetMensuel = new HorizontalLayout();
		budgetMensuel.setWidth("100.0%");
		budgetMensuel.setHeight("100.0%");
		budgetMensuel.setMargin(false);
		
		// verticalLayoutResume
		verticalLayoutResume = buildVerticalLayoutResume();
		budgetMensuel.addComponent(verticalLayoutResume);
		budgetMensuel.setExpandRatio(verticalLayoutResume, 1.0f);
		budgetMensuel.setComponentAlignment(verticalLayoutResume,
				new Alignment(33));
		
		// verticalLayoutDepenses
		verticalLayoutDepenses = buildVerticalLayoutDepenses();
		budgetMensuel.addComponent(verticalLayoutDepenses);
		budgetMensuel.setExpandRatio(verticalLayoutDepenses, 2.5f);
		
		return budgetMensuel;
	}

	@AutoGenerated
	private VerticalLayout buildVerticalLayoutResume() {
		// common part: create layout
		verticalLayoutResume = new VerticalLayout();
		verticalLayoutResume.setWidth("95.0%");
		verticalLayoutResume.setHeight("100.0%");
		verticalLayoutResume.setMargin(false);
		
		// treeResume
		treeResume = new TreeResumeCategories();
		treeResume.setWidth("100.0%");
		treeResume.setHeight("100.0%");
		verticalLayoutResume.addComponent(treeResume);
		verticalLayoutResume.setExpandRatio(treeResume, 1.0f);
		
		// tableTotalResume
		tableTotalResume = new GridResumeTotaux();
		tableTotalResume.setWidth("100.0%");
		tableTotalResume.setHeight("100px");
		verticalLayoutResume.addComponent(tableTotalResume);
		
		return verticalLayoutResume;
	}

	@AutoGenerated
	private VerticalLayout buildVerticalLayoutDepenses() {
		// common part: create layout
		verticalLayoutDepenses = new VerticalLayout();
		verticalLayoutDepenses.setWidth("100.0%");
		verticalLayoutDepenses.setHeight("100.0%");
		verticalLayoutDepenses.setMargin(false);
		
		// tableSuiviDepense
		gridOperations = new GridOperations();
		gridOperations.setWidth("100.0%");
		gridOperations.setHeight("100.0%");
		verticalLayoutDepenses.addComponent(gridOperations);
		verticalLayoutDepenses.setExpandRatio(gridOperations, 40.0f);
		verticalLayoutDepenses.setComponentAlignment(gridOperations,
				new Alignment(20));
		
		// horizontalLayout_2
		horizontalLayout_2 = buildHorizontalLayout_2();
		verticalLayoutDepenses.addComponent(horizontalLayout_2);
		
		return verticalLayoutDepenses;
	}

	@AutoGenerated
	private HorizontalLayout buildHorizontalLayout_2() {
		// common part: create layout
		horizontalLayout_2 = new HorizontalLayout();
		horizontalLayout_2.setWidth("100.0%");
		horizontalLayout_2.setHeight("-1px");
		horizontalLayout_2.setMargin(false);
		
		// horizontalLayoutActions
		horizontalLayoutActions = buildHorizontalLayoutActions();
		horizontalLayout_2.addComponent(horizontalLayoutActions);
		horizontalLayout_2.setExpandRatio(horizontalLayoutActions, 200.0f);
		horizontalLayout_2.setComponentAlignment(horizontalLayoutActions,
				new Alignment(48));
		
		// buttonRefreshMonth
		buttonRefreshMonth = new Button();
		buttonRefreshMonth.setStyleName("reinit");
		buttonRefreshMonth.setCaption(" ");
		buttonRefreshMonth.setDescription("Réinitialiser le mois courant");
		buttonRefreshMonth.setWidth("30px");
		buttonRefreshMonth.setHeight("30px");
		horizontalLayout_2.addComponent(buttonRefreshMonth);
		horizontalLayout_2.setComponentAlignment(buttonRefreshMonth,
				new Alignment(34));
		
		// buttonLock
		buttonLock = new Button();
		buttonLock.setCaption(" ");
		buttonLock.setWidth("30px");
		buttonLock.setHeight("30px");
		horizontalLayout_2.addComponent(buttonLock);
		horizontalLayout_2.setComponentAlignment(buttonLock, new Alignment(34));
		
		return horizontalLayout_2;
	}

	@AutoGenerated
	private HorizontalLayout buildHorizontalLayoutActions() {
		// common part: create layout
		horizontalLayoutActions = new HorizontalLayout();
		horizontalLayoutActions.setWidth("40.0%");
		horizontalLayoutActions.setHeight("40px");
		horizontalLayoutActions.setMargin(false);
		
		// buttonEditer
		buttonEditer = new Button();
		buttonEditer.setStyleName("primary");
		buttonEditer.setCaption("Modifier les opérations");
		buttonEditer.setWidth("-1px");
		buttonEditer.setHeight("-1px");
		horizontalLayoutActions.addComponent(buttonEditer);
		horizontalLayoutActions.setComponentAlignment(buttonEditer,
				new Alignment(48));
		
		// buttonAnnuler
		buttonAnnuler = new Button();
		buttonAnnuler.setStyleName("danger");
		buttonAnnuler.setCaption("Annuler");
		buttonAnnuler.setWidth("-1px");
		buttonAnnuler.setHeight("-1px");
		horizontalLayoutActions.addComponent(buttonAnnuler);
		horizontalLayoutActions.setComponentAlignment(buttonAnnuler,
				new Alignment(48));
		
		// buttonValider
		buttonValider = new Button();
		buttonValider.setStyleName("friendly");
		buttonValider.setCaption("Valider");
		buttonValider.setWidth("-1px");
		buttonValider.setHeight("-1px");
		horizontalLayoutActions.addComponent(buttonValider);
		horizontalLayoutActions.setComponentAlignment(buttonValider,
				new Alignment(48));
		
		// buttonCreate
		buttonCreate = new Button();
		buttonCreate.setStyleName("friendly");
		buttonCreate.setCaption("Créer une nouvelle dépense");
		buttonCreate.setWidth("-1px");
		buttonCreate.setHeight("-1px");
		buttonCreate.setTabIndex(2);
		horizontalLayoutActions.addComponent(buttonCreate);
		horizontalLayoutActions.setComponentAlignment(buttonCreate,
				new Alignment(48));
		
		return horizontalLayoutActions;
	}
}
