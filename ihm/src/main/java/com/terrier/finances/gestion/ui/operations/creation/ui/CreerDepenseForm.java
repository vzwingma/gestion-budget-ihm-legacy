package com.terrier.finances.gestion.ui.operations.creation.ui;

import java.util.Optional;

import com.terrier.finances.gestion.communs.comptes.model.CompteBancaire;
import com.terrier.finances.gestion.communs.operations.model.enums.EtatOperationEnum;
import com.terrier.finances.gestion.communs.operations.model.enums.TypeOperationEnum;
import com.terrier.finances.gestion.communs.parametrages.model.CategorieDepense;
import com.terrier.finances.gestion.ui.communs.abstrait.ui.AbstractUIComponent;
import com.terrier.finances.gestion.ui.comptes.ui.styles.ComptesItemCaptionStyle;
import com.terrier.finances.gestion.ui.operations.creation.listeners.ActionValiderCreationDepenseClickListener;
import com.terrier.finances.gestion.ui.operations.creation.listeners.SelectionCategorieValueChangeListener;
import com.terrier.finances.gestion.ui.operations.creation.listeners.SelectionSousCategorieValueChangeListener;
import com.vaadin.annotations.AutoGenerated;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.ComboBox.NewItemProvider;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;

/**
 * Formulaire de création d'une dépense
 * @author vzwingma
 *
 */
public class CreerDepenseForm extends AbstractUIComponent<CreerDepenseController> implements NewItemProvider<String> {


	@AutoGenerated
	private HorizontalLayout mainLayout;

	@AutoGenerated
	private GridLayout gridLayout;

	@AutoGenerated
	private HorizontalLayout horizontalLayout7;

	@AutoGenerated
	private Button buttonValiderContinuer;

	@AutoGenerated
	private Button buttonValider;

	@AutoGenerated
	private HorizontalLayout horizontalLayout6;

	@AutoGenerated
	private CheckBox checkBoxPeriodique;

	@AutoGenerated
	private Label labelPeriode;

	@AutoGenerated
	private HorizontalLayout horizontalLayout5;

	@AutoGenerated
	private ComboBox<EtatOperationEnum> comboBoxEtat;

	@AutoGenerated
	private Label labelEtat;

	@AutoGenerated
	private HorizontalLayout horizontalLayout4;

	@AutoGenerated
	private Label label3;

	@AutoGenerated
	private TextField textFieldValeur;

	@AutoGenerated
	private ComboBox<TypeOperationEnum> comboBoxType;

	@AutoGenerated
	private Label label4;

	@AutoGenerated
	private HorizontalLayout horizontalLayout3;

	@AutoGenerated
	private ComboBox<String> textFieldDescription;

	@AutoGenerated
	private Label label2;

	@AutoGenerated
	private HorizontalLayout horizontalLayout8;

	@AutoGenerated
	private ComboBox<CompteBancaire> comboboxComptes;

	@AutoGenerated
	private Label label5;

	@AutoGenerated
	private HorizontalLayout horizontalLayout2;

	@AutoGenerated
	private ComboBox<CategorieDepense> comboBoxSsCategorie;

	@AutoGenerated
	private ComboBox<CategorieDepense> comboBoxCategorie;

	@AutoGenerated
	private Label label1;

	/**
	 * 
	 */
	private static final long serialVersionUID = 7011560205719985290L;


	/**
	 * The constructor should first build the main layout, set the
	 * composition root and then do any custom initialization.
	 *
	 * The constructor will not be automatically regenerated by the
	 * visual editor.
	 */
	public CreerDepenseForm() {
		buildMainLayout();
		setCompositionRoot(mainLayout);
		// Start controleur
		startControleur();
		
		completeLayout();
	}

	

	
	/**
	 * @return the listSelectEtat
	 */
	public ComboBox<EtatOperationEnum> getComboboxEtat() {
		return comboBoxEtat;
	}


	/**
	 * @return the textFieldValeur
	 */
	public TextField getTextFieldValeur() {
		return textFieldValeur;
	}


	/**
	 * @return the comboBoxType
	 */
	public ComboBox<TypeOperationEnum> getComboboxType() {
		return comboBoxType;
	}


	/**
	 * @return the textFieldDescription
	 */
	public ComboBox<String> getTextFieldDescription() {
		return textFieldDescription;
	}


	/**
	 * @return the label_5
	 */
	public Label getLabelCompte() {
		return label5;
	}




	/**
	 * @return the horizontalLayout_8
	 */
	public HorizontalLayout getLayoutCompte() {
		return horizontalLayout8;
	}




