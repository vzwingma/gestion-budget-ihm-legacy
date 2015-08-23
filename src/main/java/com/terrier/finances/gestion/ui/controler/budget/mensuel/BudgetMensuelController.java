package com.terrier.finances.gestion.ui.controler.budget.mensuel;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.terrier.finances.gestion.model.business.budget.BudgetMensuel;
import com.terrier.finances.gestion.model.business.budget.LigneDepense;
import com.terrier.finances.gestion.model.business.parametrage.CompteBancaire;
import com.terrier.finances.gestion.model.enums.EntetesTableSuiviDepenseEnum;
import com.terrier.finances.gestion.model.exception.BudgetNotFoundException;
import com.terrier.finances.gestion.model.exception.DataNotFoundException;
import com.terrier.finances.gestion.ui.components.budget.mensuel.BudgetMensuelPage;
import com.terrier.finances.gestion.ui.controler.budget.mensuel.components.TableResumeTotauxController;
import com.terrier.finances.gestion.ui.controler.budget.mensuel.components.TableSuiviDepenseController;
import com.terrier.finances.gestion.ui.controler.budget.mensuel.components.TreeResumeCategoriesController;
import com.terrier.finances.gestion.ui.controler.common.AbstractUIController;
import com.terrier.finances.gestion.ui.listener.budget.mensuel.ActionDeconnexionClickListener;
import com.terrier.finances.gestion.ui.listener.budget.mensuel.ActionEditerDepensesClickListener;
import com.terrier.finances.gestion.ui.listener.budget.mensuel.ActionLockBudgetClickListener;
import com.terrier.finances.gestion.ui.listener.budget.mensuel.ActionRefreshMonthBudgetClickListener;
import com.terrier.finances.gestion.ui.listener.budget.mensuel.ActionValiderAnnulerEditionDepenseListener;
import com.terrier.finances.gestion.ui.listener.budget.mensuel.creation.ActionCreerDepenseClickListener;
import com.terrier.finances.gestion.ui.sessions.UISessionManager;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Notification;

/**
 * Controleur du budget mensuel
 * @author vzwingma
 *
 */
