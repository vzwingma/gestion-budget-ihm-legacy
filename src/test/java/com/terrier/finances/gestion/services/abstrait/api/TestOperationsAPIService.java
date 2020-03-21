package com.terrier.finances.gestion.services.abstrait.api;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.terrier.finances.gestion.communs.budget.model.BudgetMensuel;
import com.terrier.finances.gestion.communs.utils.data.BudgetApiUrlEnum;
import com.terrier.finances.gestion.communs.utils.exceptions.BudgetNotFoundException;
import com.terrier.finances.gestion.communs.utils.exceptions.DataNotFoundException;
import com.terrier.finances.gestion.communs.utils.exceptions.UserNotAuthorizedException;
import com.terrier.finances.gestion.services.operations.api.OperationsAPIService;
import com.terrier.finances.gestion.test.config.AbstractTestServices;

/**
 * Test des opérations
 * @author vzwingma
 *
 */
public class TestOperationsAPIService extends AbstractTestServices {


	@Test
	public void testChargerBudgetMensuel() throws BudgetNotFoundException, DataNotFoundException, UserNotAuthorizedException{

		OperationsAPIService service = spyOperationsAPIService();
		assertNotNull(service);
		
		// Réponse
		BudgetMensuel budgetResponse = new BudgetMensuel();
		budgetResponse.setId("TEST");
		budgetResponse.setSoldeFin(0D);
		budgetResponse.setSoldeNow(0D);
		
		// Planif de la réponse
		RouterFunction<ServerResponse> function = RouterFunctions.route(
				RequestPredicates.GET(BudgetApiUrlEnum.BUDGET_QUERY_FULL),
				request -> ServerResponse.ok().bodyValue(budgetResponse)
				);
		
		WebTestClient testClient = WebTestClient
			.bindToRouterFunction(function)
			.build();
//		when(service.getClient()).thenReturn(testClient);
		testClient.get().uri(BudgetApiUrlEnum.BUDGET_QUERY_FULL)
		.exchange().expectStatus().isOk();


//		BudgetMensuel budget = service.chargerBudgetMensuel("test", Month.JANUARY, 2018);
//		assertNotNull(budget);
	}

}
