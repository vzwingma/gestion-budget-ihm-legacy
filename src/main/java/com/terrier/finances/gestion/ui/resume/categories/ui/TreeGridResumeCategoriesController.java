/**
 * 
 */
package com.terrier.finances.gestion.ui.resume.categories.ui;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.terrier.finances.gestion.communs.budget.model.v12.BudgetMensuel;
import com.terrier.finances.gestion.communs.budget.model.v12.TotauxCategorie;
import com.terrier.finances.gestion.communs.parametrages.model.v12.CategorieOperation;
import com.terrier.finances.gestion.communs.utils.data.BudgetDataUtils;
import com.terrier.finances.gestion.ui.communs.abstrait.AbstractUIController;
import com.terrier.finances.gestion.ui.operations.model.enums.EntetesGridResumeOperationsEnum;
import com.terrier.finances.gestion.ui.resume.totaux.ui.GridResumeTotauxController;
import com.vaadin.data.TreeData;
import com.vaadin.data.provider.TreeDataProvider;

/**
 * Controleur du tableau des résumés
 * @author vzwingma
 *
 */
public class TreeGridResumeCategoriesController extends AbstractUIController<TreeGridResumeCategories>{



	//
	private static final long serialVersionUID = 5190668755144306669L;

	/**
	 * Logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(TreeGridResumeCategoriesController.class);

	/**
	 * @param composant
	 */
	public TreeGridResumeCategoriesController(TreeGridResumeCategories composant) {
		super(composant);
	}

	private boolean gridCollapsed = true;


	@Override
	public void miseAJourVueDonnees() { 
		// Rien cf. #miseAJourVueDonnees(BudgetMensuel budget)
	}

	/**
	 * Mise à jour des données suite au budget
	 * @param refreshAllTable refresh total en cas de changemetn de mois ou de compte
	 * @param budget budget à jour
	 * @param dateBudget date du budget
	 */
	public void miseAJourVueDonnees(BudgetMensuel budget){

		// Libellés
		LocalDate dateDerniereOperation = BudgetDataUtils.getMaxDateListeOperations(budget.getListeOperations());
		getComponent().getColumn(EntetesGridResumeOperationsEnum.VALEUR_NOW.getId()).setCaption(EntetesGridResumeOperationsEnum.VALEUR_NOW.getLibelle()+ dateDerniereOperation.format(GridResumeTotauxController.auDateFormat));
		getComponent().getColumn(EntetesGridResumeOperationsEnum.VALEUR_FIN.getId()).setCaption(EntetesGridResumeOperationsEnum.VALEUR_FIN.getLibelle()+ dateDerniereOperation.format(GridResumeTotauxController.finDateFormat));


		// Données des résumés
		@SuppressWarnings("unchecked")
		TreeDataProvider<TotauxCategorie> dataProvider = (TreeDataProvider<TotauxCategorie>) getComponent().getDataProvider();
		TreeData<TotauxCategorie> treeData = dataProvider.getTreeData();
		
		// Tri des catégories
		for (CategorieOperation categorie : getServiceParams().getCategories()) {
			if(categorie != null && budget.getTotauxParCategories().get(categorie.getId()) != null){

				// Ajout de la catégorie en parent
				TotauxCategorie totalCat = budget.getTotauxParCategories().get(categorie.getId());
				treeData.addItem(null, totalCat);
				
				for (CategorieOperation ssCategorie : categorie.getListeSSCategories()) {
					
					TotauxCategorie totalSsCat = budget.getTotauxParSSCategories().get(ssCategorie.getId());
					if(totalSsCat == null){
						TotauxCategorie voidTotaux = new TotauxCategorie();
						voidTotaux.setLibelleCategorie(ssCategorie.getLibelle());
						treeData.addItem(totalCat, voidTotaux);
					}
					else{
						treeData.addItem(totalCat, totalSsCat);
					}
				}
			}
			else{
				LOGGER.trace("Attention : Catégorie vide");
			}
		}
		dataProvider.refreshAll();
		this.gridCollapsed = true;
		collapseExpendTreeGrid();
	}

	/**
	 * Collapse/expand
	 */
	public void collapseExpendTreeGrid(){
		getComponent().getTreeData().getRootItems()
		.stream()
		.forEach(
				categorie -> {
					if(gridCollapsed){
						getComponent().getDataCommunicator().expand(categorie);
					}
					else{
						getComponent().getDataCommunicator().collapse(categorie);
					}
				});
		gridCollapsed = !gridCollapsed;
	}
}
