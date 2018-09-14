package com.terrier.finances.gestion.test.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Config de test
 * @author vzwingma
 *
 */
@Configuration
@ComponentScan(basePackages = { 
		"com.terrier.finances.gestion.test.api"})
@PropertySource(value={"classpath:config.properties"}, ignoreResourceNotFound = true)
public class TestAppConfig {

	// Ajout du TestAPIController
}

