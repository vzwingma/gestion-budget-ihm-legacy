package com.terrier.finances.gestion.ui.operations.edition.listeners;

import com.terrier.finances.gestion.communs.operations.model.LigneOperation;
import com.terrier.finances.gestion.communs.utils.exceptions.DataNotFoundException;
import com.terrier.finances.gestion.ui.operations.ui.GridOperationsController;
import com.vaadin.contextmenu.GridContextMenu.GridContextMenuOpenListener;
import com.vaadin.ui.Notification;

/**
 * 
 * Action sur une opération du tableau pour la déclarer dernière opération
 * @author vzwingma
 *
 */
public class GridOperationsRightClickListener implements GridContextMenuOpenListener<LigneOperation>{


	/**
	 * 
	 */
	private static final long serialVersionUID = -6541406098405268390L;

	// Controleur
	private GridOperationsController controleur;
	
	
	/**
	 * Constructeur avec le controleur associé
	 * @param controleur
	 */
	public GridOperationsRightClickListener(GridOperationsController controleur){
		this.controleur = controleur;
	}


	@Override
	public void onContextMenuOpen(GridContextMenuOpenEvent<LigneOperation> event) {
		try {
			if(this.controleur != null && this.controleur.getBudgetControleur().setLigneDepenseAsDerniereOperation(event.getItem())){
				Notification.show("L'opération est tagguée comme la dernière opération exécutée", Notification.Type.TRAY_NOTIFICATION);
			}
			else{
				Notification.show("Erreur lors du marquage de l'opération", Notification.Type.WARNING_MESSAGE);
			}
		} catch (DataNotFoundException e) {
			Notification.show("Impossible de marquer l'opération. Veuillez réessayer", Notification.Type.WARNING_MESSAGE);
		}
	}

}
