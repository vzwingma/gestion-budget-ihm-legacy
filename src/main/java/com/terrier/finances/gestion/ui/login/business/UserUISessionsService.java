package com.terrier.finances.gestion.ui.login.business;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.terrier.finances.gestion.communs.utilisateur.model.api.UtilisateurPrefsAPIObject;
import com.terrier.finances.gestion.communs.utils.exceptions.DataNotFoundException;
import com.terrier.finances.gestion.communs.utils.exceptions.UserNotAuthorizedException;
import com.terrier.finances.gestion.services.utilisateurs.api.IUtilisateursAPIService;
import com.terrier.finances.gestion.ui.communs.abstrait.IUIControllerService;
import com.terrier.finances.gestion.ui.login.model.UserUISession;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinSession;
import com.vaadin.server.WrappedSession;
import com.vaadin.ui.UI;

/**
 * Gestionnaire des UI par Session utilisateur
 * @author vzwingma
 *
 */
@Service
public class UserUISessionsService implements IUIControllerService, IUserUISessionsService {
	/**
	 * Logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(UserUISessionsService.class);

	// Gestionnaire des composants UI
	private ConcurrentHashMap<String, UserUISession> sessionsMap = new ConcurrentHashMap<>();

	@Autowired
	private IUtilisateursAPIService usersService;
	
	/**
	 * @return la session utilisateur
	 */
	public UserUISession getSession(){
		String idSession = getUIIdSession();
		UserUISession session = null;
		// Création d'une nouvelle session si nécessaire
		if(sessionsMap.get(idSession) != null) {
			session = sessionsMap.get(idSession);
		}
		else {
			session = new UserUISession(idSession);
			sessionsMap.put(idSession, session);			
		}
		return session;
	}

	
	
	/**
	 * @return l'id de session Vaadin
	 */
	private String getUIIdSession(){
		String idSession = null;
		if(UI.getCurrent() != null && UI.getCurrent().getSession() != null && UI.getCurrent().getSession().getCsrfToken() != null){
			idSession = UI.getCurrent().getSession().getCsrfToken();
		}
		return idSession;
	}


	/**
	 * Enregistrement de la session
	 * @param session
	 */
	public void enregistrementUtilisateur() {
		try {
			LocalDateTime lastDateTime = usersService.getLastAccessTime();
			getUserSession().setLastAccessTime(lastDateTime.atZone(ZoneId.of("Europe/Paris")).toInstant());
		} catch (UserNotAuthorizedException | DataNotFoundException e) {
			getUserSession().setLastAccessTime(Instant.now());
		}
		UtilisateurPrefsAPIObject prefs;
		try {
			prefs = usersService.getPreferenceDroits();
			getUserSession().setDroits(prefs.getDroits());
			getUserSession().setPreferences(prefs.getPreferences());			
		} catch (UserNotAuthorizedException | DataNotFoundException e) {
			LOGGER.error("Erreur lors du chargement des droits de l'utilisateur", e);
		}


	}

	/**
	 * Déconnexion de l'utilisateur
	 */
	public void deconnexionUtilisateur(String idSession){
		LOGGER.warn("[idSession={}] Déconnexion de l'utilisateur ", idSession);

		UserUISession session = sessionsMap.get(idSession);
		// Déconnexion métier et invalidation Session Vaadin
		// Invalidate Sessions
		VaadinSession vSession = VaadinSession.getCurrent();
		if(vSession != null){
			vSession.close();
			WrappedSession httpSession = vSession.getSession();
			//Invalidate HttpSession
			httpSession.invalidate();
		}
		session.clearValues();
		//Redirect the user to the login/default Page
		Page currentPage = session.getMainLayout().getUI().getPage();
		VaadinServlet currentServlet = VaadinServlet.getCurrent();
		if(currentPage != null && currentServlet != null && currentServlet.getServletContext() != null && currentServlet.getServletContext().getContextPath() != null){
			LOGGER.debug("Redirection vers {}", currentServlet.getServletContext().getContextPath());
			currentPage.setLocation(currentServlet.getServletContext().getContextPath());
		}
		else{
			LOGGER.error("Erreur : Impossible de trouver la page courante. Pb de framework Vaadin");
		}
		sessionsMap.remove(idSession);
	}

	/**
	 * @return le nombre de sessions actives soit utilisateur authentifié
	 */
	public long getNombreSessionsActives(){
		return sessionsMap.values().stream().filter(UserUISession::isActive).count();
	}

}
