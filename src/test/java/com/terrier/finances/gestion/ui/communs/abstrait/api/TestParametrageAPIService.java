package com.terrier.finances.gestion.ui.communs.abstrait.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.terrier.finances.gestion.communs.parametrages.model.CategorieOperation;
import com.terrier.finances.gestion.communs.utils.data.BudgetApiUrlEnum;
import com.terrier.finances.gestion.services.parametrages.api.ParametragesAPIService;

/**
 * Test des API Params
 * @author vzwingma
 *
 */
public class TestParametrageAPIService {

	private List<CategorieOperation> categories  = new ArrayList<>();
	
	@Before
	public void initData(){
		CategorieOperation catAlimentation = new CategorieOperation();
		catAlimentation.setId("8f1614c9-503c-4e7d-8cb5-0c9a9218b84a");
		catAlimentation.setActif(true);
		catAlimentation.setCategorie(true);
		catAlimentation.setLibelle("Alimentation");


		CategorieOperation ssCatCourse = new CategorieOperation();
		ssCatCourse.setActif(true);
		ssCatCourse.setCategorie(false);
		ssCatCourse.setId("467496e4-9059-4b9b-8773-21f230c8c5c6");
		ssCatCourse.setLibelle("Courses");
		ssCatCourse.setCategorieParente(null); // La réponse API n'a pas cette liaison (justement)
		catAlimentation.getListeSSCategories().add(ssCatCourse);
		categories.add(catAlimentation);

	}
	/**
	 * 
	 */
	@Test
	public void testChargerOperations(){
		
		ParametragesAPIService service = spy(new ParametragesAPIService());
		when(service.callHTTPGetListData(anyString(), eq(BudgetApiUrlEnum.PARAMS_CATEGORIES), eq(CategorieOperation.class))).thenReturn(categories);
		
		List<CategorieOperation> liste = service.getCategories();
		assertNotNull(liste);
		assertEquals("8f1614c9-503c-4e7d-8cb5-0c9a9218b84a", liste.get(0).getId());
		assertNull(liste.get(0).getCategorieParente());
		assertEquals("467496e4-9059-4b9b-8773-21f230c8c5c6", liste.get(0).getListeSSCategories().iterator().next().getId());
		assertNull(liste.get(0).getCategorieParente());
		assertEquals("8f1614c9-503c-4e7d-8cb5-0c9a9218b84a", liste.get(0).getListeSSCategories().iterator().next().getCategorieParente().getId());
	}
}
