/**
 * 
 */
package com.terrier.finances.gestion.ui.budget.listeners;

import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import com.terrier.finances.gestion.communs.budget.model.BudgetMensuel;
import com.terrier.finances.gestion.communs.utils.data.DataUtils;
import com.terrier.finances.gestion.ui.budget.ui.BudgetMensuelPage;
import com.terrier.finances.gestion.ui.communs.abstrait.listeners.AbstractComponentListener;
import com.terrier.finances.gestion.ui.communs.ui.ConfirmDialog;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

/**
 * Refresh du mois courant
 * @author vzwingma
 *
 */
public class ActionRefreshMonthBudgetClickListener extends AbstractComponentListener implements ClickListener, ConfirmDialog.ConfirmationDialogCallback {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1823872638217135776L;

	private BudgetMensuelPage page;

	/* (non-Javadoc)
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {

		page  = (BudgetMensuelPage)event.getButton().getParent().getParent().getParent().getParent().getParent();


		BudgetMensuel budgetMensuelCourant = getUserSession().getBudgetCourant();
		String moisAffiche = DataUtils.localDateFirstDayOfMonth(budgetMensuelCourant.getMois()).format(DateTimeFormatter.ofPattern("MMMM YYYY", Locale.FRENCH));

		/** Alerte **/
		String warnMoisActif = "";
		Month moisPrecedent = budgetMensuelCourant.getMois().minus(1);
		int anneePrecedente = Month.DECEMBER.equals(moisPrecedent) ? budgetMensuelCourant.getAnnee() - 1 : budgetMensuelCourant.getAnnee();
		
		Boolean budgetPrecedentActif = page.getControleur().getServiceOperations().isBudgetMensuelActif(
				budgetMensuelCourant.getCompteBancaire().getId(), 
				moisPrecedent, anneePrecedente);
		if(budgetPrecedentActif){
			warnMoisActif = "<span style=\"color: red;\"><br> Attention : Le mois précédent n'est pas clos !</span><br>Il sera automatiquement clôturé et les opérations en attente, annulées";
		}

		// Confirmation
		ConfirmDialog confirm = new ConfirmDialog("Réinitialisation du budget mensuel courant", 
				"Voulez vous réinitialiser le budget du mois de "+ moisAffiche+" ? " +
						warnMoisActif, "Oui", "Non", this);
		confirm.setWidth("400px");
		confirm.setHeight("150px");
		setPopupModale(confirm);		
	}



	/* (non-Javadoc)
	 * @see com.terrier.finances.gestion.ui.components.confirm.ConfirmDialog.ConfirmationDialogCallback#response(boolean)
	 */
	@Override
	public void response(boolean ok) {
		if(ok){
			page.getControleur().reinitialiserBudgetCourant();
		}
	}
}

