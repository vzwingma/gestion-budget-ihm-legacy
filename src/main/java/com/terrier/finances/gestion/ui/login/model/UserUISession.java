package com.terrier.finances.gestion.ui.login.model;

import com.terrier.finances.gestion.communs.budget.model.v12.BudgetMensuel;
import com.terrier.finances.gestion.communs.utilisateur.enums.UtilisateurDroitsEnum;
import com.terrier.finances.gestion.communs.utilisateur.enums.UtilisateurPrefsEnum;
import com.terrier.finances.gestion.ui.communs.abstrait.AbstractUIController;
import com.vaadin.ui.*;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;


/**
 * Session Utilisateur dans l'application, coté IHM
 * Lien vers les données métier et composants graphiques
 * @author vzwingma
 *
 */
@Getter @Setter
public class UserUISession {
	/**
	 * Logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(UserUISession.class);

	private String idSession;

	// Configuration Session
	private Instant lastAccessTime;
	
	private Map<UtilisateurDroitsEnum, Boolean> droits;
	
	private Map<UtilisateurPrefsEnum, String> preferences;
	
	private String actionUserCorrId = null;
	/*
	 * JWT
	 */
	private String accessToken;
	/**
	 * Budget courant
	 */
	private BudgetMensuel budgetMensuelCourant;

	private Map<Class<?>, AbstractUIController<? extends AbstractComponent>> mapControleurs = new HashMap<>();
	// Page principale
	private Layout mainLayout;

	private Window popupModale;



	/**
	 * Session Manager
	 * @param idSession idSessions
	 */
	public UserUISession(String idSession){
		LOGGER.trace("[INIT][idSession={}] Session UI ", idSession);
		this.idSession = idSession;
		this.lastAccessTime = Instant.now();
	}



	/**
	 * Auto déconnexion, sans redirection
	 */
	public void clearValues(){
		// Purge
		this.lastAccessTime = null;
		this.budgetMensuelCourant = null;
		this.idSession = null;
		// Suppression de l'IHM
		getMainLayout().removeAllComponents();
		// Suppression de tous les controleurs
		this.mapControleurs.clear();
	}


	/**
	 * Enregistrement des controleurs
	 * @param controleur controleur à enregistrer
	 */
	public <C extends AbstractUIController<? extends AbstractComponent>> void registerUIControler(C controleur) {
		if(mapControleurs.get(controleur.getClass()) == null){
			LOGGER.debug("[idSession={}] Enregistrement du controleur : {}", this.idSession, controleur.getClass().getSimpleName());
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
	 * @param budgetMensuelCourant the budgetMensuelCourant to set
	 */
	public void updateBudgetInSession(BudgetMensuel budgetMensuelCourant) {
		this.budgetMensuelCourant = budgetMensuelCourant;
	}
	
	/**
	 * Enregistrement de l'utilisateur
	 * @param token USER
	 */
	public boolean setAccessToken(String token){
		this.accessToken = token;
		LOGGER.info("[idSession={}] Enregistrement de l'utilisateur : {}", idSession, this.accessToken);
		return this.accessToken != null;
	}

	/**
	 * @return session active
	 */
	public boolean isActive(){
		return this.accessToken != null && this.budgetMensuelCourant != null;
	}


	/**
	 * @param cleDroit
	 * @return le résultat
	 */
	public boolean isEnabled(UtilisateurDroitsEnum cleDroit){
		return getDroits().getOrDefault(cleDroit, Boolean.FALSE);
	}


	/**
	 * @param popupModale the popupModale to set
	 */
	public void setPopupModale(Window popupModale) {
		UI.getCurrent().addWindow(popupModale);
		this.popupModale = popupModale;
	}
}
