package com.terrier.finances.gestion.ui.controler.budget.mensuel.liste.operations;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.time.temporal.ChronoField;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.terrier.finances.gestion.model.business.budget.BudgetMensuel;
import com.terrier.finances.gestion.model.business.budget.LigneDepense;
import com.terrier.finances.gestion.model.business.parametrage.CompteBancaire;
import com.terrier.finances.gestion.model.data.DataUtils;
import com.terrier.finances.gestion.model.enums.UtilisateurDroitsEnum;
import com.terrier.finances.gestion.model.exception.BudgetNotFoundException;
import com.terrier.finances.gestion.model.exception.CompteClosedException;
import com.terrier.finances.gestion.model.exception.DataNotFoundException;
import com.terrier.finances.gestion.ui.components.budget.mensuel.BudgetMensuelPage;
import com.terrier.finances.gestion.ui.controler.budget.mensuel.resume.TreeGridResumeCategoriesController;
import com.terrier.finances.gestion.ui.controler.budget.mensuel.totaux.GridResumeTotauxController;
import com.terrier.finances.gestion.ui.controler.common.AbstractUIController;
import com.terrier.finances.gestion.ui.listener.budget.mensuel.boutons.ActionDeconnexionClickListener;
import com.terrier.finances.gestion.ui.listener.budget.mensuel.boutons.ActionLockBudgetClickListener;
import com.terrier.finances.gestion.ui.listener.budget.mensuel.boutons.ActionRefreshMonthBudgetClickListener;
import com.terrier.finances.gestion.ui.listener.budget.mensuel.creation.ActionCreerDepenseClickListener;
import com.terrier.finances.gestion.ui.sessions.UISessionManager;
import com.terrier.finances.gestion.ui.styles.comptes.ComptesItemCaptionStyle;
import com.terrier.finances.gestion.ui.styles.comptes.ComptesItemIconStyle;
import com.terrier.finances.gestion.ui.styles.comptes.ComptesItemStyle;
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

		initDynamicComponentsOnPage();
	}

	/**
	 * Pool de l'IHM pour vérifier la mise à jour vis à vis de la BDD
	 * @param event
	 */
	@Override
	public void poll(UIEvents.PollEvent event) {

		String idSession = getIdSession();
		if(UISessionManager.get().getNombreSessionsActives() > 1){
			BudgetMensuel budgetCourant = getBudgetMensuelCourant();
			if(idSession != null &&  budgetCourant != null){
				LOGGER.debug("[REFRESH][{}] Dernière mise à jour reçue pour le budget {} : {}", idSession, 
						budgetCourant.getId(), budgetCourant.getDateMiseAJour() != null ? budgetCourant.getDateMiseAJour().getTime() : "null");

				if(getServiceOperations().isBudgetUpToDate(budgetCourant.getId(), budgetCourant.getDateMiseAJour())){
					LOGGER.info("[REFRESH][{}] Le budget a été mis à jour en base de données.  Mise à jour de l'IHM", idSession);
					miseAJourVueDonnees();
				}
				else{
					LOGGER.debug("[REFRESH][{}] Le budget est à jour par rapport à la base de données. ", idSession);
				}
			}
		}
		else{
			LOGGER.trace("{} session active. Pas de refresh automatique en cours", UISessionManager.get().getNombreSessionsActives());
		}
	}

	/**
	 * Init du suivi
	 * @param gridOperationsControleur tableau de suivi
	 */
	public void initDynamicComponentsOnPage(){

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
		LocalDate dateBudget = DataUtils.localDateFirstDayOfMonth();
		if(getComponent().getMois().getValue() == null){
			getComponent().getMois().setValue(dateBudget);
			LOGGER.debug("[INIT] Init du mois géré : {}", dateBudget);
		}
		// Label last connexion
		Date dateDernierAcces = getUtilisateurCourant().getDernierAcces();
		if(dateDernierAcces != null){
			SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy HH:mm", Locale.FRENCH);
			sdf.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));
			String date = sdf.format(dateDernierAcces.getTime());
			this.getComponent().getLabelLastConnected().setValue("Dernière connexion : \n" + date);
		}

		// Maj des composants MOIS/COMPTES
		getComponent().getMois().setResolution(DateResolution.MONTH);


		getComponent().getComboBoxComptes().setDescription("Choix du compte");
		getComponent().getComboBoxComptes().setTextInputAllowed(false);
		getComponent().getComboBoxComptes().setEmptySelectionAllowed(false);

		int ordreCompte = 100;
		CompteBancaire compteCourant = null;

		try{
			List<CompteBancaire> comptes = getServiceParams().getComptesUtilisateur(getUtilisateurCourant());
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

			// Bouton stat
			//getComponent().getButtonStatistique().addClickListener(new ChangePageListener(StatistiquesPage.class));
			// Bouton déconnexion
			getComponent().getButtonDeconnexion().setCaption("");
			getComponent().getButtonDeconnexion().addClickListener(new ActionDeconnexionClickListener());
			getComponent().getButtonDeconnexion().setDescription("Déconnexion de l'application");
			// Bouton refresh
			getComponent().getButtonRefreshMonth().setVisible(compteCourant.isActif());
			if(getUtilisateurCourant().isEnabled(UtilisateurDroitsEnum.DROIT_RAZ_BUDGET) && compteCourant.isActif() ){
				getComponent().getButtonRefreshMonth().addClickListener(new ActionRefreshMonthBudgetClickListener());
			}
			else{
				getComponent().getButtonRefreshMonth().setEnabled(false);
			}
			// Bouton lock
			getComponent().getButtonLock().setVisible(compteCourant.isActif());
			if(getUtilisateurCourant().isEnabled(UtilisateurDroitsEnum.DROIT_CLOTURE_BUDGET)){
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
					setRangeFinMois(newDateBudget, compte.getId());
					miseAJourVueDonnees();			
				}
			});

			// modif du compte
			getComponent().getComboBoxComptes().addValueChangeListener(event -> {
				if(event.getOldValue() != null){
					LOGGER.info("Changement du compte : {}->{}", event.getOldValue().getId(), event.getValue().getId());
					// Modification du compte
					initRangeDebutFinMois(event.getValue().getId());
					miseAJourVueDonnees();
				}
			});


		}catch (DataNotFoundException e) {
			Notification.show("Impossible de charger le budget du compte " + (compteCourant != null ? compteCourant.getLibelle() : "" ) + " du " + dateBudget.get(ChronoField.MONTH_OF_YEAR)+"/"+ dateBudget.get(ChronoField.YEAR), Notification.Type.ERROR_MESSAGE);
		}
	}

	
	/**
	 * Déconnexion de l'utilisateur
	 */
	public void deconnexion(){
		UISessionManager.get().deconnexion();
	}


	/**
	 * Finalisation du budget
	 */
	public void lockBudget(boolean setBudgetActif){
		LOGGER.info("[IHM] {} du budget mensuel", setBudgetActif ? "Ouverture" : "Clôture");
		updateBudgetCourantInSession(getServiceOperations().setBudgetActif(getBudgetMensuelCourant(), setBudgetActif));
		miseAJourVueDonnees();
	}

	/**
	 * Réinitialiser le budhet Mensuel Courant
	 */
	public void reinitialiserBudgetCourant() {
		LOGGER.info("Réinitialisation du budget mensuel courant");
		try {
			updateBudgetCourantInSession(getServiceOperations().reinitialiserBudgetMensuel(
					getBudgetMensuelCourant(), 
					getUtilisateurCourant()));
			// Ack pour forcer le "refreshAllTable"
			miseAJourVueDonnees();
		} catch (BudgetNotFoundException | DataNotFoundException | CompteClosedException e) {
			LOGGER.error("[BUDGET] Erreur lors de la réinitialisation du compte", e);
			Notification.show("Impossible de réinitialiser le mois courant "+
					getBudgetMensuelCourant().getMois()+"/"+ getBudgetMensuelCourant().getAnnee()
					+" du compte "+ getBudgetMensuelCourant().getCompteBancaire().getLibelle(), 
					Notification.Type.ERROR_MESSAGE);
		}
	}


	/**
	 * Mise à jour du range fin
	 * @param dateFin
	 */
	public void initRangeDebutFinMois(String idCompte){
		if(idCompte != null){
			// Bouton Mois précédent limité au mois du
			// Premier budget du compte de cet utilisateur
			try {
				LocalDate[] datePremierDernierBudgets = getServiceOperations().getDatePremierDernierBudgets(idCompte);
				getComponent().getMois().setRangeStart(datePremierDernierBudgets[0]);
				getComponent().getMois().setRangeEnd(datePremierDernierBudgets[1]);
			} catch (DataNotFoundException e) {
				LOGGER.error("[IHM] Erreur lors du chargement du premier budget");
			}

		}
		LOGGER.debug("[IHM] > Affichage limité à > [{}/{}]", getComponent().getMois().getRangeStart(), getComponent().getMois().getRangeEnd());

	}
	/**
	 * Mise à jour du range fin
	 * @param dateFin
	 */
	private void setRangeFinMois(LocalDate dateFin, String idCompte){
		if(dateFin != null){
			// Bouton Mois suivant limité au mois prochain si le compte n'est pas clos
			LocalDate dateRangeBudget = DataUtils.localDateFirstDayOfMonth();
			if(getServiceOperations().isCompteActif(idCompte)){
				dateRangeBudget = dateRangeBudget.plusMonths(1);
			}
			if(dateRangeBudget.isAfter(getComponent().getMois().getRangeEnd())){
				getComponent().getMois().setRangeEnd(dateRangeBudget);
			}
		}
		LOGGER.debug("[IHM] < Affichage fin limité à [{}/{}] <", getComponent().getMois().getRangeStart(), getComponent().getMois().getRangeEnd());

	}

	/* (non-Javadoc)
	 * @see com.terrier.finances.gestion.ui.controler.AbstractUIController#chargeDonnees()
	 */
	private BudgetMensuel chargeDonnees() throws DataNotFoundException {
		LOGGER.debug("[BUDGET] Chargement du budget pour le tableau de suivi des dépenses");

		CompteBancaire compte = getComponent().getComboBoxComptes().getValue();
		LocalDate dateMoisSelectionne = getComponent().getMois().getValue();
		LOGGER.debug("[BUDGET] Gestion du Compte : {} du mois {}/{}",compte, dateMoisSelectionne.getMonth(), dateMoisSelectionne.getYear());
		if(getBudgetMensuelCourant() == null 
				|| !getBudgetMensuelCourant().getCompteBancaire().getId().equals(compte.getId()) 
				|| !getBudgetMensuelCourant().getMois().equals(dateMoisSelectionne.getMonth())
				|| getBudgetMensuelCourant().getAnnee() != dateMoisSelectionne.getYear()){
			try {
				// Budget
				BudgetMensuel budget = getServiceOperations().chargerBudgetMensuel(
						getUtilisateurCourant(),
						compte,
						Month.of(dateMoisSelectionne.getMonthValue()), 
						dateMoisSelectionne.getYear());

				// Maj du budget
				getUISession().setBudgetMensuelCourant(budget);
				LOGGER.debug("[BUDGET] Changement de mois ou de compte : Refresh total des tableaux");
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
			LOGGER.debug("[BUDGET] Pas de changement de mois ou de compte : Renvoi du budget mensuel courant");
			return getBudgetMensuelCourant();
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
		} catch (final DataNotFoundException e) {
			LOGGER.warn("[BUDGET] Budget non trouvé.");
			Notification.show(e.getMessage(), Notification.Type.WARNING_MESSAGE);
			return;
		}
		LOGGER.info("[IHM] >> Mise à jour des vues >> {}", budgetCourant.isActif());		
		LOGGER.debug("[IHM] Affichage des données dans le tableau de suivi des dépenses");
		List<LigneDepense> listeOperations = budgetCourant.getListeDepenses();		
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
		LOGGER.debug("[IHM] Total par categorie");
		treeResumeControleur.miseAJourVueDonnees(budgetCourant);
		/**
		 * Affichage du résumé
		 */
		gridResumeTotauxControleur.miseAJourVueDonnees(budgetCourant);

		LOGGER.debug("[IHM] << Mise à jour des vues <<");
	}
}
