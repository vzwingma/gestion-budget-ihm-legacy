package com.terrier.finances.gestion.ui.controler.budget.mensuel.components;

import java.util.Calendar;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
		TableResumeTotauxController t = new TableResumeTotauxController(null);
		LOGGER.info("du {} à fin {}", 
				t.auDateFormat.format(Calendar.getInstance().getTime()), 
				t.finDateFormat.format(Calendar.getInstance().getTime()));
	}
}
