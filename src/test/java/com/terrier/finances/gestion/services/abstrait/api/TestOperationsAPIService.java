package com.terrier.finances.gestion.services.abstrait.api;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.time.Month;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

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

		BudgetMensuel budgetResponse = new BudgetMensuel();
		budgetResponse.setId("TEST");
		when(service.callHTTPGetData(
				eq(BudgetApiUrlEnum.BUDGET_QUERY_FULL), 
				ArgumentMatchers.anyMap())
				.block())
			.thenReturn(budgetResponse);

		BudgetMensuel budget = service.chargerBudgetMensuel("test", Month.JANUARY, 2018);
		assertNotNull(budget);
	}
}
