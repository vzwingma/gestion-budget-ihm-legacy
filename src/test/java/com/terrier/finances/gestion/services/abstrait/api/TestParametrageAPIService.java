package com.terrier.finances.gestion.services.abstrait.api;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.spy;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.terrier.finances.gestion.communs.parametrages.model.v12.CategorieOperation;
import com.terrier.finances.gestion.communs.utils.exceptions.DataNotFoundException;
import com.terrier.finances.gestion.communs.utils.exceptions.UserNotAuthorizedException;
import com.terrier.finances.gestion.services.parametrages.api.ParametragesAPIService;
import com.terrier.finances.gestion.test.config.AbstractTestServices;

/**
 * Test des API Params
 * @author vzwingma
 *
 */
public class TestParametrageAPIService extends AbstractTestServices {


	/**
	 * @throws UserNotAuthorizedException  erreur d'authentification
	 * @throws DataNotFoundException  erreur lors de l'appel
	 * 
	 */
	@Test
	public void testChargerOperations() throws UserNotAuthorizedException, DataNotFoundException{
		
		List<CategorieOperation> categories  = new ArrayList<>();
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
		
		ParametragesAPIService service = spy(new ParametragesAPIService());
		assertNotNull(service);
//		when(service.callHTTPGetListData(eq(BudgetApiUrlEnum.PARAMS_CATEGORIES_FULL))).
//			thenReturn(Mono.just(categories));
		
//		List<CategorieOperation> liste = service.getCategories();
//		assertNotNull(liste);
//		assertTrue(liste.size() > 0);
//		assertEquals("8f1614c9-503c-4e7d-8cb5-0c9a9218b84a", liste.get(0).getId());
//		assertNull(liste.get(0).getCategorieParente());
//		assertEquals("467496e4-9059-4b9b-8773-21f230c8c5c6", liste.get(0).getListeSSCategories().iterator().next().getId());
//		assertNull(liste.get(0).getCategorieParente());
//		assertEquals("8f1614c9-503c-4e7d-8cb5-0c9a9218b84a", liste.get(0).getListeSSCategories().iterator().next().getCategorieParente().getId());
	}
}
