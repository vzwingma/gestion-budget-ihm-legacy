/**
 * 
 */
package com.terrier.finances.gestion.ui.operations.creation.listeners;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.terrier.finances.gestion.communs.parametrages.model.CategorieOperation;
import com.terrier.finances.gestion.communs.parametrages.model.enums.IdsCategoriesEnum;
import com.terrier.finances.gestion.ui.communs.abstrait.listeners.AbstractComponentListener;
import com.terrier.finances.gestion.ui.operations.creation.ui.CreerDepenseController;
import com.vaadin.event.selection.SingleSelectionEvent;
import com.vaadin.event.selection.SingleSelectionListener;

/**
 * Changement d'une catégorie dans le formulaire de création
 * @author vzwingma
 *
 */
public class SelectionCategorieValueChangeListener extends AbstractComponentListener implements SingleSelectionListener<CategorieOperation>{

	// Controleur
	private CreerDepenseController controleur;


	public SelectionCategorieValueChangeListener(CreerDepenseController controleur){
		this.controleur = controleur;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 7460353635366793837L;

	/**
	 * Sélection d'une catégorie
	 * @see com.vaadin.event.selection.SelectionListener#selectionChange(com.vaadin.event.selection.SelectionEvent)
	 */
	@Override
	public void selectionChange(SingleSelectionEvent<CategorieOperation> event) {
		
		controleur.getComponent().getComboBoxSsCategorie().clear();
		controleur.getComponent().getComboBoxSsCategorie().setSelectedItem(null);
		
		controleur.getComponent().getComboboxComptes().setVisible(false);
		controleur.getComponent().getLayoutCompte().setVisible(false);
		controleur.getComponent().getLabelCompte().setVisible(false);
		
		Optional<CategorieOperation> categories = event.getSelectedItem();
		if(categories.isPresent()){
			CategorieOperation categorie = categories.get();
			
			// Sélection d'une catégorie
			// Alimentation de la liste des sous catégories
			if(categorie != null && categorie.getListeSSCategories() != null){

				List<CategorieOperation> streamSSCategories = categorie.getListeSSCategories()
						.stream()
						.filter(CategorieOperation::isActif)
						.sorted()
						.collect(Collectors.toList());
				controleur.getComponent().getComboBoxSsCategorie().setItems(streamSSCategories);
				// #51 : S'il n'y a qu'un seul élément : sélection automatique de celui ci
				if(streamSSCategories.size() == 1){
					controleur.getComponent().getComboBoxSsCategorie().setSelectedItem(streamSSCategories.get(0));
				}
				else{
					controleur.getComponent().getComboBoxSsCategorie().setSelectedItem(null);
				}
				controleur.getComponent().getComboBoxSsCategorie().setEnabled(true);
				
				//#114 : Si prelevement alors mensuel = true
				controleur.getComponent().getCheckBoxPeriodique().setValue(IdsCategoriesEnum.PRELEVEMENTS_MENSUELS.getId().equals(categorie.getId()));
				
			}
			else{
				controleur.getComponent().getComboBoxSsCategorie().setEnabled(false);
			}
		}
	}
}