public class BudgetMensuelController extends AbstractUIController<BudgetMensuelPage> implements ValueChangeListener, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -235154625221927340L;

	/**
	 * Logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(BudgetMensuelController.class);


	// Table de suivi
	private TableSuiviDepenseController tableSuiviDepenseControleur;
	private TableResumeTotauxController tableTotalResumeControleur;
	private TreeResumeCategoriesController treeResumeControleur;
	private ComboBox compte;

	// Calcul de mise à jour du compte courant
	private int oldMois;
	private int oldAnnee;
	private String oldIdCompte;
	private boolean refreshAllTable = false;
	/**
	 * Constructure du Controleur du composant
	 * @param composant
	 */
	public BudgetMensuelController(BudgetMensuelPage composant) {
		super(composant);
	}



	/**
	 * Init du suivi
	 * @param tableSuiviDepenseControleur tableau de suivi
	 */
	@Override
	public void initDynamicComponentsOnPage(){

		// Démarrage
		getComponent().getButtonEditer().addClickListener(new ActionEditerDepensesClickListener());
		getComponent().getButtonEditer().setDescription("Editer le tableau des opérations");
		getComponent().getButtonCreate().addClickListener(new ActionCreerDepenseClickListener());
		getComponent().getButtonCreate().setDescription("Ajouter une nouvelle opération");
		getComponent().getComboBoxComptes().setNullSelectionAllowed(false);
		getComponent().getComboBoxComptes().setImmediate(true);

		getComponent().getButtonValider().setVisible(false);
		getComponent().getButtonValider().setEnabled(false);
		getComponent().getButtonValider().addClickListener(new ActionValiderAnnulerEditionDepenseListener());
		getComponent().getButtonValider().setDescription("Valider les modifications du tableau des dépenses");
		getComponent().getButtonValider().setClickShortcut(KeyCode.ENTER);
		getComponent().getButtonAnnuler().setVisible(false);
		getComponent().getButtonAnnuler().setEnabled(false);
		getComponent().getButtonAnnuler().addClickListener(new ActionValiderAnnulerEditionDepenseListener());
		getComponent().getButtonAnnuler().setDescription("Annuler les modifications du tableau des dépenses");


		// Init des controleurs
		this.tableSuiviDepenseControleur = getComponent().getTableSuiviDepense().getControleur();
		this.treeResumeControleur = getComponent().getTreeResume().getControleur();
		this.tableTotalResumeControleur = getComponent().getTableTotalResume().getControleur();

		// Init premiere fois
		Calendar dateBudget = Calendar.getInstance();
		dateBudget.set(Calendar.DAY_OF_MONTH, 1);
		if(getComponent().getMois().getValue() == null){
			getComponent().getMois().setValue(dateBudget.getTime());
			LOGGER.debug("[INIT] Init du mois géré : {}", dateBudget.getTime());
		}
		// Label lastconnextion
		if(UISessionManager.getSession().getUtilisateurCourant().getDateDernierAcces() != null){
			SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM YYYY HH:mm", Locale.FRENCH);
			sdf.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));
			String date = sdf.format(UISessionManager.getSession().getUtilisateurCourant().getDateDernierAcces().getTime());
			this.getComponent().getLabelLastConnected().setValue("Dernière connexion : \n" + date);
		}

		// Maj des composants MOIS/COMPTES
		getComponent().getMois().setResolution(Resolution.MONTH);
		getComponent().getMois().setImmediate(true);
		getComponent().getMois().addValueChangeListener(this);

		this.compte = getComponent().getComboBoxComptes();
		this.compte.setImmediate(true);
		this.compte.setDescription("Choix du compte");
		this.compte.setNewItemsAllowed(false);
		this.compte.setNullSelectionAllowed(false);
		String libelleCompte =null;
		try{
			List<CompteBancaire> comptes = getServiceParams().getComptesUtilisateur(UISessionManager.getSession().getUtilisateurCourant());
			for (CompteBancaire compte : comptes) {
				this.compte.addItem(compte.getId());
				this.compte.setItemCaption(compte.getId(), "  " + compte.getLibelle());
				if(compte.isDefaut()){
					this.compte.select(compte.getId());
					libelleCompte = compte.getLibelle();
				}
				if(getComponent().getIdCompteSelectionne() != null && compte.getId().equals(getComponent().getIdCompteSelectionne())){
					this.compte.select(compte.getId());
					libelleCompte = compte.getLibelle();
				}
				this.compte.setItemIcon(compte.getId(), new ThemeResource(compte.getItemIcon()));
			}
			String idCompte = (String)this.compte.getConvertedValue();
			initRangeDebutFinMois(idCompte);
			this.compte.setTextInputAllowed(false);
			this.compte.addValueChangeListener(this);
			// Bouton stat
			//getComponent().getButtonStatistique().addClickListener(new ChangePageListener(StatistiquesPage.class));
			// Bouton déconnexion
			getComponent().getButtonDeconnexion().setCaption("");
			getComponent().getButtonDeconnexion().addClickListener(new ActionDeconnexionClickListener());
			getComponent().getButtonDeconnexion().setDescription("Déconnexion de l'application");
			// Bouton lock
			getComponent().getButtonRefreshMonth().addClickListener(new ActionRefreshMonthBudgetClickListener());
			// Bouton lock
			getComponent().getButtonLock().setCaption("");
			getComponent().getButtonLock().addClickListener(new ActionLockBudgetClickListener());
		} catch (DataNotFoundException e) {
			Notification.show("Impossible de charger le budget du compte "+ libelleCompte + " du " + dateBudget.get(Calendar.MONTH)+"/"+ dateBudget.get(Calendar.YEAR), Notification.Type.ERROR_MESSAGE);
		}


	}


	/**
	 * Déconnexion de l'utilisateur
	 */
	public void deconnexion(){
		UISessionManager.deconnexion();
	}


	/**
	 * Finalisation du budget
	 */
	public void lockBudget(boolean setBudgetActif){
		LOGGER.info("[IHM] {} du budget mensuel", setBudgetActif ? "Ouverture" : "Clôture");
		getServiceDepense().setBudgetActif(UISessionManager.getSession().getBudgetMensuelCourant(), setBudgetActif);
		miseAJourVueDonnees();
	}

	/**
	 * Réinitialiser le budhet Mensuel Courant
	 */
	public void reinitialiserBudgetCourant(){
		LOGGER.info("Réinitialisation du budget mensuel courant");
		try {
			getServiceDepense().reinitialiserBudgetMensuel(
					UISessionManager.getSession().getBudgetMensuelCourant(), 
					UISessionManager.getSession().getUtilisateurCourant());
			// Ack pour forcer le "refreshAllTable"
			oldMois = -1;
			miseAJourVueDonnees();
		} catch (BudgetNotFoundException | DataNotFoundException e) {
			LOGGER.error("[BUDGET] Erreur lors de la réinitialisation du compte", e);
			Notification.show("Impossible de réinitialiser le mois courant "+
					( UISessionManager.getSession().getBudgetMensuelCourant().getMois() + 1)+"/"+UISessionManager.getSession().getBudgetMensuelCourant().getAnnee()
					+" du compte "+ UISessionManager.getSession().getBudgetMensuelCourant().getCompteBancaire().getLibelle(), 
					Notification.Type.ERROR_MESSAGE);
		}
	}

	
	/**
	 * Modif du compte ou bien de la date
	 * @see com.vaadin.data.Property.ValueChangeListener#valueChange(com.vaadin.data.Property.ValueChangeEvent)
	 */
	@Override
	public void valueChange(ValueChangeEvent event) {
		// Modification de la date
		boolean miseAJour = false;
		if(event.getProperty().getType().equals(Date.class)){
			Calendar d = Calendar.getInstance();
			d.setTime((Date)event.getProperty().getValue());
			d.set(Calendar.DAY_OF_MONTH, 1);
			setRangeFinMois(d);
			if(d.get(Calendar.MONTH) != this.oldMois || d.get(Calendar.YEAR) != this.oldAnnee){
				miseAJourVueDonnees();			
			}
		}
		else{
			// Modification du compte
			miseAJour = true;
			String idCompte = (String)this.compte.getConvertedValue();
			initRangeDebutFinMois(idCompte);
		}
		// Si oui : refresh
		if(miseAJour){
			miseAJourVueDonnees();
		}
	}





	/**
	 * Mise à jour du range fin
	 * @param dateFin
	 */
	private void initRangeDebutFinMois(String idCompte){
		if(idCompte != null){
			// Bouton Mois précédent limité au mois du
			// Premier budget du compte de cet utilisateur
			try {
				Calendar[] datePremierDernierBudgets = getServiceDepense().getDatePremierDernierBudgets(idCompte);
				getComponent().getMois().setRangeStart(datePremierDernierBudgets[0].getTime());
				getComponent().getMois().setRangeEnd(datePremierDernierBudgets[1].getTime());
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
	private void setRangeFinMois(Calendar dateFin){
		if(dateFin != null){
			// Bouton Mois suivant limité au mois prochain.
			Calendar dateRangeBudget = Calendar.getInstance();
			dateRangeBudget.setTime(dateFin.getTime());
			dateRangeBudget.add(Calendar.MONTH, 1);
			if(dateRangeBudget.getTime().after(getComponent().getMois().getRangeEnd())){
				getComponent().getMois().setRangeEnd(dateRangeBudget.getTime());
			}
		}
		LOGGER.debug("[IHM] < Affichage limité à [{}/{}] <", getComponent().getMois().getRangeStart(), getComponent().getMois().getRangeEnd());

	}

	/* (non-Javadoc)
	 * @see com.terrier.finances.gestion.ui.controler.AbstractUIController#chargeDonnees()
	 */
	private BudgetMensuel chargeDonnees() throws DataNotFoundException {
		LOGGER.debug("[BUDGET] Chargement du budget pour le tableau de suivi des dépenses");

		String idCompte = (String)this.compte.getConvertedValue();
		Date dateMois = (Date)getComponent().getMois().getConvertedValue();

		Calendar c = Calendar.getInstance();
		if(dateMois != null){
			c.setTime(dateMois);
		}

		LOGGER.debug("[BUDGET] Gestion du Compte : {} du mois {}",idCompte, c.getTime());

		try {
			// Budget
			BudgetMensuel budget = getServiceDepense().chargerBudgetMensuel(
					UISessionManager.getSession().getUtilisateurCourant(),
					idCompte,
					c.get(Calendar.MONTH), 
					c.get(Calendar.YEAR));

			// Maj du budget
			UISessionManager.getSession().setBudgetMensuelCourant(budget);
			if(c.get(Calendar.MONTH) == oldMois && idCompte.equals(oldIdCompte)){
				refreshAllTable = false;
				LOGGER.info("[BUDGET] Pas de changement de mois ou de compte : Pas de refresh total des tableaux");
			}
			else{
				LOGGER.debug("[BUDGET] Changement de mois ou de compte : Refresh total des tableaux");
				refreshAllTable = true;
			}
			oldMois = c.get(Calendar.MONTH);
			oldAnnee = c.get(Calendar.YEAR);
			oldIdCompte = idCompte;
			return budget;
		} catch (BudgetNotFoundException e) {
			String messageerreur = new StringBuilder().
					append("Impossible de charger le budget du compte ").
					append(idCompte).append(" du ").
					append(c.get(Calendar.MONTH)).append("/").append(c.get(Calendar.YEAR)).toString();
			LOGGER.error(messageerreur);
			throw new DataNotFoundException(messageerreur);
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
			Calendar c = Calendar.getInstance();
			c.setTime(getComponent().getMois().getValue());
			c.set(Calendar.MONTH, this.oldMois);
			c.set(Calendar.YEAR, this.oldAnnee);
			LOGGER.warn("[BUDGET] Budget non trouvé. Réinjection de {}", c.getTime());
			getComponent().getMois().setValue(c.getTime());
			Notification.show(e.getMessage(), Notification.Type.WARNING_MESSAGE);
			return;
		}
		LOGGER.debug("[IHM] >> Mise à jour des vues >> {}", budgetCourant.isActif());		
		LOGGER.debug("[IHM] Affichage des données dans le tableau de suivi des dépenses");
		List<LigneDepense> listeDepenses = budgetCourant.getListeDepenses();		
		/**
		 * Affichage des lignes dans le tableau
		 **/

		tableSuiviDepenseControleur.miseAJourVueDonnees(this.refreshAllTable, budgetCourant.isActif(), listeDepenses);
		if(!budgetCourant.isActif()){
			tableSuiviDepenseControleur.getComponent().setColumnCollapsed(EntetesTableSuiviDepenseEnum.AUTEUR.getId(), false);
			tableSuiviDepenseControleur.getComponent().setColumnCollapsed(EntetesTableSuiviDepenseEnum.ACTIONS.getId(), true);
		}
		else{
			tableSuiviDepenseControleur.getComponent().setColumnCollapsed(EntetesTableSuiviDepenseEnum.AUTEUR.getId(), true);
			tableSuiviDepenseControleur.getComponent().setColumnCollapsed(EntetesTableSuiviDepenseEnum.ACTIONS.getId(), false);
		}

		// Maj du mois
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_MONTH, 1);
		c.set(Calendar.MONTH, budgetCourant.getMois());
		c.set(Calendar.YEAR, budgetCourant.getAnnee());
		getComponent().getMois().setValue(c.getTime());

		// Boutons actions sur Budget inactif :
		if(!budgetCourant.isActif()){
			getComponent().getButtonAnnuler().setVisible(false);
			getComponent().getButtonValider().setVisible(false);
			getComponent().getButtonEditer().setVisible(false);
			getComponent().getButtonCreate().setVisible(false);
			getComponent().getButtonRefreshMonth().setVisible(false);
			getComponent().getButtonLock().setDescription("Réouvrir le budget mensuel");
			getComponent().getButtonLock().setStyleName("locked");
		}
		// Boutons actions sur Budget actif :
		else{
			getComponent().getButtonCreate().setVisible(true);
			getComponent().getButtonEditer().setVisible(true);
			getComponent().getButtonRefreshMonth().setVisible(true);
			getComponent().getButtonLock().setDescription("Finaliser le budget mensuel");
			getComponent().getButtonLock().setStyleName("unlocked");
		}

		/** 
		 * Affichage par catégorie
		 */
		LOGGER.debug("[IHM] Total par categorie");
		treeResumeControleur.miseAJourVueDonnees(budgetCourant, getMaxDateListeDepenses(listeDepenses, budgetCourant.getMois(), budgetCourant.getAnnee()));
		/**
		 * Affichage du résumé
		 */
		tableTotalResumeControleur.miseAJourVueDonnees(budgetCourant, getMaxDateListeDepenses(listeDepenses, budgetCourant.getMois(), budgetCourant.getAnnee()));

		LOGGER.debug("[IHM] << Mise à jour des vues <<");
		this.refreshAllTable = false;
	}


	/**
	 * @param listeDepenses
	 * @return date max d'une liste de dépenses
	 */
	private Calendar getMaxDateListeDepenses(List<LigneDepense> listeDepenses, int moisBudgetCourant, int anneeBudgetCourant){
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, anneeBudgetCourant);
		c.set(Calendar.MONTH, moisBudgetCourant);
		c.set(Calendar.DAY_OF_MONTH, 1);
		if(listeDepenses != null){
			for (LigneDepense ligneDepense : listeDepenses) {
				if(ligneDepense.getDateOperation() != null && c.getTime().before(ligneDepense.getDateOperation())){
					c.setTime(ligneDepense.getDateOperation());
				}
			}
		}
		return c;
	}
}
