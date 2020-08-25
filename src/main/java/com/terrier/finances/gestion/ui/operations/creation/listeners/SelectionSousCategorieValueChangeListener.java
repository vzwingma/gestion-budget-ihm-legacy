/**
 * 
 */
package com.terrier.finances.gestion.ui.operations.creation.listeners;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.terrier.finances.gestion.communs.comptes.model.v12.CompteBancaire;
import com.terrier.finances.gestion.communs.operations.model.enums.TypeOperationEnum;
import com.terrier.finances.gestion.communs.parametrages.model.v12.CategorieOperation;
import com.terrier.finances.gestion.communs.parametrages.model.enums.IdsCategoriesEnum;
import com.terrier.finances.gestion.communs.utils.exceptions.DataNotFoundException;
import com.terrier.finances.gestion.communs.utils.exceptions.UserNotAuthorizedException;
import com.terrier.finances.gestion.ui.communs.abstrait.listeners.AbstractActionUtilisateurListener;
import com.terrier.finances.gestion.ui.operations.creation.ui.CreerDepenseController;
import com.vaadin.event.selection.SingleSelectionEvent;
import com.vaadin.event.selection.SingleSelectionListener;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification;

/**
 * Changement d'une ss catégorie dans le formulaire de création
 * Affichage du transfert intercompte
 * @author vzwingma
 *
 */
public class SelectionSousCategorieValueChangeListener extends AbstractActionUtilisateurListener implements SingleSelectionListener<CategorieOperation>{

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
	public void selectionChange(SingleSelectionEvent<CategorieOperation> event) {
		Optional<CategorieOperation> catSelected = event.getFirstSelectedItem();
		if(catSelected.isPresent()){
			CategorieOperation ssCategorie = catSelected.get();	
			boolean interCompte = false;
			if(ssCategorie != null){
				interCompte = IdsCategoriesEnum.TRANSFERT_INTERCOMPTE.getId().equals(ssCategorie.getId());
			}	
			/*
			 * Sélection d'un virement intercompte
			 */
			try {
				List<CompteBancaire> listeComptesTransfert = getServiceComptes().getComptes()
						.stream()
						.filter(CompteBancaire::isActif)
						.filter(c -> !c.getId().equals(getUserSession().getBudgetMensuelCourant().getIdCompteBancaire()))
						.collect(Collectors.toList());

				controleur.getComponent().getComboboxComptes().setVisible(interCompte);
				if(interCompte && controleur.getComponent().getComboboxComptes().isEmpty()){
					controleur.getComponent().getComboboxComptes().setItems(listeComptesTransfert);
				}
				controleur.getComponent().getLayoutCompte().setVisible(interCompte);
				controleur.getComponent().getLabelCompte().setVisible(interCompte);
			} catch (DataNotFoundException e) {
				Notification.show("Impossible de charger la liste des comptes pour le transfert. Veuillez réessayer ultérieurement", Notification.Type.ERROR_MESSAGE);
			} catch (UserNotAuthorizedException e) {
				// this.controleur.g
			}

			controleur.getComponent().getComboboxEtat().setVisible(true);
			controleur.getComponent().getLabelEtat().setVisible(true);

			/**
			 * Préparation du type de dépense
			 */
			if(ssCategorie != null){
				TypeOperationEnum typeAttendu = TypeOperationEnum.DEPENSE;
				if(IdsCategoriesEnum.SALAIRE.getId().equals(ssCategorie.getId()) || IdsCategoriesEnum.REMBOURSEMENT.getId().equals(ssCategorie.getId())){
					typeAttendu = TypeOperationEnum.CREDIT;
				}
				controleur.getComponent().getComboboxType().setSelectedItem(typeAttendu);
			}
		}
	}

	@Override
	public void boutonClick(ClickEvent event) {
		// Rien ici
	}
}
