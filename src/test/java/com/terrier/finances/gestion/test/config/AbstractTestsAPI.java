/**
 * 
 */
package com.terrier.finances.gestion.test.config;

import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.terrier.finances.gestion.ui.communs.config.AppConfig;

/**
 * Classe abstraite des tests d'API
 * @author vzwingma
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={TestAppConfig.class, AppConfig.class})
public abstract class AbstractTestsAPI {

	/**
	 * Logger
	 */
	protected final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	
}
