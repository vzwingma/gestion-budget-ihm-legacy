package com.terrier.finances.gestion.ui.budget.ui;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.terrier.finances.gestion.communs.budget.model.BudgetMensuel;
import com.terrier.finances.gestion.communs.comptes.model.CompteBancaire;
import com.terrier.finances.gestion.communs.comptes.model.api.IntervallesCompteAPIObject;
import com.terrier.finances.gestion.communs.operations.model.LigneOperation;
import com.terrier.finances.gestion.communs.utilisateur.enums.UtilisateurDroitsEnum;
import com.terrier.finances.gestion.communs.utils.data.BudgetDateTimeUtils;
import com.terrier.finances.gestion.communs.utils.exceptions.BudgetNotFoundException;
import com.terrier.finances.gestion.communs.utils.exceptions.CompteClosedException;
import com.terrier.finances.gestion.communs.utils.exceptions.DataNotFoundException;
import com.terrier.finances.gestion.communs.utils.exceptions.UserNotAuthorizedException;
import com.terrier.finances.gestion.ui.budget.listeners.ActionDeconnexionClickListener;
import com.terrier.finances.gestion.ui.budget.listeners.ActionLockBudgetClickListener;
import com.terrier.finances.gestion.ui.budget.listeners.ActionRefreshMonthBudgetClickListener;
import com.terrier.finances.gestion.ui.communs.InformationDialog;
import com.terrier.finances.gestion.ui.communs.abstrait.AbstractUIController;
import com.terrier.finances.gestion.ui.comptes.ui.styles.ComptesItemCaptionStyle;
import com.terrier.finances.gestion.ui.comptes.ui.styles.ComptesItemIconStyle;
import com.terrier.finances.gestion.ui.comptes.ui.styles.ComptesItemStyle;
import com.terrier.finances.gestion.ui.operations.creation.listeners.ActionCreerDepenseClickListener;
import com.terrier.finances.gestion.ui.operations.ui.GridOperationsController;
import com.terrier.finances.gestion.ui.resume.categories.ui.TreeGridResumeCategoriesController;
import com.terrier.finances.gestion.ui.resume.totaux.ui.GridResumeTotauxController;
import com.vaadin.event.UIEvents;
import com.vaadin.shared.ui.datefield.DateResolution;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;

/**
 * Controleur du budget mensuel
 * Le controleur est poolé régulièrement par l'IHM pour vérifier s'il faut mettre à jour le modèle du à des modifs en BDD
 * @author vzwingma
 *
 */
public class BudgetMensuelController extends AbstractUIController<BudgetMensuelPage> implements UIEvents.PollListener, Serializable{

	//
	private static final long serialVersionUID = -235154625221927340L;

	/**
	 * Logger
	 */ 
	private static final Logger LOGGER = LoggerFactory.getLogger(BudgetMensuelController.class);


	// Table de suivi
	private GridOperationsController gridOperationsControleur;
	private GridResumeTotauxController gridResumeTotauxControleur;
	private TreeGridResumeCategoriesController treeResumeControleur;

	/**
	 * Constructure du Controleur du composant
	 * @param composant
	 */
	public BudgetMensuelController(BudgetMensuelPage composant) {
		super(composant);
		// Ajout du pooling listener de l'UI sur ce controleur
		UI.getCurrent().addPollListener(this);

		try {
			initDynamicComponentsOnPage();
		} catch (UserNotAuthorizedException e) {
			forceDisconnect();
		} catch (DataNotFoundException e) {
			Notification.show("Impossible de charger les données du budget. Veuillez réessayer",  Notification.Type.ERROR_MESSAGE);
		}
	}

	/**
	 * Pool de l'IHM pour vérifier la mise à jour vis à vis de la BDD
	 * @param event
	 */
	@Override
	public void poll(UIEvents.PollEvent event) {

		String idSession = getUserSession().getId();
		long nbSessions = getServiceUserSessions().getNombreSessionsActives();
		if(nbSessions > 1){
			BudgetMensuel budgetCourant = getUserSession().getBudgetCourant();
			if(idSession != null &&  budgetCourant != null){
				LOGGER.debug("[REFRESH][{}] Dernière mise à jour reçue pour le budget {} : {}", idSession, 
						budgetCourant.getId(), budgetCourant.getDateMiseAJour() != null ? budgetCourant.getDateMiseAJour() : "null");

				if(!getServiceOperations().isBudgetUpToDate(budgetCourant.getId(), budgetCourant.getDateMiseAJour().getTime())){
					LOGGER.info("[REFRESH][{}] Le budget a été mis à jour en base de données.  Mise à jour de l'IHM", idSession);
					miseAJourVueDonnees();
				}
				else{
					LOGGER.debug("[REFRESH][{}] Le budget est à jour par rapport à la base de données. ", idSession);
				}
			}
		}
		else{
			LOGGER.trace("{} session active. Pas de refresh automatique en cours", nbSessions);
		}
	}

