package com.terrier.finances.gestion.ui.communs.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	private static final Logger LOGGER = LoggerFactory.getLogger( AppConfig.class );
	
	
	/**
	 * Retourne la valeur string de la variable d'environnement
	 * @param cle
	 * @return valeur de la clé
	 */
	public static String getStringEnvVar(AppConfigEnum cle){
		String envVar = System.getenv(cle.name());
		if(envVar != null) {
			return envVar;
		}
		else {
			if(LOGGER.isWarnEnabled()) {
				LOGGER.warn("La clé {} n'est pas définie. Utilisation de la valeur par défaut : {} ", cle.name(), cle.getDefaultValue());
			}
			return cle.getDefaultValue();
		}
	}
}

