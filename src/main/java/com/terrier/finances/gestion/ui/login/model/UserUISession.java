package com.terrier.finances.gestion.ui.login.model;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.terrier.finances.gestion.communs.api.security.JwtConfigEnum;
import com.terrier.finances.gestion.communs.budget.model.BudgetMensuel;
import com.terrier.finances.gestion.communs.utilisateur.enums.UtilisateurDroitsEnum;
import com.terrier.finances.gestion.ui.communs.abstrait.AbstractUIController;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Layout;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.SignatureException;

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
	/*
	 * JWT
	 */
	private String jwtToken;

	private Claims jwtClaims;
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
		LOGGER.trace("[INIT][idSession={}] Session UI ", idSession);
		this.idSession = idSession;
		this.lastAccessTime = Instant.now();
	}



	/**
	 * Auto déconnexion, sans redirection
	 */
	public void clearValues(){
		// Purge
		this.jwtClaims = null;
		this.jwtToken = null;
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
	 * Enregistrement de l'utilisateur
	 * @param idUtilisateur USER
	 */
	public boolean registerUtilisateur(String token){
		this.jwtToken = token;
		try {
			this.jwtClaims = JwtConfigEnum.getJWTClaims(token);
			if(this.jwtClaims != null) {
				LOGGER.info("[idSession={}] Enregistrement de l'utilisateur : {}", idSession, this.jwtClaims.get(JwtConfigEnum.JWT_CLAIM_HEADER_USERID));
			}
		}
		catch (SignatureException e) {
			LOGGER.warn("[idSession={}] Le token est mal signé [{}]", idSession, token);
			this.jwtClaims = null;
		}
		return this.jwtClaims != null;
	}



	/**
	 * @param lastAccessTime the lastAccessTime to set
	 */
	public void setLastAccessTime(Instant lastAccessTime) {
		this.lastAccessTime = lastAccessTime;
	}

	public Instant getValiditeSession() {
		return this.jwtClaims.getExpiration().toInstant();
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
		return this.jwtToken != null && this.budgetMensuelCourant != null;
	}


	/**
	 * @param cleDroit
	 * @return le résultat
	 */
	public boolean isEnabled(UtilisateurDroitsEnum cleDroit){
		if(getJwtClaims() != null){
			return getJwtClaims().get(JwtConfigEnum.JWT_CLAIM_HEADER_AUTORITIES, List.class).contains(cleDroit.name());
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


	/**
	 * @return the jwtToken
	 */
	public String getJwtToken() {
		return jwtToken;
	}


	/**
	 * @return the jwtClaims
	 */
	public Claims getJwtClaims() {
		return jwtClaims;
	}
}