	/**
	 * @return the comboBoxSsCategorie
	 */
	public ComboBox<CategorieDepense> getComboBoxSsCategorie() {
		return comboBoxSsCategorie;
	}


	/**
	 * @return the textFieldCategorie
	 */
	public ComboBox<CategorieDepense> getComboBoxCategorie() {
		return comboBoxCategorie;
	}


	/**
	 * @return the checkBoxPeriodique
	 */
	public CheckBox getCheckBoxPeriodique() {
		return checkBoxPeriodique;
	}


	/**
	 * @return the buttonValider
	 */
	public Button getButtonValider() {
		return buttonValider;
	}

	



	/**
	 * @return the listSelectComptes
	 */
	public ComboBox<CompteBancaire> getComboboxComptes() {
		return comboboxComptes;
	}




	/**
	 * @return the buttonValiderContinuer
	 */
	public Button getButtonValiderContinuer() {
		return buttonValiderContinuer;
	}




	/**
	 * @return the labelEtat
	 */
	public Label getLabelEtat() {
		return labelEtat;
	}




	/* (non-Javadoc)
	 * @see com.terrier.finances.gestion.ui.components.AbstractUIComponent#getControleur()
	 */
	@Override
	public CreerDepenseController createControleur() {
		return new CreerDepenseController(this);
	}




	@AutoGenerated
	private HorizontalLayout buildMainLayout() {
		// common part: create layout
		mainLayout = new HorizontalLayout();
		mainLayout.setSizeFull();
		mainLayout.setMargin(false);
		
		// top-level component properties
		setSizeFull();
		
		// gridLayout
		gridLayout = buildGridLayout();
		mainLayout.addComponent(gridLayout);
		mainLayout.setExpandRatio(gridLayout, 1.0f);
		mainLayout.setComponentAlignment(gridLayout, new Alignment(48));
		
		return mainLayout;
	}




	@AutoGenerated
	private GridLayout buildGridLayout() {
		// common part: create layout
		gridLayout = new GridLayout();
		gridLayout.setWidth("500px");
		gridLayout.setHeight("270px");
		gridLayout.setMargin(false);
		gridLayout.setColumns(2);
		gridLayout.setRows(7);
		
		// label_1
		label1 = new Label();
		label1.setSizeUndefined();
		label1.setValue("Catégorie");
		gridLayout.addComponent(label1, 0, 0);
		gridLayout.setComponentAlignment(label1, new Alignment(33));
		
		// horizontalLayout_2
		horizontalLayout2 = buildHorizontalLayout2();
		gridLayout.addComponent(horizontalLayout2, 1, 0);
		gridLayout.setComponentAlignment(horizontalLayout2, new Alignment(48));
		
		// label_5
		label5 = new Label();
		label5.setSizeUndefined();
		label5.setValue("Compte");
		gridLayout.addComponent(label5, 0, 1);
		gridLayout.setComponentAlignment(label5, new Alignment(33));
		
		// horizontalLayout_8
		horizontalLayout8 = buildHorizontalLayout8();
		gridLayout.addComponent(horizontalLayout8, 1, 1);
		gridLayout.setComponentAlignment(horizontalLayout8, new Alignment(48));
		
		// label_2
		label2 = new Label();
		label2.setSizeFull();
		label2.setHeightUndefined();
		label2.setValue("Description");
		gridLayout.addComponent(label2, 0, 2);
		gridLayout.setComponentAlignment(label2, new Alignment(33));
		
		// horizontalLayout_3
		horizontalLayout3 = buildHorizontalLayout3();
		gridLayout.addComponent(horizontalLayout3, 1, 2);
		gridLayout.setComponentAlignment(horizontalLayout3, new Alignment(34));
		
		// label_4
		label4 = new Label();
		label4.setSizeUndefined();
		label4.setValue("Valeur");
		gridLayout.addComponent(label4, 0, 3);
		gridLayout.setComponentAlignment(label4, new Alignment(33));
		
		// horizontalLayout_4
		horizontalLayout4 = buildHorizontalLayout4();
		gridLayout.addComponent(horizontalLayout4, 1, 3);
		gridLayout.setComponentAlignment(horizontalLayout4, new Alignment(48));
		
		// labelEtat
		labelEtat = new Label();
		labelEtat.setSizeUndefined();
		labelEtat.setValue("Etat");
		gridLayout.addComponent(labelEtat, 0, 4);
		gridLayout.setComponentAlignment(labelEtat, new Alignment(33));
		
		// horizontalLayout_5
		horizontalLayout5 = buildHorizontalLayout5();
		gridLayout.addComponent(horizontalLayout5, 1, 4);
		gridLayout.setComponentAlignment(horizontalLayout5, new Alignment(48));
		
		// labelPeriode
		labelPeriode = new Label();
		labelPeriode.setWidth("120px");
		labelPeriode.setHeight("-1px");
		labelPeriode.setValue("Dépense mensuelle");
		gridLayout.addComponent(labelPeriode, 0, 5);
		gridLayout.setComponentAlignment(labelPeriode, new Alignment(33));
		
		// horizontalLayout_6
		horizontalLayout6 = buildHorizontalLayout6();
		gridLayout.addComponent(horizontalLayout6, 1, 5);
		gridLayout.setComponentAlignment(horizontalLayout6, new Alignment(33));
		
		// horizontalLayout_7
		horizontalLayout7 = buildHorizontalLayout7();
		gridLayout.addComponent(horizontalLayout7, 1, 6);
		gridLayout.setComponentAlignment(horizontalLayout7, new Alignment(33));
		
		return gridLayout;
	}




