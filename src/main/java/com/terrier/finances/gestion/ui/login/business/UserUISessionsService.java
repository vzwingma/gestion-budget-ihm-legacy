package com.terrier.finances.gestion.ui.login.business;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.terrier.finances.gestion.ui.communs.abstrait.IUIControllerService;
import com.terrier.finances.gestion.ui.login.model.UserUISession;
import com.vaadin.ui.UI;

/**
 * Gestionnaire des UI par Session utilisateur
 * @author vzwingma
 *
 */
@Service
public class UserUISessionsService implements IUIControllerService {
	/**
	 * Logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(UserUISessionsService.class);

	// Gestionnaire des composants UI
	private ConcurrentHashMap<String, UserUISession> sessionsMap = new ConcurrentHashMap<>();

	private ScheduledThreadPoolExecutor pool;


	/**
	 * Démarrage du controle des sessions
	 */
	@PostConstruct
	public void startSessionsControl(){
		pool = new ScheduledThreadPoolExecutor(1);
		pool.scheduleAtFixedRate(() -> {
			List<String> idsInvalide = sessionsMap.values()
					.parallelStream()
				/*	.peek(session -> LOGGER.debug(" > {} : active : {}. Dernière activité : {}. Date de validité : {} :  Valide : {}", 
							session.getId(), 
							session.isActive(), 
							session.getLastAccessTime(), 
							session.getValiditeSession(),
							!session.getLastAccessTime().isBefore(session.getValiditeSession()))) */
					.filter(session -> !session.getLastAccessTime().isBefore(session.getValiditeSession()))
					.map(UserUISession::getId)
					.collect(Collectors.toList());

			idsInvalide.parallelStream().forEach(key -> deconnexionUtilisateur(key));
		}, 0, 1, TimeUnit.MINUTES);
	}


	/**
	 * Arrêt du controle des sessions
	 */
	@PreDestroy
	public void stopSessionsControl(){
		this.pool.shutdown();
	}

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
		session.setLastAccessTime(Instant.now());
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
	 * Déconnexion de l'utilisateur
	 */
	public void deconnexionUtilisateur(String idSession){
		LOGGER.warn("[idSession={}] Déconnexion de l'utilisateur ", idSession);

		UserUISession session = sessionsMap.get(idSession);
		session.deconnexionAndRedirect();
		sessionsMap.remove(idSession);
	}


	/**
	 * @return le nombre de sessions actives soit utilisateur authentifié
	 */
	public long getNombreSessionsActives(){
		return sessionsMap.values().stream().filter(UserUISession::isActive).count();
	}

}
