package com.terrier.finances.gestion.ui.statistiques.ui;

import com.terrier.finances.gestion.communs.comptes.model.CompteBancaire;
import com.terrier.finances.gestion.ui.communs.abstrait.ui.AbstractUIComponent;
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
 * @deprecated	
 */
@JavaScript({ "http://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js", "highcharts.js", "js_highchart.js"})
@Deprecated
public class StatistiquesPage  extends AbstractUIComponent<StatistiquesController>{

	

	@AutoGenerated
	private VerticalLayout mainLayout;


	@AutoGenerated
	private VerticalLayout chartLayout;


	@AutoGenerated
	private HorizontalLayout menu;


	@AutoGenerated
	private HorizontalLayout horizontalLayout1;


	@AutoGenerated
	private Button buttonDeconnexion;


	@AutoGenerated
	private Button buttonBudget;


	@AutoGenerated
	private ComboBox<CompteBancaire> comboBoxComptes;


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
	public StatistiquesPage(CompteBancaire idCompteSelectionne) {
		buildMainLayout();
		setCompositionRoot(mainLayout);

		// Démarrage
		comboBoxComptes.setEmptySelectionAllowed(false);
		this.compteSelectionne = idCompteSelectionne;
		// Démarrage
		startControleur();
	}
	

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
	public CompteBancaire getCompteSelectionne() {
		return compteSelectionne;
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
		mainLayout.setSizeFull();
		mainLayout.setMargin(true);
		
		// top-level component properties
		setSizeFull();
		
		// menu
		menu = buildMenu();
		mainLayout.addComponent(menu);
		mainLayout.setComponentAlignment(menu, new Alignment(20));
		
		// chartLayout
		chartLayout = new VerticalLayout();
		chartLayout.setSizeFull();
		chartLayout.setMargin(false);
		mainLayout.addComponent(chartLayout);
		mainLayout.setComponentAlignment(chartLayout, new Alignment(48));
		
		return mainLayout;
	}


	@AutoGenerated
	private HorizontalLayout buildMenu() {
		// common part: create layout
		menu = new HorizontalLayout();
		menu.setSizeFull();
		menu.setHeight("40px");
		menu.setMargin(false);
		
		// comboBoxComptes
		comboBoxComptes = new ComboBox<>();
		comboBoxComptes.setWidth("300px");
		comboBoxComptes.setHeight("26px");
		menu.addComponent(comboBoxComptes);
		menu.setComponentAlignment(comboBoxComptes, new Alignment(33));
		
		// horizontalLayout_1
		horizontalLayout1 = buildHorizontalLayout1();
		menu.addComponent(horizontalLayout1);
		menu.setComponentAlignment(horizontalLayout1, new Alignment(34));
		
		return menu;
	}


	@AutoGenerated
	private HorizontalLayout buildHorizontalLayout1() {
		// common part: create layout
		horizontalLayout1 = new HorizontalLayout();
		horizontalLayout1.setSizeUndefined();
		horizontalLayout1.setMargin(false);
		
		// buttonBudget
		buttonBudget = new Button();
		buttonBudget.setCaption("Retour au budget");
		buttonBudget.setSizeUndefined();
		horizontalLayout1.addComponent(buttonBudget);
		horizontalLayout1.setComponentAlignment(buttonBudget,
				new Alignment(34));
		
		// buttonDeconnexion
		buttonDeconnexion = new Button();
		buttonDeconnexion.setStyleName("logout");
		buttonDeconnexion.setCaption("Button");
		buttonDeconnexion.setWidth("30px");
		buttonDeconnexion.setHeight("30px");
		horizontalLayout1.addComponent(buttonDeconnexion);
		horizontalLayout1.setComponentAlignment(buttonDeconnexion,
				new Alignment(33));
		
		return horizontalLayout1;
	}

	
}