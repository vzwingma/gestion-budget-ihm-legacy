package com.terrier.finances.gestion.ui.communs.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan(basePackages = { 
		"com.terrier.finances.gestion.services",
		"com.terrier.finances.gestion.services.comptes.api",
		"com.terrier.finances.gestion.services.operations.api",
		"com.terrier.finances.gestion.services.parametrages.api",
		"com.terrier.finances.gestion.services.utilisateurs.api",
		"com.terrier.finances.gestion.ui.login.business"})
@PropertySource(value={"classpath:config.properties"}, ignoreResourceNotFound = true)
public class AppConfig {

	
}

