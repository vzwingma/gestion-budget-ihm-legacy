/**
 * 
 */
package com.terrier.finances.gestion.ui.operations.creation.listeners;

import java.util.Optional;

import com.terrier.finances.gestion.communs.operations.model.enums.TypeOperationEnum;
import com.terrier.finances.gestion.communs.parametrages.model.CategorieDepense;
import com.terrier.finances.gestion.services.budget.business.OperationsService;
import com.terrier.finances.gestion.ui.communs.abstrait.listeners.AbstractComponentListener;
import com.terrier.finances.gestion.ui.operations.creation.ui.CreerDepenseController;
import com.vaadin.event.selection.SingleSelectionEvent;
import com.vaadin.event.selection.SingleSelectionListener;

/**
 * Changement d'une ss catégorie dans le formulaire de création
 * Affichage du transfert intercompte
 * @author vzwingma
 *
 */
public class SelectionSousCategorieValueChangeListener extends AbstractComponentListener implements SingleSelectionListener<CategorieDepense>{

	// Controleur
	private CreerDepenseController controleur;

	public SelectionSousCategorieValueChangeListener(CreerDepenseController controleur){
		this.controleur = controleur;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 7460353635366793837L;

	/**
	 * Si transfert intercompte affichage du choix du compte
	 * @see com.vaadin.data.Property.ValueChangeListener#valueChange(com.vaadin.data.Property.ValueChangeEvent)
	 */
	@Override
	public void selectionChange(SingleSelectionEvent<CategorieDepense> event) {
		Optional<CategorieDepense> catSelected = event.getFirstSelectedItem();
		if(catSelected.isPresent()){
			CategorieDepense ssCategorie = catSelected.get();	
			boolean interCompte = false;
			boolean reserve = false;
			if(ssCategorie != null){
				interCompte = OperationsService.ID_SS_CAT_TRANSFERT_INTERCOMPTE.equals(ssCategorie.getId());
				reserve = OperationsService.ID_SS_CAT_RESERVE.equals(ssCategorie.getId());
			}	

			/*
			 * Sélection d'un virement intercompte
			 */
			controleur.getComponent().getComboboxComptes().setVisible(interCompte);
			controleur.getComponent().getLayoutCompte().setVisible(interCompte);
			controleur.getComponent().getLabelCompte().setVisible(interCompte);

			/*
			 * 	#121 sélection d'une réserve
			 */
			controleur.getComponent().getComboboxEtat().setVisible(!reserve);
			controleur.getComponent().getLabelEtat().setVisible(!reserve);

			/**
			 * Préparation du type de dépense
			 */
			if(ssCategorie != null){
				TypeOperationEnum typeAttendu = TypeOperationEnum.DEPENSE;
				if(OperationsService.ID_SS_CAT_SALAIRE.equals(ssCategorie.getId()) || OperationsService.ID_SS_CAT_REMBOURSEMENT.equals(ssCategorie.getId())){
					typeAttendu = TypeOperationEnum.CREDIT;
				}
				controleur.getComponent().getComboboxType().setSelectedItem(typeAttendu);
			}
		}
	}
}
