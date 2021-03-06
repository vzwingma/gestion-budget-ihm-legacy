package com.terrier.finances.gestion.ui.operations.creation.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.terrier.finances.gestion.communs.budget.model.v12.BudgetMensuel;
import com.terrier.finances.gestion.communs.comptes.model.v12.CompteBancaire;
import com.terrier.finances.gestion.communs.operations.model.enums.EtatOperationEnum;
import com.terrier.finances.gestion.communs.operations.model.enums.TypeOperationEnum;
import com.terrier.finances.gestion.communs.operations.model.v12.LigneOperation;
import com.terrier.finances.gestion.communs.parametrages.model.enums.IdsCategoriesEnum;
import com.terrier.finances.gestion.communs.parametrages.model.v12.CategorieOperation;
import com.terrier.finances.gestion.communs.utilisateur.enums.UtilisateurPrefsEnum;
import com.terrier.finances.gestion.communs.utils.exceptions.DataNotFoundException;
import com.terrier.finances.gestion.communs.utils.exceptions.UserNotAuthorizedException;
import com.terrier.finances.gestion.ui.communs.abstrait.AbstractUIController;
import com.terrier.finances.gestion.ui.operations.creation.validator.OperationValidator;
import com.vaadin.data.ValidationResult;
import com.vaadin.ui.Notification;

/**
 * Controleur de créer des dépenses
 * @author vzwingma
 *
 */
public class CreerDepenseController extends AbstractUIController<CreerDepenseForm> {


	// 
	private static final long serialVersionUID = -1843521169417325067L;
	/**
	 * Logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(CreerDepenseController.class);


	/**
	 * Contructeur
	 * @param composant composant
	 */
	public CreerDepenseController(CreerDepenseForm composant) {
		super(composant);
	}


	/**
	 * Validation formulaire
	 * @param newOperation operation
	 * @param compteTransfert compte si transfert inter compte
	 */
	public boolean validateAndCreate(LigneOperation newOperation, Optional<CompteBancaire> compteTransfert){
		ValidationResult resultatValidation = new OperationValidator().apply(newOperation, null);
		if(!resultatValidation.isError()){
			// Si oui création
			BudgetMensuel budget = getUserSession().getBudgetMensuelCourant();
			try{
				if(IdsCategoriesEnum.TRANSFERT_INTERCOMPTE.getId().equals(newOperation.getSsCategorie().getId())
						&& compteTransfert.isPresent()){
					LOGGER.info("Ajout d'un nouveau transfert intercompte");
					getUserSession().updateBudgetInSession(getServiceOperations().ajoutLigneTransfertIntercompte(budget.getId(), newOperation, compteTransfert.get()));
					Notification.show("Le transfert inter-compte a bien été créée", Notification.Type.TRAY_NOTIFICATION);
				}
				else{
					LOGGER.info("Ajout d'une nouvelle dépense");
					getUserSession().updateBudgetInSession(getServiceOperations().majLigneOperation(budget.getId(), newOperation));
					Notification.show("l'opération a bien été créée", Notification.Type.TRAY_NOTIFICATION);
				}
				return true;
			}
			catch(Exception e){
				LOGGER.error("Erreur : ", e);
				Notification.show("Impossible de créer l'opération : " + e.getMessage(), Notification.Type.ERROR_MESSAGE);
				return false;
			}
		}
		else{
			LOGGER.error("Erreur : {}", resultatValidation.getErrorMessage());
			Notification.show("Impossible de créer l'opération : " + resultatValidation.getErrorMessage(), Notification.Type.ERROR_MESSAGE);
			return false;
		}
	}


	/**
	 * Complétion des éléments du formulaire
	 * @see com.terrier.finances.gestion.ui.communs.abstrait.AbstractUIController#miseAJourVueDonnees()
	 */
	@Override
	public void miseAJourVueDonnees() {

		// Sélection d'une catégorie
		// Catégories
		getComponent().getComboBoxCategorie().clear();
		getComponent().getComboBoxCategorie().setSelectedItem(null);
		getComponent().getComboBoxCategorie().setItems(
				getServiceParams().getCategories()
					.stream()
					.sorted((c1, c2) -> c1.getLibelle().compareTo(c2.getLibelle())));
		getComponent().getComboBoxCategorie().setEnabled(true);


		// Sélection d'une sous catégorie
		getComponent().getComboBoxSsCategorie().clear();
		getComponent().getComboBoxSsCategorie().setSelectedItem(null);
		getComponent().getComboBoxSsCategorie().setEnabled(true);
		getComponent().getComboBoxSsCategorie().setTextInputAllowed(true);

		List<CategorieOperation> ssCats = new ArrayList<>();
		getServiceParams().getCategories()
		.stream()
		.sorted((c1, c2) -> c1.getLibelle().compareTo(c2.getLibelle()))
		.forEach(c -> {
			c.getListeSSCategories()
			.stream()
			.sorted((ssc1, ssc2) -> ssc1.getLibelle().compareTo(ssc2.getLibelle()))
			.forEach(ssc -> ssCats.add(ssc));
		});
		getComponent().getComboBoxSsCategorie().setItems(ssCats);

		// Comptes pour virement intercomptes
		getComponent().getComboboxComptes().setTextInputAllowed(false);
		getComponent().getComboboxComptes().setVisible(false);
		getComponent().getLayoutCompte().setVisible(false);
		getComponent().getLabelCompte().setVisible(false);
		// Description
		getComponent().getTextFieldDescription().setSelectedItem(null);
		// Valeur
		getComponent().getTextFieldValeur().clear();
		getComponent().getTextFieldValeur().setValue("0");
		// Type dépense
		getComponent().getComboboxType().setItems(TypeOperationEnum.values());
		getComponent().getComboboxType().setTextInputAllowed(false);
		getComponent().getComboboxType().setSelectedItem(TypeOperationEnum.DEPENSE);
		getComponent().getComboboxType().clear();
		// Etat
		getComponent().getComboboxEtat().setItems(EtatOperationEnum.values());
		getComponent().getComboboxEtat().setTextInputAllowed(false);
		getComponent().getComboboxEtat().clear();
		// #50 : Gestion du style par préférence utilisateur
		Object etatNlleDepense = null;
		try {
			etatNlleDepense = getServiceUtilisateurs().getPreferenceDroits().getPreferences().getOrDefault(UtilisateurPrefsEnum.PREFS_STATUT_NLLE_DEPENSE, EtatOperationEnum.PREVUE.getId());
		} catch (UserNotAuthorizedException | DataNotFoundException e1) {
			LOGGER.warn("Impossible de trouver les préférences utilisateurs");
		}
		if(etatNlleDepense != null){
			getComponent().getComboboxEtat().setSelectedItem(EtatOperationEnum.getEnum((String)etatNlleDepense));
		}
		else{
			getComponent().getComboboxEtat().setSelectedItem(EtatOperationEnum.PREVUE);
		}
		// Périodique
		getComponent().getCheckBoxPeriodique().setCaption(null);
		getComponent().getCheckBoxPeriodique().setValue(Boolean.FALSE);
		getComponent().getCheckBoxPeriodique().clear();
		// Description
		try {
			getComponent().getTextFieldDescription().setItems(
				getServiceLibellesOperations().getForAutocomplete(
					getUserSession().getBudgetMensuelCourant().getIdCompteBancaire(),
					getUserSession().getBudgetMensuelCourant().getAnnee()));
		} catch (Exception e) {
			LOGGER.error("Erreur lors de l'accès à l'autocompletion des libellés");
		}
		getComponent().getTextFieldDescription().clear();
	}
}
