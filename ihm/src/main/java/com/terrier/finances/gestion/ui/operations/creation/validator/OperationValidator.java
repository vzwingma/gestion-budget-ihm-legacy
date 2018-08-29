/**
 * 
 */
package com.terrier.finances.gestion.ui.operations.creation.validator;

import com.terrier.finances.gestion.budget.business.OperationsService;
import com.terrier.finances.gestion.model.enums.TypeOperationEnum;
import com.terrier.finances.gestion.operations.model.LigneOperation;
import com.vaadin.data.ValidationResult;
import com.vaadin.data.Validator;
import com.vaadin.data.ValueContext;

/**
 * Validation d'une opération sur le formulaire de création
 * @author vzwingma
 *
 */
public class OperationValidator implements Validator<LigneOperation> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7063946665726387722L;


	@Override
	public ValidationResult apply(LigneOperation operation, ValueContext context) {

		// Not Null
		if(operation.getSsCategorie() == null
				|| operation.getValeurAbsStringFromDouble() == null
				|| operation.getLibelle() == null
				|| operation.getEtat() == null
				|| operation.getTypeDepense() == null){
			return ValidationResult.error("Un des éléments requis est nul");
		}

		// Valeur
		if(Double.isInfinite(operation.getValeur()) || Double.isNaN(operation.getValeur())){
			return ValidationResult.error("La valeur est incorrecte");
		}


		// Catégorie crédit
		if((OperationsService.ID_SS_CAT_SALAIRE.equals(operation.getSsCategorie().getId()) 
				|| OperationsService.ID_SS_CAT_REMBOURSEMENT.equals(operation.getSsCategorie().getId()))){
			if(TypeOperationEnum.DEPENSE.equals(operation.getTypeDepense())){
				return ValidationResult.error("L'opération est un crédit. Le type doit être CREDIT");
			}
			// Sinon c'est correct
		}
		else if(TypeOperationEnum.CREDIT.equals(operation.getTypeDepense())){
			return ValidationResult.error("L'opération est un débit. Le type doit être DEBIT (-)");
		}
		return ValidationResult.ok();
	}
}

