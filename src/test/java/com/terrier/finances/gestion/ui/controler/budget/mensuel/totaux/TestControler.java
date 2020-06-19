package com.terrier.finances.gestion.ui.controler.budget.mensuel.totaux;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.terrier.finances.gestion.communs.utils.data.BudgetDateTimeUtils;
import com.terrier.finances.gestion.ui.resume.totaux.ui.GridResumeTotauxController;

/**
 * Tests des controleurs
 * @author vzwingma
 *
 */
class TestControler {


	/**
	 * Logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(TestControler.class);
	@Test
	void testAffichageDate(){
		LocalDate now = BudgetDateTimeUtils.localDateNow();
		LOGGER.info("du {} à fin {}", 
				now.format(GridResumeTotauxController.auDateFormat), now.format(GridResumeTotauxController.finDateFormat));
		assertNotNull(now);
	}
}
