package com.terrier.finances.gestion.ui.components.stats;

import com.terrier.finances.gestion.ui.components.abstrait.AbstractUIComponent;
import com.terrier.finances.gestion.ui.controler.stats.StatistiquesController;
import com.vaadin.annotations.AutoGenerated;
import com.vaadin.annotations.JavaScript;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

/**
 * Page des statistiques
 * @author vzwingma
 *
 */
@JavaScript({ "http://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js", "highcharts.js", "js_highchart.js"})
public class StatistiquesPage  extends AbstractUIComponent<StatistiquesController>{

	

	/*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */

	@AutoGenerated
	private VerticalLayout mainLayout;


	@AutoGenerated
	private VerticalLayout chartLayout;


	@AutoGenerated
	private HorizontalLayout menu;


	@AutoGenerated
	private HorizontalLayout horizontalLayout_1;


	@AutoGenerated
	private Button buttonDeconnexion;


	@AutoGenerated
	private Button buttonBudget;


	@AutoGenerated
	private ComboBox comboBoxComptes;


	private String idCompteSelectionne;
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
	public StatistiquesPage() {
		buildMainLayout();
		setCompositionRoot(mainLayout);
		// Démarrage
		startControleur();
	}

	
	/**
	 * Sélection du compte
	 * @param idCompteSelectionne
	 */
	public StatistiquesPage(String idCompteSelectionne) {
		buildMainLayout();
		setCompositionRoot(mainLayout);

		// Démarrage
		comboBoxComptes.setEmptySelectionAllowed(false);
		this.idCompteSelectionne = idCompteSelectionne;
		// Démarrage
		startControleur();
	}
	

	/**
	 * @return the comboBoxComptes
	 */
	public ComboBox getComboBoxComptes() {
		return comboBoxComptes;
	}
	
	
	/**
	 * @return the buttonDeconnexion
	 */
	public Button getButtonDeconnexion() {
		return buttonDeconnexion;
	}
	
	


	/**
	 * @return the buttonBudget
	 */
	public Button getButtonBudget() {
		return buttonBudget;
	}



	@Override
	public StatistiquesController createControleur() {
		return new StatistiquesController(this);
	}



	/**
	 * @return the idCompteSelectionne
	 */
	public String getIdCompteSelectionne() {
		return idCompteSelectionne;
	}


	/**
	 * @return the chartLayout
	 */
	public VerticalLayout getChartLayout() {
		return chartLayout;
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
		
		// chartLayout
		chartLayout = new VerticalLayout();
		chartLayout.setWidth("100.0%");
		chartLayout.setHeight("100.0%");
		chartLayout.setMargin(false);
		mainLayout.addComponent(chartLayout);
		mainLayout.setComponentAlignment(chartLayout, new Alignment(48));
		
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
		comboBoxComptes = new ComboBox();
		comboBoxComptes.setWidth("300px");
		comboBoxComptes.setHeight("26px");
		menu.addComponent(comboBoxComptes);
		menu.setComponentAlignment(comboBoxComptes, new Alignment(33));
		
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
		
		// buttonBudget
		buttonBudget = new Button();
		buttonBudget.setCaption("Retour au budget");
		buttonBudget.setWidth("-1px");
		buttonBudget.setHeight("-1px");
		horizontalLayout_1.addComponent(buttonBudget);
		horizontalLayout_1.setComponentAlignment(buttonBudget,
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

	
}
