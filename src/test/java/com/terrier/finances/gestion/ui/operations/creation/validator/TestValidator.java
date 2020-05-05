package com.terrier.finances.gestion.ui.operations.creation.validator;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.terrier.finances.gestion.communs.operations.model.enums.EtatOperationEnum;
import com.terrier.finances.gestion.communs.operations.model.enums.TypeOperationEnum;
import com.terrier.finances.gestion.communs.operations.model.v12.LigneOperation;
import com.terrier.finances.gestion.communs.parametrages.model.enums.IdsCategoriesEnum;
import com.vaadin.data.ValidationResult;

public class TestValidator {


	private OperationValidator validator = new OperationValidator();

	@Test
	public void testValidator(){

		LigneOperation operation = new LigneOperation();


		// Ligne nulle
		assertEquals(ValidationResult.error("").isError(), validator.apply(operation, null).isError());

		operation.setCategorie(operation.new Categorie());
		operation.setSsCategorie(operation.new Categorie());
		operation.setEtat(EtatOperationEnum.PREVUE);
		operation.setLibelle("TEST LIBELLE");
		operation.setTypeOperation(TypeOperationEnum.DEPENSE);
		operation.setValeurFromSaisie(-123.13D);
		// Ligne OK
		ValidationResult r = validator.apply(operation, null);
		assertEquals(ValidationResult.ok().isError(), r.isError());
	}


	@Test
	public void testValue(){

		LigneOperation operation = new LigneOperation();

		// Ligne nulle
		operation.setCategorie(operation.new Categorie());
		operation.setSsCategorie(operation.new Categorie());
		operation.setEtat(EtatOperationEnum.PREVUE);
		operation.setLibelle("TEST LIBELLE");
		operation.setTypeOperation(TypeOperationEnum.DEPENSE);
		operation.setValeurFromSaisie(0D);
		// Ligne OK
		ValidationResult r = validator.apply(operation, null);
		assertEquals(ValidationResult.error("").isError(), r.isError());
	}



	

	@Test
	public void testValidatorCredit(){
		
		
		LigneOperation operation = new LigneOperation();
		operation.setCategorie(operation.new Categorie());
		operation.setSsCategorie(operation.new Categorie());
		operation.getSsCategorie().setId(IdsCategoriesEnum.SALAIRE.toString());
		operation.setEtat(EtatOperationEnum.PREVUE);
		operation.setLibelle("TEST LIBELLE");		
		operation.setValeurFromSaisie(-123D);
		operation.setTypeOperation(TypeOperationEnum.DEPENSE);
		assertEquals(ValidationResult.error("").isError(), validator.apply(operation, null).isError());
	}
	
	
	@Test
	public void testValidatorDebit(){
			
		LigneOperation operation = new LigneOperation();
		operation.setCategorie(operation.new Categorie());
		operation.setSsCategorie(operation.new Categorie());
		operation.getSsCategorie().setId("26a4b966-ffff-ffff-8611-a5ba4b518ef5");
		operation.setEtat(EtatOperationEnum.PREVUE);
		operation.setLibelle("TEST LIBELLE");	
		operation.setValeurFromSaisie(123D);
		operation.setTypeOperation(TypeOperationEnum.CREDIT);
		assertEquals(ValidationResult.error("").isError(), validator.apply(operation, null).isError());
	}
	
}