	/**
	 * Init du suivi
	 * @param gridOperationsControleur tableau de suivi
	 * @throws UserNotAuthorizedException  erreur d'authentification
	 * @throws DataNotFoundException  erreur lors de l'appel
	 */
	public void initDynamicComponentsOnPage() throws UserNotAuthorizedException, DataNotFoundException{

		// Init des boutons
		getComponent().getButtonCreate().addClickListener(new ActionCreerDepenseClickListener());
		getComponent().getButtonCreate().setDescription("Ajouter une nouvelle opération");
		getComponent().getComboBoxComptes().setEmptySelectionAllowed(false);

		// Init des controleurs
		this.gridOperationsControleur = getComponent().getGridOperations().getControleur();
		this.gridOperationsControleur.setBudgetControleur(this);
		this.treeResumeControleur = getComponent().getTreeResume().getControleur();
		this.gridResumeTotauxControleur = getComponent().getGridResumeTotaux().getControleur();

		// Init premiere fois
		LocalDate dateBudget = BudgetDateTimeUtils.localDateFirstDayOfMonth();
		if(getComponent().getMois().getValue() == null){
			getComponent().getMois().setValue(dateBudget);
			LOGGER.debug("Init du mois géré : {}", dateBudget);
		}
		// Label last connexion
		LocalDateTime dateDernierAcces = getServiceUtilisateurs().getLastAccessTime();

		if(dateDernierAcces == null){
			dateDernierAcces = LocalDateTime.now();
		}
		this.getComponent().getLabelLastConnected().setValue("Dernière connexion : \n" + BudgetDateTimeUtils.getLibelleDate(dateDernierAcces));

		// Maj des composants MOIS/COMPTES
		getComponent().getMois().setResolution(DateResolution.MONTH);


		getComponent().getComboBoxComptes().setDescription("Choix du compte");
		getComponent().getComboBoxComptes().setTextInputAllowed(false);
		getComponent().getComboBoxComptes().setEmptySelectionAllowed(false);

		int ordreCompte = 100;
		CompteBancaire compteCourant = null;

		List<CompteBancaire> comptes = getServiceComptes().getComptes();
		if(comptes != null && !comptes.isEmpty()){
			// Ajout de la liste des comptes dans la combobox
			getComponent().getComboBoxComptes().setItems(comptes);
			// Sélection du premier du groupe
			for (CompteBancaire compte : comptes) {
				if(compte.getOrdre() <= ordreCompte){
					getComponent().getComboBoxComptes().setSelectedItem(compte);
					compteCourant = compte;
					ordreCompte = compte.getOrdre();
				}
				if(getComponent().getCompteSelectionne() != null && compte.getId().equals(getComponent().getCompteSelectionne().getId())){
					getComponent().getComboBoxComptes().setSelectedItem(getComponent().getCompteSelectionne());
					compteCourant = compte;
				}
			}
			// mise à jour du style
			getComponent().getComboBoxComptes().setItemCaptionGenerator(new ComptesItemCaptionStyle());
			getComponent().getComboBoxComptes().setItemIconGenerator(new ComptesItemIconStyle());
			getComponent().getComboBoxComptes().setStyleGenerator(new ComptesItemStyle());

			initRangeDebutFinMois(compteCourant.getId());
			getComponent().getComboBoxComptes().setTextInputAllowed(false);

			// Bouton déconnexion
			getComponent().getButtonDeconnexion().setCaption("");
			getComponent().getButtonDeconnexion().addClickListener(new ActionDeconnexionClickListener());
			getComponent().getButtonDeconnexion().setDescription("Déconnexion de l'application");
			// Bouton refresh
			getComponent().getButtonRefreshMonth().setVisible(compteCourant.isActif());


			if(getUserSession().isEnabled(UtilisateurDroitsEnum.DROIT_RAZ_BUDGET) && compteCourant.isActif() ){
				getComponent().getButtonRefreshMonth().addClickListener(new ActionRefreshMonthBudgetClickListener());
			}
			else{
				getComponent().getButtonRefreshMonth().setEnabled(false);
			}
			// Bouton lock
			getComponent().getButtonLock().setVisible(compteCourant.isActif());
			if(getUserSession().isEnabled(UtilisateurDroitsEnum.DROIT_CLOTURE_BUDGET)){
				getComponent().getButtonLock().setCaption("");
				getComponent().getButtonLock().addClickListener(new ActionLockBudgetClickListener());
			}
			else{
				getComponent().getButtonLock().setEnabled(false);
			}


			/*
			 * Listeners
			 */
			getComponent().getMois().addValueChangeListener(event -> {
				// Modification de la date
				CompteBancaire compte = getComponent().getComboBoxComptes().getValue();
				LocalDate newDateBudget = event.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate().with(ChronoField.DAY_OF_MONTH, 1);
				LocalDate oldDateBudget = event.getOldValue().atStartOfDay(ZoneId.systemDefault()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate().with(ChronoField.DAY_OF_MONTH, 1);
				if(!newDateBudget.isEqual(oldDateBudget)){
					LOGGER.info("Changement du mois : {}/{} -> {}/{}", oldDateBudget.getMonth(), oldDateBudget.getYear(), newDateBudget.getMonth(), newDateBudget.getYear());
					try {
						setRangeFinMois(newDateBudget, compte.getId());
						miseAJourVueDonnees();
					} catch (UserNotAuthorizedException e) {
						forceDisconnect();						
					} catch (DataNotFoundException e) {
						Notification.show("Impossible de changer de mois. Veuillez réessayer ultérieurement", Notification.Type.ERROR_MESSAGE);
					}

				}
			});

			// modif du compte
			getComponent().getComboBoxComptes().addValueChangeListener(event -> {
				if(event.getOldValue() != null){
					LOGGER.info("Changement du compte : {}->{}", event.getOldValue().getId(), event.getValue().getId());
					try {
						// Modification du compte
						initRangeDebutFinMois(event.getValue().getId());
						miseAJourVueDonnees();
					} catch (UserNotAuthorizedException e) {
						forceDisconnect();						
					} catch (DataNotFoundException e) {
						Notification.show("Impossible de changer de compte. Veuillez réessayer ultérieurement", Notification.Type.ERROR_MESSAGE);
					}
				}
			});


		}
		else{
			Notification.show("Impossible de charger le budget du compte " + (compteCourant != null ? compteCourant.getLibelle() : "" ) + " du " + dateBudget.get(ChronoField.MONTH_OF_YEAR)+"/"+ dateBudget.get(ChronoField.YEAR), Notification.Type.ERROR_MESSAGE);
		}
	}


	/**
	 * Force disconnect si auth non authentifié
	 */
	private void forceDisconnect() {
		setPopupModale(new InformationDialog("Session expirée", "L'utilisateur n'est plus authentifié. \n Retour à la page de login", "OK", this::deconnexion));

	}


	/**
	 * Déconnexion de l'utilisateur
	 */
	public void deconnexion(){
		getServiceUserSessions().deconnexionUtilisateur(getUserSession().getId());
	}


	/**
	 * Finalisation du budget
	 * @throws UserNotAuthorizedException  erreur d'authentification
	 * @throws DataNotFoundException  erreur lors de l'appel
	 */
	public void lockBudget(boolean setBudgetActif) throws DataNotFoundException{
		LOGGER.info("{} du budget mensuel", setBudgetActif ? "Ouverture" : "Clôture");
		try {
			BudgetMensuel lock = getServiceOperations().setBudgetActif(getUserSession().getBudgetCourant().getId(), setBudgetActif);
			// Si l'opération s'est bien passée, on répercute la modif
			if(lock != null){
				getUserSession().updateBudgetInSession(lock);	
			}
			miseAJourVueDonnees();
		}
		catch (UserNotAuthorizedException e) {
			forceDisconnect();
		}
	}

	/**
	 * Réinitialiser le budhet Mensuel Courant
	 */
	public void reinitialiserBudgetCourant() {
		LOGGER.info("Réinitialisation du budget mensuel courant");
		try {
			getUserSession().updateBudgetInSession(getServiceOperations().reinitialiserBudgetMensuel(
					getUserSession().getBudgetCourant()));
			// Ack pour forcer le "refreshAllTable"
			miseAJourVueDonnees();
		} catch (BudgetNotFoundException | DataNotFoundException | CompteClosedException | UserNotAuthorizedException e) {
			LOGGER.error("Erreur lors de la réinitialisation du compte", e);
			Notification.show("Impossible de réinitialiser le mois courant "+
					getUserSession().getBudgetCourant().getMois()+"/"+ getUserSession().getBudgetCourant().getAnnee()
					+" du compte "+ getUserSession().getBudgetCourant().getCompteBancaire().getLibelle(), 
					Notification.Type.ERROR_MESSAGE);
		}
	}


	/**
	 * Set opération comme dernière
	 * @param operation
	 * @throws UserNotAuthorizedException  erreur d'authentification
	 * @throws DataNotFoundException  erreur lors de l'appel
	 */
	public boolean setLigneDepenseAsDerniereOperation(LigneOperation operation) throws DataNotFoundException{
		try {
			if(getServiceOperations().setLigneDepenseAsDerniereOperation(getUserSession().getBudgetCourant(), operation.getId())){
				// Suppression de l'ancienne valeur
				getUserSession().getBudgetCourant().getListeOperations().stream()
					.filter(o -> o.isDerniereOperation())
					.forEach(o -> o.setDerniereOperation(false));
				getUserSession().getBudgetCourant().getListeOperations().stream()
				.filter(o -> operation.getId().equals(o.getId()))
				.forEach(o -> o.setDerniereOperation(true));
				
				miseAJourVueDonnees();
				return true;
			}
		} catch (UserNotAuthorizedException e) {
			forceDisconnect();
		}
		return false;
	}

	/**
	 * Mise à jour du range fin
	 * @param dateFin
	 * @throws UserNotAuthorizedException erreur d'authentification
	 * @throws DataNotFoundException  erreur d'accès
	 */
	public void initRangeDebutFinMois(String idCompte) throws UserNotAuthorizedException, DataNotFoundException{
		if(idCompte != null){
			// Bouton Mois précédent limité au mois du
			// Premier budget du compte de cet utilisateur
			IntervallesCompteAPIObject datePremierDernierBudgets = getServiceBudgetsCompte().getIntervallesBudgets(idCompte);
			getComponent().getMois().setRangeStart(datePremierDernierBudgets.getLocalDatePremierBudget());
			getComponent().getMois().setRangeEnd(datePremierDernierBudgets.getLocalDateDernierBudget());
		}
		LOGGER.debug("> Affichage limité à > [{}/{}]", getComponent().getMois().getRangeStart(), getComponent().getMois().getRangeEnd());

	}
	/**
	 * Mise à jour du range fin
	 * @param dateFin
	 * @throws UserNotAuthorizedException  erreur d'authentification
	 * @throws DataNotFoundException  erreur lors de l'appel
	 */
	private void setRangeFinMois(LocalDate dateFin, String idCompte) throws UserNotAuthorizedException, DataNotFoundException{
		if(dateFin != null){
			// Bouton Mois suivant limité au mois prochain si le compte n'est pas clos
			LocalDate dateRangeBudget = BudgetDateTimeUtils.localDateFirstDayOfMonth();
			CompteBancaire compte = getServiceComptes().getCompte(idCompte);
			if(compte.isActif()){
				dateRangeBudget = dateRangeBudget.plusMonths(1);
			}
			if(dateRangeBudget.isAfter(getComponent().getMois().getRangeEnd())){
				getComponent().getMois().setRangeEnd(dateRangeBudget);
			}
		}
		LOGGER.debug("< Affichage fin limité à [{}/{}] <", getComponent().getMois().getRangeStart(), getComponent().getMois().getRangeEnd());

	}

	/* (non-Javadoc)
	 * @see com.terrier.finances.gestion.ui.controler.AbstractUIController#chargeDonnees()
	 */
	private BudgetMensuel chargeDonnees() throws DataNotFoundException, UserNotAuthorizedException {
		LOGGER.debug("Chargement du budget pour le tableau de suivi des dépenses");

		CompteBancaire compte = getComponent().getComboBoxComptes().getValue();
		LocalDate dateMoisSelectionne = getComponent().getMois().getValue();
		LOGGER.debug("Gestion du Compte : {} du mois {}/{}",compte, dateMoisSelectionne.getMonth(), dateMoisSelectionne.getYear());
		if(getUserSession().getBudgetCourant() == null 
				|| !getUserSession().getBudgetCourant().getCompteBancaire().getId().equals(compte.getId()) 
				|| !getUserSession().getBudgetCourant().getMois().equals(dateMoisSelectionne.getMonth())
				|| getUserSession().getBudgetCourant().getAnnee() != dateMoisSelectionne.getYear()){
			try {
				// Budget
				BudgetMensuel budget = getServiceOperations().chargerBudgetMensuel(
						compte.getId(),
						Month.of(dateMoisSelectionne.getMonthValue()), 
						dateMoisSelectionne.getYear());
				// Maj du budget
				getUserSession().updateBudgetInSession(budget);
				LOGGER.debug("Changement de mois ou de compte : Refresh total des tableaux");
				return budget;

			} catch (BudgetNotFoundException e) {
				String messageerreur = new StringBuilder().
						append("Impossible de charger le budget du compte ").
						append(compte).append(" du ").
						append(dateMoisSelectionne.getMonth()).append("/").append(dateMoisSelectionne.getYear()).
						toString();
				throw new DataNotFoundException(messageerreur);
			}
		}
		else{
			LOGGER.debug("Pas de changement de mois ou de compte : Renvoi du budget mensuel courant");
			return getUserSession().getBudgetCourant();
		}
	}


	/* (non-Javadoc)
	 * @see com.terrier.finances.gestion.ui.controler.AbstractUIController#miseAJourVueDonnees()
	 */
	@Override
	public void miseAJourVueDonnees() {
		BudgetMensuel budgetCourant = null;
		try {
			budgetCourant = chargeDonnees();
			if(budgetCourant.isNewBudget()){
				Notification.show("Création du budget mensuel. Le mois précédent est automatiquement clôturé et les opérations prévues sont reportées", Notification.Type.WARNING_MESSAGE);
				budgetCourant.setNewBudget(false);
			}
		} catch (final DataNotFoundException | UserNotAuthorizedException e) {
			LOGGER.warn("Budget non trouvé.");
			Notification.show(e.getMessage(), Notification.Type.WARNING_MESSAGE);
			return;
		}
		LOGGER.info(">> Mise à jour des vues >> {}", budgetCourant.isActif());		
		LOGGER.debug("Affichage des données dans le tableau de suivi des dépenses");
		List<LigneOperation> listeOperations = new ArrayList<>();
		budgetCourant.getListeOperations().stream().forEach(listeOperations::add);		
		/**
		 * Affichage des lignes dans le tableau
		 **/
		gridOperationsControleur.miseAJourVueDonnees(budgetCourant.isActif(), listeOperations);
		// Boutons actions sur Budget inactif :
		if(!budgetCourant.isActif()){
			getComponent().getButtonCreate().setVisible(false);
			getComponent().getButtonRefreshMonth().setVisible(false);
			getComponent().getButtonLock().setVisible(budgetCourant.getCompteBancaire().isActif());
			getComponent().getButtonLock().setDescription("Réouvrir le budget mensuel");
			getComponent().getButtonLock().setStyleName("locked");
		}
		// Boutons actions sur Budget actif :
		else{
			getComponent().getButtonCreate().setVisible(budgetCourant.getCompteBancaire().isActif());
			getComponent().getButtonRefreshMonth().setVisible(budgetCourant.getCompteBancaire().isActif());
			getComponent().getButtonLock().setVisible(budgetCourant.getCompteBancaire().isActif());
			getComponent().getButtonLock().setDescription("Finaliser le budget mensuel");
			getComponent().getButtonLock().setStyleName("unlocked");
		}

		/** 
		 * Affichage par catégorie
		 */
		LOGGER.debug("Total par categorie");
		treeResumeControleur.miseAJourVueDonnees(budgetCourant);
		/**
		 * Affichage du résumé
		 */
		gridResumeTotauxControleur.miseAJourVueDonnees(budgetCourant);

		LOGGER.debug("<< Mise à jour des vues <<");
	}

}
