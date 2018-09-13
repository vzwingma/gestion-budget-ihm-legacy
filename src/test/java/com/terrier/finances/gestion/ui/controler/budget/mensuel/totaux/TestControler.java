package com.terrier.finances.gestion.ui.controler.budget.mensuel.totaux;

import java.time.LocalDate;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.terrier.finances.gestion.communs.utils.data.BudgetDateTimeUtils;
import com.terrier.finances.gestion.ui.resume.totaux.ui.GridResumeTotauxController;

/**
 * Tests des controleurs
 * @author vzwingma
 *
 */
public class TestControler {


	/**
	 * Logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(TestControler.class);
	@Test
	public void testAffichageDate(){
		LocalDate now = BudgetDateTimeUtils.localDateNow();
		LOGGER.info("du {} à fin {}", 
				now.format(GridResumeTotauxController.auDateFormat), now.format(GridResumeTotauxController.finDateFormat));
	}
}
