package com.terrier.finances.gestion.ui.statistiques.ui;

import java.io.Serializable;

import com.terrier.finances.gestion.communs.comptes.model.CompteBancaire;
import com.terrier.finances.gestion.ui.budget.listeners.ActionDeconnexionClickListener;
import com.terrier.finances.gestion.ui.budget.ui.BudgetMensuelPage;
import com.terrier.finances.gestion.ui.communs.abstrait.ui.AbstractUIController;
import com.terrier.finances.gestion.ui.statistiques.listeners.ChangePageListener;
import com.vaadin.ui.ComboBox;

/**
 * Controleur de la page des statistiques
 * @author vzwingma
 * @deprecated
 */
@Deprecated
public class StatistiquesController extends AbstractUIController<StatistiquesPage> implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6410285178655721867L;

	@Deprecated
	private ComboBox<CompteBancaire> compte;
	
	/**
	 * Constructure du Controleur du composant
	 * @param composant
	 */
	public StatistiquesController(StatistiquesPage composant) {
		super(composant);
	}


	@Deprecated
	public void initDynamicComponentsOnPage() {

		// Démarrage
		getComponent().getComboBoxComptes().setEmptySelectionAllowed(false);
		
		getComponent().getButtonDeconnexion().setCaption("");
		getComponent().getButtonDeconnexion().addClickListener(new ActionDeconnexionClickListener());
		getComponent().getButtonDeconnexion().setDescription("Déconnexion de l'application");
		getComponent().getButtonBudget().addClickListener(new ChangePageListener(BudgetMensuelPage.class));
	
		this.compte = getComponent().getComboBoxComptes();
		this.compte.setDescription("Choix du compte");
		this.compte.setTextInputAllowed(false);
		this.compte.setEmptySelectionAllowed(false);
	}


	@Override
	public void miseAJourVueDonnees() {
		// Rien à faire pour le moment
	}
}

