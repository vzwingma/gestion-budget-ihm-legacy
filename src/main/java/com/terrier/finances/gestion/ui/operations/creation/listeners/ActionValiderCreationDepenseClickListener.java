/**
 * 
 */
package com.terrier.finances.gestion.ui.operations.creation.listeners;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.terrier.finances.gestion.communs.operations.model.v12.LigneOperation;
import com.terrier.finances.gestion.communs.operations.model.enums.EtatOperationEnum;
import com.terrier.finances.gestion.communs.operations.model.enums.TypeOperationEnum;
import com.terrier.finances.gestion.communs.parametrages.model.v12.CategorieOperation;
import com.terrier.finances.gestion.communs.utils.data.BudgetDataUtils;
import com.terrier.finances.gestion.ui.budget.ui.BudgetMensuelController;
import com.terrier.finances.gestion.ui.communs.abstrait.listeners.AbstractActionUtilisateurListener;
import com.terrier.finances.gestion.ui.operations.creation.ui.CreerDepenseController;
import com.terrier.finances.gestion.ui.operations.creation.ui.CreerDepenseForm;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

/**
 * Validation de la création d'une dépense
 * @author vzwingma
 *
 */
public class ActionValiderCreationDepenseClickListener extends AbstractActionUtilisateurListener implements ClickListener {

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
	public void boutonClick(ClickEvent event) {
		CreerDepenseForm form = (CreerDepenseForm)event.getButton().getParent().getParent().getParent().getParent();

		LOGGER.debug("Validation du formulaire de création");
		Optional<TypeOperationEnum> typeSelected = form.getComboboxType().getSelectedItem();
		TypeOperationEnum type = typeSelected.isPresent() ? typeSelected.get() : TypeOperationEnum.DEPENSE;

		Optional<EtatOperationEnum> etatSelected = form.getComboboxEtat().getSelectedItem();
		EtatOperationEnum etat = etatSelected.isPresent() ? etatSelected.get() : EtatOperationEnum.PREVUE;
		
		Optional<CategorieOperation> ssCategorieSelected = form.getComboBoxSsCategorie().getSelectedItem();
		Optional<String> descriptionSelected = form.getTextFieldDescription().getSelectedItem();


		LigneOperation newOperation = new LigneOperation(
				ssCategorieSelected.isPresent() ? ssCategorieSelected.get() : null, 
				descriptionSelected.isPresent() ? descriptionSelected.get() : null, 
				type,
				BudgetDataUtils.getValueFromString(form.getTextFieldValeur().getValue()),
				etat,
				form.getCheckBoxPeriodique().getValue());

		LOGGER.debug(" >  {}", newOperation);
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

