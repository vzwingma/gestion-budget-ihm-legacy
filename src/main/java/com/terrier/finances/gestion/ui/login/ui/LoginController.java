/**
 * 
 */
package com.terrier.finances.gestion.ui.login.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.terrier.finances.gestion.communs.utils.exceptions.DataNotFoundException;
import com.terrier.finances.gestion.communs.utils.exceptions.UserNotAuthorizedException;
import com.terrier.finances.gestion.services.abstrait.api.IAPIClient;
import com.terrier.finances.gestion.services.admin.model.Info;
import com.terrier.finances.gestion.ui.budget.ui.BudgetMensuelPage;
import com.terrier.finances.gestion.ui.communs.abstrait.AbstractUIController;
import com.terrier.finances.gestion.ui.login.listeners.ActionConnexionClickListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;

import reactor.core.publisher.Mono;

/**
 * Controleur du login
 * @author vzwingma
 *
 */
public class LoginController extends AbstractUIController<Login>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7301428518502835422L;

	/**
	 * Logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);


	/**
	 * Démarrage du contoleur
	 * @param composant
	 */
	public LoginController(Login composant) {
		super(composant);

		initDynamicComponentsOnPage();
	}

	/* (non-Javadoc)
	 * @see com.terrier.finances.gestion.ui.controler.common.AbstractUIController#initDynamicComponentsOnPage()
	 */
	public void initDynamicComponentsOnPage() {
		// Ajout controle
		getComponent().getButtonConnexion().addClickListener(new ActionConnexionClickListener(this));
		getComponent().getPasswordField().focus();
	}



	/* (non-Javadoc)
	 * @see com.terrier.finances.gestion.ui.controler.common.AbstractUIController#miseAJourVueDonnees()
	 */
	@Override
	public void miseAJourVueDonnees() {

		LOGGER.info("Démarrage de l'application [{}][{}]", getServiceParams().getVersion(), getServiceParams().getBuildTime());

		getComponent().getPasswordField().setIcon(new ThemeResource("img/passwd.png"));
		getComponent().getLabelVersion().setValue("Version IHM : " + getServiceParams().getVersion());
		getComponent().getLabelBuildTime().setValue("Build : " + getServiceParams().getBuildTime());

		addVersion(getComponent().getLabelVersionComptes(), getServiceComptes());
		addVersion(getComponent().getLabelVersionOperations(), getServiceOperations());
		addVersion(getComponent().getLabelVersionParametres(), getServiceParams());
		addVersion(getComponent().getLabelVersionUtilisateurs(), getServiceUtilisateurs());
	}

	
	String version = "N/A";
	/**
	 * @param label label à completer
	 * @param apiService service
	 */
	private void addVersion(Label label, IAPIClient apiService) {
		
		Mono<Info> mono;
		try {
			mono = apiService.getInfo();
			mono.subscribe(c -> {
				if(c.getApp() != null && c.getApp().getVersion() != null) {
					version = c.getApp().getVersion();	
				}
				label.setValue(label.getValue() + ":" +  version);
			});
		} catch (DataNotFoundException | UserNotAuthorizedException e) {
			LOGGER.error("Erreur sur la réception des versions");
			label.setValue(label.getValue() + ":" + version);
		}

	}


	/**
	 * Méthode d'authenticiation de l'utilisateur
	 * @param login de l'utilisateur
	 * @param accessTokenEnClair en clair de l'utilisateur
	 */
	public void authenticateUser(String accessToken){

		if(accessToken != null){
			getUserSession().setAccessToken(accessToken);
			LOGGER.info("Accès autorisé pour {}", accessToken);
			getServiceUserSessions().enregistrementUtilisateur();
			// MAJ
			getUserSession().getMainLayout().removeAllComponents();
			getUserSession().getMainLayout().addComponent(new BudgetMensuelPage());
		}
		else{
			LOGGER.error("****************** ECHEC AUTH ***********************");
			Notification.show("Les login et mot de passe sont incorrects", Notification.Type.ERROR_MESSAGE);
		}
	}
	

}
