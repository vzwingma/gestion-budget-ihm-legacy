package com.terrier.finances.gestion.ui.operations.actions.ui.renderers;

import com.terrier.finances.gestion.services.budget.business.OperationsService;
import com.terrier.finances.gestion.ui.operations.actions.ui.ActionsOperation;
import com.vaadin.ui.Component;
import com.vaadin.ui.renderers.ComponentRenderer;

import elemental.json.JsonValue;

/**
 * Affichage des boutons des actions sauf pour la réserve
 * @author vzwingma
 *
 */
public class ActionsOperationRenderer extends ComponentRenderer {

	/* (non-Javadoc)
	 * @see com.vaadin.ui.renderers.ComponentRenderer#encode(com.vaadin.ui.Component)
	 */
	@Override
	public JsonValue encode(Component value) {
		if(value instanceof ActionsOperation){
			ActionsOperation actions = (ActionsOperation)value;
			if(OperationsService.ID_SS_CAT_RESERVE.equals(actions.getControleur().getOperation().getSsCategorie().getId())){
				return null;
			}
		}
		return super.encode(value);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -1961495323666066789L;

}