	@AutoGenerated
	private HorizontalLayout buildHorizontalLayout2() {
		// common part: create layout
		horizontalLayout2 = new HorizontalLayout();
		horizontalLayout2.setSizeFull();
		horizontalLayout2.setHeight("40px");
		horizontalLayout2.setMargin(false);
		
		// comboBoxCategorie
		comboBoxCategorie = new ComboBox<>();
		comboBoxCategorie.setSizeFull();
		comboBoxCategorie.setHeight("26px");
		comboBoxCategorie.setRequiredIndicatorVisible(true);
		horizontalLayout2.addComponent(comboBoxCategorie);
		horizontalLayout2.setExpandRatio(comboBoxCategorie, 1.0f);
		horizontalLayout2.setComponentAlignment(comboBoxCategorie,
				new Alignment(33));
		
		// comboBoxSsCategorie
		comboBoxSsCategorie = new ComboBox<>();
		comboBoxSsCategorie.setEnabled(false);
		comboBoxSsCategorie.setSizeFull();
		comboBoxSsCategorie.setHeight("26px");
		comboBoxSsCategorie.setRequiredIndicatorVisible(true);
		horizontalLayout2.addComponent(comboBoxSsCategorie);
		horizontalLayout2.setExpandRatio(comboBoxSsCategorie, 1.0f);
		horizontalLayout2.setComponentAlignment(comboBoxSsCategorie,
				new Alignment(33));
		
		return horizontalLayout2;
	}




	@AutoGenerated
	private HorizontalLayout buildHorizontalLayout8() {
		// common part: create layout
		horizontalLayout8 = new HorizontalLayout();
		horizontalLayout8.setSizeFull();
		horizontalLayout8.setHeight("40px");
		horizontalLayout8.setMargin(false);
		
		// listSelectComptes
		comboboxComptes = new ComboBox<>();
		comboboxComptes.setWidth("183px");
		comboboxComptes.setHeight("-1px");
		comboboxComptes.setRequiredIndicatorVisible(true);
		horizontalLayout8.addComponent(comboboxComptes);
		horizontalLayout8.setComponentAlignment(comboboxComptes,
				new Alignment(34));
		
		return horizontalLayout8;
	}




	@AutoGenerated
	private HorizontalLayout buildHorizontalLayout3() {
		// common part: create layout
		horizontalLayout3 = new HorizontalLayout();
		horizontalLayout3.setWidthUndefined();
		horizontalLayout3.setHeight("40px");
		horizontalLayout3.setMargin(false);
		
		// textFieldDescription
		textFieldDescription = new ComboBox<>();
		textFieldDescription.setWidth("370px");
		textFieldDescription.setRequiredIndicatorVisible(true);
		horizontalLayout3.addComponent(textFieldDescription);	
		horizontalLayout3.setComponentAlignment(textFieldDescription,
				new Alignment(33));
		return horizontalLayout3;
	}




	@AutoGenerated
	private HorizontalLayout buildHorizontalLayout4() {
		// common part: create layout
		horizontalLayout4 = new HorizontalLayout();
		horizontalLayout4.setSizeFull();
		horizontalLayout4.setHeight("40px");
		horizontalLayout4.setMargin(false);
		
		// comboBoxType
		comboBoxType = new ComboBox<>();
		comboBoxType.setWidthUndefined();
		comboBoxType.setHeight("30px");
		comboBoxType.setRequiredIndicatorVisible(true);
		horizontalLayout4.addComponent(comboBoxType);
		horizontalLayout4.setComponentAlignment(comboBoxType,
				new Alignment(33));
		
		// textFieldValeur
		textFieldValeur = new TextField();
		textFieldValeur.setWidth("210px");
		textFieldValeur.setHeightUndefined();
		textFieldValeur.setRequiredIndicatorVisible(true);
		horizontalLayout4.addComponent(textFieldValeur);
		horizontalLayout4.setExpandRatio(textFieldValeur, 1.0f);
		horizontalLayout4.setComponentAlignment(textFieldValeur,
				new Alignment(34));
		
		// label_3
		label3 = new Label();
		label3.setSizeUndefined();
		label3.setValue("€");
		horizontalLayout4.addComponent(label3);
		horizontalLayout4.setComponentAlignment(label3, new Alignment(34));
		
		return horizontalLayout4;
	}




