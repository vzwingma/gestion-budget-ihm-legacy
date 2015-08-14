package com.terrier.finances.gestion.ui.controler.budget.mensuel;

import com.terrier.finances.gestion.model.enums.EtatLigneDepenseEnum;
import com.terrier.finances.gestion.ui.components.budget.mensuel.ActionsLigneBudget;
import com.terrier.finances.gestion.ui.controler.common.AbstractUIController;
import com.terrier.finances.gestion.ui.listener.budget.mensuel.ActionsLigneDepenseClickListener;
import com.vaadin.ui.Button;

/**
 * Controleur du budget mensuel
 * @author vzwingma
 *
 */
public class ActionsLigneBudgetController extends AbstractUIController<ActionsLigneBudget> {


	/**
	 * 
	 */
	private static final long serialVersionUID = 3969804553001678780L;

	/**
	 * Identifiant de la dépense
	 */
	private String idDepense;

	
	/**
	 * Contructeur
	 * @param composant
	 */
	public ActionsLigneBudgetController(ActionsLigneBudget composant) {
		super(composant);
	}
	
	/**
	 * @see com.terrier.finances.gestion.ui.controler.common.AbstractUIController#initDynamicComponentsOnPage()
	 */
	public void initDynamicComponentsOnPage(){
		getComponent().getButtonReel().addClickListener(new ActionsLigneDepenseClickListener());
		getComponent().getButtonReel().setId("buttonReel");
		getComponent().getButtonReel().setStyleName("v-button-actions v-button-reel");
		getComponent().getButtonReel().setDescription("Enregistrement de la dépense");
		
		getComponent().getButtonAnnuler().addClickListener(new ActionsLigneDepenseClickListener());
		getComponent().getButtonAnnuler().setId("buttonAnnuler");
		getComponent().getButtonAnnuler().setStyleName("v-button-actions v-button-annuler");
		getComponent().getButtonAnnuler().setDescription("Annulation de la dépense");
		
		
		getComponent().getButtonReporter().addClickListener(new ActionsLigneDepenseClickListener());
		getComponent().getButtonReporter().setId("buttonReporter");
		getComponent().getButtonReporter().setStyleName("v-button-actions v-button-reporter");
		getComponent().getButtonReporter().setDescription("Report de la dépense au mois prochain");
		
		getComponent().getButtonPrevue().addClickListener(new ActionsLigneDepenseClickListener());
		getComponent().getButtonPrevue().setId("buttonPrevue");
		getComponent().getButtonPrevue().setStyleName("v-button-actions v-button-prevue");
		getComponent().getButtonPrevue().setDescription("Mise en prévision de la dépense");
		
		getComponent().getButtonSupprimer().addClickListener(new ActionsLigneDepenseClickListener());
		getComponent().getButtonSupprimer().setId("buttonSupprimer");
		getComponent().getButtonSupprimer().setStyleName("v-button-actions v-button-supprimer");
		getComponent().getButtonSupprimer().setDescription("Suppression de la dépense");		
	}

	public void setidDepense(String idDepense){
		this.idDepense = idDepense;
	}
	
	
	/**
	 * mise à jour de l'état de la ligne
	 * @param etat
	 */
	public void miseAJourEtatLigne(EtatLigneDepenseEnum etat){
		Button boutonInactif = getComponent().getButtonPrevue();
		if(etat != null){
			if(etat.equals(EtatLigneDepenseEnum.ANNULEE)) {
				boutonInactif = getComponent().getButtonAnnuler();
			}
			else if(etat.equals(EtatLigneDepenseEnum.REALISEE)) {
				boutonInactif = getComponent().getButtonReel();
			}
			else if(etat.equals(EtatLigneDepenseEnum.REPORTEE)) {
				boutonInactif = getComponent().getButtonReporter();
			}
			else if(etat.equals(EtatLigneDepenseEnum.PREVUE)) {
				boutonInactif = getComponent().getButtonPrevue();
			}
		}
		else{
			boutonInactif = getComponent().getButtonSupprimer();
		}
		boutonInactif.setVisible(false);
	}
	
		


	/**
	 * @return the idDepense
	 */
	public String getIdDepense() {
		return idDepense;
	}

	/* (non-Javadoc)
	 * @see com.terrier.finances.gestion.ui.controler.AbstractUIController#miseAJourVueDonnees()
	 */
	@Override
	public void miseAJourVueDonnees() { }
}
