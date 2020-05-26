package com.terrier.finances.gestion.ui.operations.actions.ui;

import com.terrier.finances.gestion.communs.operations.model.v12.LigneOperation;
import com.terrier.finances.gestion.communs.operations.model.enums.EtatOperationEnum;
import com.terrier.finances.gestion.ui.communs.abstrait.AbstractUIController;
import com.vaadin.ui.Button;

/**
 * Controleur des boutons d'actions d'une opération
 * @author vzwingma
 *
 */
public class ActionsOperationController extends AbstractUIController<ActionsOperation> {


	private static final long serialVersionUID = 3969804553001678780L;

	private LigneOperation operation;
	/**
	 * Contructeur
	 * @param composant
	 */
	public ActionsOperationController(ActionsOperation composant) {
		super(composant);
	}


	/* (non-Javadoc)
	 * @see com.terrier.finances.gestion.ui.controler.AbstractUIController#miseAJourVueDonnees()
	 */
	@Override
	public void miseAJourVueDonnees() {
		// rien à faire
	}


	/**
	 * mise à jour de l'état de la ligne
	 * @param etat
	 */
	public void miseAJourEtatLigne(EtatOperationEnum etat){
		Button boutonInactif = getComponent().getButtonPrevue();
		if(etat != null){
			if(etat.equals(EtatOperationEnum.ANNULEE)) {
				boutonInactif = getComponent().getButtonAnnuler();
			}
			else if(etat.equals(EtatOperationEnum.REALISEE)) {
				boutonInactif = getComponent().getButtonReel();
			}
			else if(etat.equals(EtatOperationEnum.REPORTEE)) {
				boutonInactif = getComponent().getButtonReporter();
			}
			else if(etat.equals(EtatOperationEnum.PREVUE)) {
				boutonInactif = getComponent().getButtonPrevue();
			}
		}
		else{
			boutonInactif = getComponent().getButtonSupprimer();
		}
		boutonInactif.setVisible(false);
	}


	/**
	 * @return the operation
	 */
	public LigneOperation getOperation() {
		return operation;
	}


	/**
	 * @param operation the operation to set
	 */
	public void setOperation(LigneOperation operation) {
		this.operation = operation;
		miseAJourEtatLigne(this.operation.getEtat());
	}
}

