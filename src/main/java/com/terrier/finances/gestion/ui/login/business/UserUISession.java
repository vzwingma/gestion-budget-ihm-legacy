package com.terrier.finances.gestion.ui.login.business;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.terrier.finances.gestion.communs.budget.model.BudgetMensuel;
import com.terrier.finances.gestion.communs.utilisateur.enums.UtilisateurDroitsEnum;
import com.terrier.finances.gestion.ui.communs.abstrait.ui.AbstractUIController;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinSession;
import com.vaadin.server.WrappedSession;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Layout;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

/**
 * Session Utilisateur dans l'application, coté IHM
 * Lien vers les données métier et composants graphiques
 * @author vzwingma
 *
 */
public class UserUISession {
	/**
	 * Logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(UserUISession.class);

	private String idSession;

	private Instant lastAccessTime;
	/**
	 * Utilisateur courant
	 */
	private String idUtilisateur = null;
	
	private Map<UtilisateurDroitsEnum, Boolean> droits;
	/**
	 * Budget courant
	 */
	private BudgetMensuel budgetMensuelCourant;

	@SuppressWarnings("rawtypes")
	private Map<Class, AbstractUIController<? extends AbstractComponent>> mapControleurs = new HashMap<>();
	// Page principale
	private Layout mainLayout;

	private Window popupModale;
	
	
	
	/**
	 * Session Manager
	 * @param idSession idSessions
	 */
	public UserUISession(String idSession){
		LOGGER.trace("[INIT][{}] Session UI ", idSession);
		this.idSession = idSession;
		this.lastAccessTime = Instant.now();
	}


	/**
	 * Déconnexion de l'utilisateur manuellement
	 */
	protected void deconnexionAndRedirect(){
		// Déconnexion métier et invalidation Session Vaadin
		deconnexion();
		//Redirect the user to the login/default Page
		Page currentPage = Page.getCurrent();
		VaadinServlet currentServlet = VaadinServlet.getCurrent();
		if(currentPage != null && currentServlet != null && currentServlet.getServletContext() != null && currentServlet.getServletContext().getContextPath() != null){
			currentPage.setLocation(currentServlet.getServletContext().getContextPath()+"/ihm");
		}
		else{
			LOGGER.error("Erreur : Impossible de trouver la page courante. Pb de framework Vaadin");
		}
	}

	/**
	 * Auto déconnexion, sans redirection
	 */
	protected void deconnexion(){
		// Suppression de l'utilisateur
		this.idUtilisateur = null;
		this.droits = null;
		this.lastAccessTime = null;
		this.budgetMensuelCourant = null;
		this.idSession = null;
		// Suppression de l'IHM
		getMainLayout().removeAllComponents();
		// Suppression de tous les controleurs
		this.mapControleurs.clear();

		// Invalidate Sessions
		VaadinSession vSession = VaadinSession.getCurrent();
		if(vSession != null){
			vSession.close();
			WrappedSession httpSession = vSession.getSession();
			//Invalidate HttpSession
			httpSession.invalidate();
		}
	}


	/**
	 * Enregistrement des controleurs
	 * @param controleur controleur à enregistrer
	 */
	public <C extends AbstractUIController<? extends AbstractComponent>> void registerUIControler(C controleur) {
		if(mapControleurs.get(controleur.getClass()) == null){
			LOGGER.info("[{}] Enregistrement du controleur : {}", this.idSession, controleur.getClass().getSimpleName());
			mapControleurs.put(controleur.getClass(), controleur);
		}

	}


	/**
	 * @return the mapControleurs
	 */
	@SuppressWarnings("unchecked")
	public <C extends AbstractUIController<? extends CustomComponent>> C getControleur(Class<C> classNameControleur) {
		return (C) mapControleurs.get(classNameControleur);
	}

	/**
	 * Enregistrement de l'utilisateur
	 * @param utilisateur USER
	 */
	public boolean registerUtilisateur(String utilisateur, Map<UtilisateurDroitsEnum, Boolean> droits){
		LOGGER.info("[{}] Enregistrement de l'utilisateur : {}", idSession, utilisateur);
		this.idUtilisateur = utilisateur;
		this.droits = droits;
		return true;
	}


	/**
	 * @return the utilisateurCourant
	 */
	public String getIdUtilisateur() {
		return idUtilisateur;
	}



	/**
	 * @param lastAccessTime the lastAccessTime to set
	 */
	public void setLastAccessTime(Instant lastAccessTime) {
		this.lastAccessTime = lastAccessTime;
	}


	/**
	 * @return the lastAccessTime
	 */
	public Instant getLastAccessTime() {
		return lastAccessTime;
	}


	/**
	 * @return session active
	 */
	public boolean isActive(){
		return this.idUtilisateur != null && this.budgetMensuelCourant != null;
	}


	/**
	 * @param cleDroit
	 * @return le résultat
	 */
	public boolean isEnabled(UtilisateurDroitsEnum cleDroit){
		if(this.droits != null){
			Boolean droit = this.droits.get(cleDroit);
			return droit != null && droit.booleanValue();
		}
		return false;
	}
	
	/**
	 * @return the idSession
	 */
	public String getId() {
		return idSession;
	}

	/**
	 * @return the budgetMensuelCourant
	 */
	public BudgetMensuel getBudgetCourant() {
		return budgetMensuelCourant;
	}

	/**
	 * @param budgetMensuelCourant the budgetMensuelCourant to set
	 */
	public void updateBudgetInSession(BudgetMensuel budgetMensuelCourant) {
		this.budgetMensuelCourant = budgetMensuelCourant;
	}



	/**
	 * @return le mainLayout
	 */
	public Layout getMainLayout() {
		return mainLayout;
	}

	/**
	 * @param mainLayout to set
	 */
	public void setMainLayout(Layout mainLayout) {
		this.mainLayout = mainLayout;
	}

	/**
	 * @return the popupModale
	 */
	public Window getPopupModale() {
		return popupModale;
	}

	/**
	 * @param popupModale the popupModale to set
	 */
	public void setPopupModale(Window popupModale) {
		UI.getCurrent().addWindow(popupModale);
		this.popupModale = popupModale;
	}
}