	@AutoGenerated
	private HorizontalLayout buildHorizontalLayout5() {
		// common part: create layout
		horizontalLayout5 = new HorizontalLayout();
		horizontalLayout5.setWidthUndefined();
		horizontalLayout5.setHeight("40px");
		horizontalLayout5.setMargin(false);
		
		// listSelectEtat
		comboBoxEtat = new ComboBox<>();
		comboBoxEtat.setWidth("370px");
		comboBoxEtat.setHeight("30px");
		comboBoxEtat.setRequiredIndicatorVisible(true);
		horizontalLayout5.addComponent(comboBoxEtat);
		horizontalLayout5.setComponentAlignment(comboBoxEtat, new Alignment(
				33));
		
		return horizontalLayout5;
	}




	@AutoGenerated
	private HorizontalLayout buildHorizontalLayout6() {
		// common part: create layout
		horizontalLayout6 = new HorizontalLayout();
		horizontalLayout6.setWidthUndefined();
		horizontalLayout6.setHeight("40px");
		horizontalLayout6.setMargin(false);
		
		// checkBoxPeriodique
		checkBoxPeriodique = new CheckBox();
		checkBoxPeriodique.setStyleName("color2");
		checkBoxPeriodique.setCaption("CheckBox");
		checkBoxPeriodique.setSizeUndefined();
		horizontalLayout6.addComponent(checkBoxPeriodique);
		horizontalLayout6.setComponentAlignment(checkBoxPeriodique,
				new Alignment(33));
		
		return horizontalLayout6;
	}




	@AutoGenerated
	private HorizontalLayout buildHorizontalLayout7() {
		// common part: create layout
		horizontalLayout7 = new HorizontalLayout();
		horizontalLayout7.setWidthUndefined();
		horizontalLayout7.setHeight("40px");
		horizontalLayout7.setMargin(false);
		
		// buttonValider
		buttonValider = new Button();
		buttonValider.setStyleName("primary");
		buttonValider.setCaption("Valider et Fermer");
		buttonValider.setSizeUndefined();
		horizontalLayout7.addComponent(buttonValider);
		horizontalLayout7.setComponentAlignment(buttonValider, new Alignment(
				48));
		
		// buttonValiderContinuer
		buttonValiderContinuer = new Button();
		buttonValiderContinuer.setStyleName("friendly");
		buttonValiderContinuer.setCaption("Valider et Continuer");
		buttonValiderContinuer.setSizeUndefined();
		horizontalLayout7.addComponent(buttonValiderContinuer);
		horizontalLayout7.setComponentAlignment(buttonValiderContinuer,
				new Alignment(48));
		
		return horizontalLayout7;
	}
	
	/**
	 * Complétion du layout
	 */
	private void completeLayout(){
		getComboBoxCategorie().setEmptySelectionAllowed(false);
		getComboBoxCategorie().setTextInputAllowed(false);		
		getComboBoxCategorie().addSelectionListener(new SelectionCategorieValueChangeListener(getControleur()));
		
		getComboBoxSsCategorie().setEmptySelectionAllowed(false);
		getComboBoxSsCategorie().setTextInputAllowed(false);
		getComboBoxSsCategorie().addSelectionListener(new SelectionSousCategorieValueChangeListener(getControleur()));

		getComboboxComptes().setItemCaptionGenerator(new ComptesItemCaptionStyle());

		getCheckBoxPeriodique().setDescription("Cocher pour une dépense mensuelle");
		getTextFieldDescription().setNewItemProvider(this);
		
		// Bouton
		getButtonValider().addClickListener(new ActionValiderCreationDepenseClickListener());
		getButtonValider().setDescription("Valider l'opération et fermer l'écran de saisie");
		getButtonValiderContinuer().addClickListener(new ActionValiderCreationDepenseClickListener());
		getButtonValiderContinuer().setDescription("Valider l'opération et Créer une nouvelle opération");
	}
	
	/* (non-Javadoc)
	 * @see java.util.function.Function#apply(java.lang.Object)
	 */
	@Override
	public Optional<String> apply(String t) {
		getTextFieldDescription().setSelectedItem(t);
		return Optional.of(t);
	}
}
