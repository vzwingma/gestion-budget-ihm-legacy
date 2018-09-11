/**
 * 
 */
package com.terrier.finances.gestion.ui.operations.creation.listeners;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.terrier.finances.gestion.communs.operations.model.LigneOperation;
import com.terrier.finances.gestion.communs.operations.model.enums.EtatOperationEnum;
import com.terrier.finances.gestion.communs.operations.model.enums.TypeOperationEnum;
import com.terrier.finances.gestion.communs.parametrages.model.CategorieDepense;
import com.terrier.finances.gestion.communs.parametrages.model.enums.IdsCategoriesEnum;
import com.terrier.finances.gestion.ui.budget.ui.BudgetMensuelController;
import com.terrier.finances.gestion.ui.communs.abstrait.listeners.AbstractComponentListener;
import com.terrier.finances.gestion.ui.operations.creation.ui.CreerDepenseController;
import com.terrier.finances.gestion.ui.operations.creation.ui.CreerDepenseForm;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

/**
 * Validation de la création d'une dépense
 * @author vzwingma
 *
 */
public class ActionValiderCreationDepenseClickListener extends AbstractComponentListener implements ClickListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1823872638217135776L;

	/**
	 * Logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(ActionValiderCreationDepenseClickListener.class);



	/* (non-Javadoc)
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		CreerDepenseForm form = (CreerDepenseForm)event.getButton().getParent().getParent().getParent().getParent();

		LOGGER.debug("[IHM] Validation du formulaire de création");
		Optional<TypeOperationEnum> typeSelected = form.getComboboxType().getSelectedItem();
		TypeOperationEnum type = typeSelected.isPresent() ? typeSelected.get() : TypeOperationEnum.DEPENSE;

		Optional<EtatOperationEnum> etatSelected = form.getComboboxEtat().getSelectedItem();
		EtatOperationEnum etat = etatSelected.isPresent() ? etatSelected.get() : EtatOperationEnum.PREVUE;
		
		Optional<CategorieDepense> categorieSelected = form.getComboBoxSsCategorie().getSelectedItem();
		Optional<String> descriptionSelected = form.getTextFieldDescription().getSelectedItem();


		LigneOperation newOperation = new LigneOperation(
				categorieSelected.isPresent() ? categorieSelected.get() : null, 
				descriptionSelected.isPresent() ? descriptionSelected.get() : null, 
				type,
				form.getTextFieldValeur().getValue(),
				etat,
				form.getCheckBoxPeriodique().getValue());

		// #121 : Opération de réserve: toujours validée
		if(IdsCategoriesEnum.RESERVE.getId().equals(newOperation.getSsCategorie().getId())){
			newOperation.setEtat(EtatOperationEnum.REALISEE);
		}
		LOGGER.debug("[IHM]  >  {}", newOperation);
		boolean resultat = getControleur(CreerDepenseController.class).validateAndCreate(newOperation, form.getComboboxComptes().getSelectedItem());

		if(resultat){
			/**
			 * MAJ des tableaux
			 */
			BudgetMensuelController controleur = getControleur(BudgetMensuelController.class);
			if(event.getButton().getCaption().contains("Fermer")){
				// Fin du formulaire
				getUserSession().getPopupModale().close();
				controleur.miseAJourVueDonnees();
			}
			else{
				// Reset du formulaire
				form.getControleur().miseAJourVueDonnees();
				controleur.miseAJourVueDonnees();		
			}
		}
	}
}

