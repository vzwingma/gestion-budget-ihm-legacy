package com.terrier.finances.gestion.ui.budget.ui;

import javax.servlet.annotation.WebServlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.terrier.finances.gestion.communs.utils.config.CorrelationsIdUtils;
import com.terrier.finances.gestion.services.FacadeServices;
import com.terrier.finances.gestion.ui.login.ui.Login;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * IHM FINANCES
 * @author vzwingma
 *
 */
@Theme("mytheme")
@Title("Gestion de budget")
public class FinancesVaadinUI extends UI
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3105864589672927628L;

	/**
	 * Logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(FinancesVaadinUI.class);


	@WebServlet(value = {"/*", "/VAADIN/*"}, asyncSupported = true)
	@VaadinServletConfiguration(productionMode = true, 
								ui = FinancesVaadinUI.class, 
								widgetset = "com.terrier.finances.gestion.AppWidgetSet")
	public static class Servlet extends VaadinServlet {

		/**
		 * 
		 */
		private static final long serialVersionUID = -1941895602784719745L;
	}

	@Override
	protected void init(VaadinRequest request) {
		CorrelationsIdUtils.putCorrIdOnMDC(FacadeServices.get().getInitCorrId());
		LOGGER.debug("FinancesVaadinUI - IdSession : {}", this.getSession().getCsrfToken());
		final VerticalLayout layout = new VerticalLayout();
		layout.setMargin(false);
		layout.setSizeFull();
		setContent(layout);
		setSizeFull();
		FacadeServices.get().getServiceUserSessions().getSession().setMainLayout(layout);
		UI.setCurrent(this);

		// Refresh
		int pollInterval = Integer.parseInt(FacadeServices.get().getServiceParams().getUiRefreshPeriod());
		UI.getCurrent().setPollInterval(pollInterval);
		LOGGER.debug("FinancesVaadinUI - PoolInterval de {} ms", pollInterval);

		if(!FacadeServices.get().getServiceUserSessions().getSession().isActive()){
			// Page de login au démarrage si non authentifié
			layout.addComponent(new Login());
		}
		else{
			// Sinon la page en cours
			layout.addComponent(new BudgetMensuelPage());
		}
	}
}
