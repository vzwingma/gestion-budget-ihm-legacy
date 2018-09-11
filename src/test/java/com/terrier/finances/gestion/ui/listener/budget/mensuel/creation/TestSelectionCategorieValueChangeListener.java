package com.terrier.finances.gestion.ui.listener.budget.mensuel.creation;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.terrier.finances.gestion.communs.parametrages.model.CategorieOperation;

public class TestSelectionCategorieValueChangeListener {

	
	@Test
	public void addSsCategoriesActives(){
		
		List<CategorieOperation> ssCategories = new ArrayList<>();
		
		CategorieOperation catActive = new CategorieOperation();
		catActive.setActif(true);
		catActive.setId("ACTIVE");
		ssCategories.add(catActive);
		
		CategorieOperation catInactive = new CategorieOperation();
		catInactive.setActif(false);
		catInactive.setId("INACTIVE");
		ssCategories.add(catInactive);
		
		assertEquals(1, ssCategories.stream().filter(cat -> cat.isActif()).count());
		
	}
}
