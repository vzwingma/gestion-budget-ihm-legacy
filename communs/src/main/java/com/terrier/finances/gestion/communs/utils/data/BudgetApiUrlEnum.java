package com.terrier.finances.gestion.communs.utils.data;

public class BudgetApiUrlEnum {

	public static final String ROOT_BASE = "/rest";

	/**
	 * Statut
	 */
	public static final String STATUT_BASE = "/statut";
	
	/**
	 * Authentification
	 */
	public static final String AUTH_BASE = "/authentification";
	
	public static final String AUTH_AUTHENTICATE = "/v1/authenticate";
	
	public static final String AUTH_AUTHENTICATE_FULL = AUTH_BASE + AUTH_AUTHENTICATE;
	
}
