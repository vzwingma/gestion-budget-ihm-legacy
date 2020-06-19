/**
 * 
 */
package com.terrier.finances.gestion.ui.operations.ui;

import java.util.List;

import com.terrier.finances.gestion.communs.operations.model.v12.LigneOperation;
import com.terrier.finances.gestion.ui.budget.ui.BudgetMensuelController;
import com.terrier.finances.gestion.ui.communs.abstrait.AbstractUIController;
import com.terrier.finances.gestion.ui.operations.model.enums.EntetesGridOperationsEnum;

/**
 * Grille des opérations du budget
 * @author vzwingma
 *
 */
public class GridOperationsController extends AbstractUIController<GridOperations>{


	private static final long serialVersionUID = 5190668755144306669L;

	private BudgetMensuelController budgetControleur;

	
	/**
	 * Constructeur du controleur du composant
	 * @param composant
	 */
	public GridOperationsController(GridOperations composant) {
		super(composant);
	}


	/**
	 * Mise à jour de la vue
	 * @see com.terrier.finances.gestion.ui.communs.abstrait.AbstractUIController#miseAJourVueDonnees()
	 */
	@Override
	public void miseAJourVueDonnees() {
		/**
		 * Table de suivi des dépenses
		 */
		getComponent().setSizeFull();
		getComponent().setColumnReorderingAllowed(false);
		getComponent().setResponsive(true);
	}

	/**
	 * Sette la table en mode édition
	 */
	public void updateViewGridOnEditableMode(boolean editableMode){

//		// Activation du tableau en mode édition
        getComponent().getColumn(EntetesGridOperationsEnum.TYPE).setHidden(!editableMode);
        getComponent().getColumn(EntetesGridOperationsEnum.PERIODIQUE).setHidden(!editableMode);
        // Réalignement de la colonne en mode édition
        getComponent().getColumn(EntetesGridOperationsEnum.PERIODIQUE).setWidth(editableMode ? GridOperations.TAILLE_COLONNE_TYPE_MENSUEL + 15 : GridOperations.TAILLE_COLONNE_TYPE_MENSUEL);
        getComponent().getColumn(EntetesGridOperationsEnum.ACTIONS).setHidden(editableMode);
//        getComponent().getColumn(EntetesGridOperationsEnum.DATE_MAJ).setHidden(editableMode);
        
        //
		this.budgetControleur.getComponent().getButtonCreate().setVisible(!editableMode);
	}

	/**
	 * Mise à jour de la vue suite aux données
	 * @param budgetIsActif budget actif ?
	 * @param listeOperations liste des dépenses à utiliser
	 */
	public void miseAJourVueDonnees(boolean budgetIsActif, List<LigneOperation> listeOperations){
		// Ajout des opérations
		getComponent().setItems(listeOperations);

		// Mise à jour des colonnes suivant l'activité du budget
		getComponent().getColumn(EntetesGridOperationsEnum.AUTEUR).setHidden(budgetIsActif);
		getComponent().getColumn(EntetesGridOperationsEnum.ACTIONS).setHidden(!budgetIsActif);
		
		// Not editable	if closed
		if(!getComponent().getEditor().isOpen()) {
			getComponent().getEditor().setEnabled(budgetIsActif);
		}
	}


	/**
	 * @param budgetControleur the budgetControleur to set
	 */
	public void setBudgetControleur(BudgetMensuelController budgetControleur) {
		this.budgetControleur = budgetControleur;
	}


	/**
	 * @return the budgetControleur
	 */
	public BudgetMensuelController getBudgetControleur() {
		return budgetControleur;
	}
	
	
}
