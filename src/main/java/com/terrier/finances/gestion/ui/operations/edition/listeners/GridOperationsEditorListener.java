package com.terrier.finances.gestion.ui.operations.edition.listeners;

import com.terrier.finances.gestion.communs.budget.model.v12.BudgetMensuel;
import com.terrier.finances.gestion.communs.operations.model.v12.LigneOperation;
import com.terrier.finances.gestion.communs.utils.exceptions.BudgetNotFoundException;
import com.terrier.finances.gestion.communs.utils.exceptions.DataNotFoundException;
import com.terrier.finances.gestion.communs.utils.exceptions.UserNotAuthorizedException;
import com.terrier.finances.gestion.ui.operations.ui.GridOperationsController;
import com.vaadin.ui.Notification;
import com.vaadin.ui.components.grid.EditorCancelEvent;
import com.vaadin.ui.components.grid.EditorCancelListener;
import com.vaadin.ui.components.grid.EditorOpenEvent;
import com.vaadin.ui.components.grid.EditorOpenListener;
import com.vaadin.ui.components.grid.EditorSaveEvent;
import com.vaadin.ui.components.grid.EditorSaveListener;

/**
 * Listener de l'éditor de la grille des opérations
 * @author vzwingma
 *
 */
public class GridOperationsEditorListener implements EditorCancelListener<LigneOperation>, EditorSaveListener<LigneOperation>, EditorOpenListener<LigneOperation> {

	//
	private static final long serialVersionUID = -4092876167681783200L;

	// Controleur
	private GridOperationsController controler;

	public GridOperationsEditorListener(GridOperationsController controller) {
		this.controler = controller;
	}


	@Override
	public void onEditorCancel(EditorCancelEvent<LigneOperation> event) {
		this.controler.updateViewGridOnEditableMode(false);
	}

	/* (non-Javadoc)
	 * @see com.vaadin.ui.components.grid.EditorOpenListener#onEditorOpen(com.vaadin.ui.components.grid.EditorOpenEvent)
	 */
	@Override
	public void onEditorOpen(EditorOpenEvent<LigneOperation> event) {
		this.controler.updateViewGridOnEditableMode(true);
	}

	/* (non-Javadoc)
	 * @see com.vaadin.ui.components.grid.EditorSaveListener#onEditorSave(com.vaadin.ui.components.grid.EditorSaveEvent)
	 */	
	@Override
	public void onEditorSave(EditorSaveEvent<LigneOperation> event) {
		try {
			// Recalcul du budget
			BudgetMensuel budget = this.controler.getServiceOperations().majLigneOperation(this.controler.getUserSession().getBudgetCourant().getId(), event.getBean());
			this.controler.getUserSession().updateBudgetInSession(budget);
			// MAJ des tableaux
			this.controler.getBudgetControleur().miseAJourVueDonnees();
			Notification.show("L'opération a bien été mise à jour", Notification.Type.TRAY_NOTIFICATION);
			this.controler.updateViewGridOnEditableMode(false);
		} catch (DataNotFoundException | BudgetNotFoundException | UserNotAuthorizedException e) {
			Notification.show("Erreur lors de la mise à jour de l'opération \n [" + e.getMessage() + "]", Notification.Type.WARNING_MESSAGE);
		}
		
	}
}

