/**
 * 
 */
package com.terrier.finances.gestion.ui.resume.totaux.ui;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.terrier.finances.gestion.communs.budget.model.BudgetMensuel;
import com.terrier.finances.gestion.communs.budget.model.TotalBudgetMensuel;
import com.terrier.finances.gestion.communs.utils.data.DataUtils;
import com.terrier.finances.gestion.ui.communs.abstrait.ui.AbstractUIController;
import com.terrier.finances.gestion.ui.operations.model.enums.EntetesGridResumeOperationsEnum;

/**
 * Controleur du tableau des résumés
 * @author vzwingma
 *
 */
public class GridResumeTotauxController extends AbstractUIController<GridResumeTotaux>{


	private static final long serialVersionUID = 5190668755144306669L;
	
	public static final DateTimeFormatter auDateFormat = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.FRENCH);
	public static final DateTimeFormatter finDateFormat = DateTimeFormatter.ofPattern("MMM yyyy", Locale.FRENCH);

	
	/**
	 * @param composant
	 */
	public GridResumeTotauxController(GridResumeTotaux composant) {
		super(composant);
	}


	@Override
	public void miseAJourVueDonnees() { 
		// Rien, cf #miseAJourVueDonnees(BudgetMensuel budget)

	}
	

	/**
	 * Mise à jour des données suite au budget
	 * @param refreshAllTable refresh total en cas de changemetn de mois ou de compte
	 * @param budget budget à jour
	 * @param dateBudget date du budget
	 */
	public void miseAJourVueDonnees(BudgetMensuel budget){

		LocalDate dateDerniereOperation = DataUtils.getMaxDateListeOperations(budget.getListeOperations());
		
		// Injection des données
		List<TotalBudgetMensuel> totauxBudget = new ArrayList<>();
		totauxBudget.add(new TotalBudgetMensuel("Solde prévu", budget.getSoldeNow(), budget.getSoldeFin()));
		totauxBudget.add(new TotalBudgetMensuel("Solde réel ", budget.getSoldeReelNow(), budget.getSoldeReelFin()));
		
		// Maj des colonnes
		getComponent().getColumn(EntetesGridResumeOperationsEnum.VALEUR_NOW.getId()).setCaption(EntetesGridResumeOperationsEnum.VALEUR_NOW.getLibelle()+ dateDerniereOperation.format(auDateFormat));
		getComponent().getColumn(EntetesGridResumeOperationsEnum.VALEUR_FIN.getId()).setCaption(EntetesGridResumeOperationsEnum.VALEUR_FIN.getLibelle()+ dateDerniereOperation.format(finDateFormat));
		getComponent().setItems(totauxBudget);
		getComponent().getDataProvider().refreshAll();
		getComponent().setDescription("Marge de sécurité : "+budget.getMargeSecurite()+" €");
	}

}
