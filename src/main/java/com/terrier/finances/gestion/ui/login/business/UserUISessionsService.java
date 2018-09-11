package com.terrier.finances.gestion.ui.login.business;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.terrier.finances.gestion.ui.communs.abstrait.ui.IUIControleurService;
import com.vaadin.ui.UI;

/**
 * Gestionnaire des UI par Session utilisateur
 * @author vzwingma
 *
 */
@Service
public class UserUISessionsService implements Runnable, IUIControleurService {
	/**
	 * Logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(UserUISessionsService.class);

	// Gestionnaire des composants UI
	private ConcurrentHashMap<String, UserUISession> sessionsMap = new ConcurrentHashMap<>();

	private ScheduledThreadPoolExecutor pool;

	private int sessionValidity = 10; 

	@Value("${budget.ui.session.validity.period:10}")
	public void setUiValiditySessionPeriod(String sessionValidity){
		try{
			this.sessionValidity = Integer.parseInt(sessionValidity);
			LOGGER.info("Suivi des sessions utilisateurs. Durée de validité d'une session : {} minutes", sessionValidity);
		}
		catch(Exception e){
			LOGGER.warn("Suivi des sessions utilisateurs. Durée de validité par défaut d'une session : {} minutes", sessionValidity);
		}
	}

	/**
	 * Démarrage du controle des sessions
	 */
	@PostConstruct
	public void startSessionsControl(){
		pool = new ScheduledThreadPoolExecutor(1);
		pool.scheduleAtFixedRate(this, 0, 1, TimeUnit.MINUTES);
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
		// Création d'une nouvelle session si nécessaire
		sessionsMap.putIfAbsent(idSession, new UserUISession(idSession));
		UserUISession session = sessionsMap.get(idSession);
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
		else{
			LOGGER.warn("[TEST] ***** id session de test  ***** ");
			idSession = "TEST";
		}
		return idSession;
	}
	
	
	/**
	 * Déconnexion de l'utilisateur
	 */
	public void deconnexionUtilisateur(String idSession, boolean redirect){
		UserUISession session = sessionsMap.get(idSession);
		getServiceUtilisateurs().deconnexion(session.getIdUtilisateur());
		if(redirect){
			session.deconnexionAndRedirect();
		}
		else{
			session.deconnexion();
		}
		sessionsMap.remove(idSession);
		
	}


	/**
	 * @return le nombre de sessions actives soit utilisateur authentifié
	 */
	public long getNombreSessionsActives(){
		return sessionsMap.values().stream().filter(UserUISession::isActive).count();
	}


	/**
	 * Vérification des sessions
	 */
	@Override
	public void run() {
		final Instant validiteSession  = Instant.now().minus(sessionValidity, ChronoUnit.MINUTES);
		List<String> idsInvalide = sessionsMap.values()
			.parallelStream()
//			.peek(session -> LOGGER.debug(" > {} : active : {}. Dernière activité : {}. Valide : {}", session.getId(), session.isActive(), session.getLastAccessTime(), !session.getLastAccessTime().isBefore(validiteSession)))
			.filter(session -> session.getLastAccessTime().isBefore(validiteSession))
			.map(UserUISession::getId)
			.collect(Collectors.toList());
		idsInvalide.parallelStream().forEach(key -> deconnexionUtilisateur(key, false));
	}
}
