package com.terrier.finances.gestion.ui.controler.budget.mensuel.creer.operation;

import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.terrier.finances.gestion.business.validator.OperationValidator;
import com.terrier.finances.gestion.model.business.budget.BudgetMensuel;
import com.terrier.finances.gestion.model.business.budget.LigneDepense;
import com.terrier.finances.gestion.model.business.parametrage.CompteBancaire;
import com.terrier.finances.gestion.model.enums.EtatLigneDepenseEnum;
import com.terrier.finances.gestion.model.enums.TypeDepenseEnum;
import com.terrier.finances.gestion.model.enums.UtilisateurPrefsEnum;
import com.terrier.finances.gestion.model.exception.DataNotFoundException;
import com.terrier.finances.gestion.services.budget.business.OperationsService;
import com.terrier.finances.gestion.ui.components.budget.mensuel.components.CreerDepenseForm;
import com.terrier.finances.gestion.ui.controler.common.AbstractUIController;
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
	public boolean validateAndCreate(LigneDepense newOperation, Optional<CompteBancaire> compteTransfert){
		ValidationResult resultatValidation = new OperationValidator().apply(newOperation, null);
		if(!resultatValidation.isError()){
			// Si oui création
			BudgetMensuel budget = getBudgetMensuelCourant();
			try{
				if(OperationsService.ID_SS_CAT_TRANSFERT_INTERCOMPTE.equals(newOperation.getSsCategorie().getId())
						&& compteTransfert.isPresent()){
					LOGGER.info("[IHM] Ajout d'un nouveau transfert intercompte");
					updateBudgetCourantInSession(getServiceOperations().ajoutLigneTransfertIntercompte(budget.getId(), newOperation, compteTransfert.get(), getUtilisateurCourant()));
					Notification.show("Le transfert inter-compte a bien été créée", Notification.Type.TRAY_NOTIFICATION);
				}
				else{
					LOGGER.info("[IHM] Ajout d'une nouvelle dépense");
					updateBudgetCourantInSession(getServiceOperations().ajoutOperationEtCalcul(budget.getId(), newOperation, getUtilisateurCourant()));
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
	 * @see com.terrier.finances.gestion.ui.controler.common.AbstractUIController#miseAJourVueDonnees()
	 */
	@Override
	public void miseAJourVueDonnees() {

		// Sélection d'une catégorie
		// Catégories
		getComponent().getComboBoxCategorie().clear();
		getComponent().getComboBoxCategorie().setSelectedItem(null);
		getComponent().getComboBoxCategorie().setItems(getServiceParams().getCategories().stream().sorted((c1, c2) -> c1.getLibelle().compareTo(c2.getLibelle())));
		getComponent().getComboBoxCategorie().setEnabled(true);

		
		// Sélection d'une sous catégorie
		getComponent().getComboBoxSsCategorie().clear();
		getComponent().getComboBoxSsCategorie().setSelectedItem(null);
		getComponent().getComboBoxSsCategorie().setEnabled(false);
		
		
		// Comptes pour virement intercomptes
		try{
			getComponent().getComboboxComptes().setItems(
					getServiceParams().getComptesUtilisateur(getUtilisateurCourant())
					.stream()
					.filter(CompteBancaire::isActif)
					.filter(c -> !c.getId().equals(getBudgetMensuelCourant().getCompteBancaire().getId()))
					.collect(Collectors.toList()));
			getComponent().getComboboxComptes().clear();
		} catch (DataNotFoundException e) {
			Notification.show("Erreur grave : Impossible de charger les données", Notification.Type.ERROR_MESSAGE);
			return;
		}
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
		getComponent().getComboboxType().setItems(TypeDepenseEnum.values());
		getComponent().getComboboxType().setTextInputAllowed(false);
		getComponent().getComboboxType().setSelectedItem(TypeDepenseEnum.DEPENSE);
		getComponent().getComboboxType().clear();
		// Etat
		getComponent().getListSelectEtat().setItems(EtatLigneDepenseEnum.values());
		getComponent().getListSelectEtat().setTextInputAllowed(false);
		getComponent().getListSelectEtat().clear();
		// #50 : Gestion du style par préférence utilisateur
		String etatNlleDepense = getUtilisateurCourant().getPreference(UtilisateurPrefsEnum.PREFS_STATUT_NLLE_DEPENSE);
		if(etatNlleDepense != null){
			getComponent().getListSelectEtat().setSelectedItem(EtatLigneDepenseEnum.getEnum(etatNlleDepense));
		}
		else{
			getComponent().getListSelectEtat().setSelectedItem(EtatLigneDepenseEnum.PREVUE);
		}
		// Périodique
		getComponent().getCheckBoxPeriodique().setCaption(null);
		getComponent().getCheckBoxPeriodique().setValue(Boolean.FALSE);
		getComponent().getCheckBoxPeriodique().clear();
		// Description
		getComponent().getTextFieldDescription().setItems(getBudgetMensuelCourant().getSetLibellesDepensesForAutocomplete());
		getComponent().getTextFieldDescription().clear();
	}
}
