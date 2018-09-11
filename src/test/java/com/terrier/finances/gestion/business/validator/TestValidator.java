package com.terrier.finances.gestion.business.validator;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.terrier.finances.gestion.communs.operations.model.LigneOperation;
import com.terrier.finances.gestion.communs.operations.model.enums.EtatOperationEnum;
import com.terrier.finances.gestion.communs.operations.model.enums.TypeOperationEnum;
import com.terrier.finances.gestion.communs.parametrages.model.CategorieOperation;
import com.terrier.finances.gestion.communs.parametrages.model.enums.IdsCategoriesEnum;
import com.terrier.finances.gestion.ui.operations.creation.validator.OperationValidator;
import com.vaadin.data.ValidationResult;

public class TestValidator {


	private OperationValidator validator = new OperationValidator();

	@Test
	public void testValidator(){

		LigneOperation operation = new LigneOperation();


		// Ligne nulle
		assertEquals(ValidationResult.error("").isError(), validator.apply(operation, null).isError());

		operation.setCategorie(new CategorieOperation());
		operation.setSsCategorie(new CategorieOperation());
		operation.setEtat(EtatOperationEnum.PREVUE);
		operation.setLibelle("TEST LIBELLE");
		operation.setTypeDepense(TypeOperationEnum.DEPENSE);
		operation.setValeurAbsStringToDouble("-123.13");
		// Ligne OK
		ValidationResult r = validator.apply(operation, null);
		assertEquals(ValidationResult.ok().isError(), r.isError());
	}


	@Test
	public void testValue(){

		LigneOperation operation = new LigneOperation();

		// Ligne nulle
		operation.setCategorie(new CategorieOperation());
		operation.setSsCategorie(new CategorieOperation());
		operation.setEtat(EtatOperationEnum.PREVUE);
		operation.setLibelle("TEST LIBELLE");
		operation.setTypeDepense(TypeOperationEnum.DEPENSE);
		operation.setValeurAbsStringToDouble("NaN");
		// Ligne OK
		ValidationResult r = validator.apply(operation, null);
		assertEquals(ValidationResult.error("").isError(), r.isError());
	}



	

	@Test
	public void testValidatorCredit(){
		
		
		LigneOperation operation = new LigneOperation();
		operation.setCategorie(new CategorieOperation());
		operation.setSsCategorie(new CategorieOperation());
		operation.getSsCategorie().setId(IdsCategoriesEnum.SALAIRE.getId());
		operation.setEtat(EtatOperationEnum.PREVUE);
		operation.setLibelle("TEST LIBELLE");		
		operation.setValeurAbsStringToDouble("-123");
		operation.setTypeDepense(TypeOperationEnum.DEPENSE);
		assertEquals(ValidationResult.error("").isError(), validator.apply(operation, null).isError());
	}
	
	
	@Test
	public void testValidatorDebit(){
			
		LigneOperation operation = new LigneOperation();
		operation.setCategorie(new CategorieOperation());
		operation.setSsCategorie(new CategorieOperation());
		operation.getSsCategorie().setId("26a4b966-ffff-ffff-8611-a5ba4b518ef5");
		operation.setEtat(EtatOperationEnum.PREVUE);
		operation.setLibelle("TEST LIBELLE");	
		operation.setValeurAbsStringToDouble("123");
		operation.setTypeDepense(TypeOperationEnum.CREDIT);
		assertEquals(ValidationResult.error("").isError(), validator.apply(operation, null).isError());
	}
	
}
