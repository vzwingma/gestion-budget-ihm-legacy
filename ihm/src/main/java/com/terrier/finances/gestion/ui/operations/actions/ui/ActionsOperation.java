package com.terrier.finances.gestion.ui.operations.actions.ui;

import com.terrier.finances.gestion.communs.operations.model.LigneOperation;
import com.terrier.finances.gestion.ui.communs.abstrait.ui.AbstractUIComponent;
import com.terrier.finances.gestion.ui.operations.actions.ui.listeners.ActionsOperationClickListener;
import com.vaadin.annotations.AutoGenerated;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;

/**
 * Liste des actions sur une ligne de budget
 * @author vzwingma
 *
 */
public class ActionsOperation extends AbstractUIComponent<ActionsOperationController> {

	@AutoGenerated
	private HorizontalLayout mainLayout;
	@AutoGenerated
	private Button buttonReporter;
	@AutoGenerated
	private Button buttonAnnuler;
	@AutoGenerated
	private Button buttonPrevue;
	@AutoGenerated
	private Button buttonReel;
	@AutoGenerated
	private Button buttonSupprime;
	//
	private static final long serialVersionUID = -1624090010081839745L;
	/**
	 * The constructor should first build the main layout, set the
	 * composition root and then do any custom initialization.
	 *
	 * The constructor will not be automatically regenerated by the
	 * visual editor.
	 */
	public ActionsOperation(LigneOperation operation) {
		buildMainLayout();
		setCompositionRoot(mainLayout);
		// Démarrage
		startControleur();
		completeLayout();
		// Injection de l'id de l'opération
		getControleur().setOperation(operation);
	}

	/**
	 * @return the buttonReporter
	 */
	public Button getButtonReporter() {
		return buttonReporter;
	}

	/**
	 * @return the buttonAnnuler
	 */
	public Button getButtonAnnuler() {
		return buttonAnnuler;
	}

	/**
	 * @return the buttonReel
	 */
	public Button getButtonReel() {
		return buttonReel;
	}

	

	/**
	 * @return the buttonSupprime
	 */
	public Button getButtonSupprimer() {
		return buttonSupprime;
	}

	/**
	 * @return the buttonPrevue
	 */
	public Button getButtonPrevue() {
		return buttonPrevue;
	}

	@AutoGenerated
	private HorizontalLayout buildMainLayout() {
		// common part: create layout
		mainLayout = new HorizontalLayout();
		mainLayout.setWidth("100%");
		mainLayout.setHeight("100%");
		mainLayout.setMargin(false);
		
		// top-level component properties
		setWidth("100.0%");
		setHeight("100.0%");
		 CssLayout group = new CssLayout();
	        group.addStyleName("v-component-group");
	        mainLayout.addComponent(group);

		// buttonReel
		buttonReel = new Button();
		buttonReel.setCaption(" ");
		buttonReel.setWidth("-1px");
		buttonReel.setHeight("-1px");
		group.addComponent(buttonReel);
		
		// buttonPrevue
		buttonPrevue = new Button();
		buttonPrevue.setCaption(" ");
		buttonPrevue.setWidth("-1px");
		buttonPrevue.setHeight("-1px");
		group.addComponent(buttonPrevue);
		
		// buttonAnnuler
		buttonAnnuler = new Button();
		buttonAnnuler.setCaption(" ");
		buttonAnnuler.setWidth("-1px");
		buttonAnnuler.setHeight("-1px");
		group.addComponent(buttonAnnuler);
		
		// buttonReporter
		buttonReporter = new Button();
		buttonReporter.setCaption(" ");
		buttonReporter.setWidth("-1px");
		buttonReporter.setHeight("-1px");
		group.addComponent(buttonReporter);

		
		// buttonSupprime
		buttonSupprime = new Button();
		buttonSupprime.setCaption(" ");
		buttonSupprime.setWidth("-1px");
		buttonSupprime.setHeight("-1px");
		group.addComponent(buttonSupprime);
		
		return mainLayout;
	}

	
	/**
	 * Complétion du layout avec les éléments non auto générés
	 */
	private void completeLayout(){
		getButtonReel().addClickListener(new ActionsOperationClickListener());
		getButtonReel().setId("buttonReel");
		getButtonReel().setStyleName("v-button-actions v-button-reel");
		getButtonReel().setDescription("Enregistrement de l'opération");

		getButtonAnnuler().addClickListener(new ActionsOperationClickListener());
		getButtonAnnuler().setId("buttonAnnuler");
		getButtonAnnuler().setStyleName("v-button-actions v-button-annuler");
		getButtonAnnuler().setDescription("Annulation de l'opération");


		getButtonReporter().addClickListener(new ActionsOperationClickListener());
		getButtonReporter().setId("buttonReporter");
		getButtonReporter().setStyleName("v-button-actions v-button-reporter");
		getButtonReporter().setDescription("Report de l'opération au mois prochain");

		getButtonPrevue().addClickListener(new ActionsOperationClickListener());
		getButtonPrevue().setId("buttonPrevue");
		getButtonPrevue().setStyleName("v-button-actions v-button-prevue");
		getButtonPrevue().setDescription("Mise en prévision de l'opération");

		getButtonSupprimer().addClickListener(new ActionsOperationClickListener());
		getButtonSupprimer().setId("buttonSupprimer");
		getButtonSupprimer().setStyleName("v-button-actions v-button-supprimer");
		getButtonSupprimer().setDescription("Suppression de l'opération");		
	}
	
	/* (non-Javadoc)
	 * @see com.terrier.finances.gestion.ui.components.AbstractUIComponent#getControleur()
	 */
	@Override
	public ActionsOperationController createControleur() {
		return new ActionsOperationController(this);
	}	
	
}