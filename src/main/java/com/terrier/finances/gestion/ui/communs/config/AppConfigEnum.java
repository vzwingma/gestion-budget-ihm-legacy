package com.terrier.finances.gestion.ui.communs.config;

/**
 * Enum des config
 * @author vzwingma
 *
 */
public enum AppConfigEnum {

	// URL du service
	APP_CONFIG_URL_SERVICE("http://localhost:8090/services");
	
	
	
	private String defaultValue;
	
	private AppConfigEnum(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	/**
	 * @return the defaultValue
	 */
	public String getDefaultValue() {
		return defaultValue;
	}
}
