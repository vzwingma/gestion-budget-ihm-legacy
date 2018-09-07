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
	public static final String USERS_BASE = "/utilisateurs";
	
	public static final String USERS_AUTHENTICATE = "/v1/authenticate";
	public static final String USERS_AUTHENTICATE_FULL = USERS_BASE + USERS_AUTHENTICATE;
	
	
	public static final String USERS_DISCONNECT = "/v1/disconnect";
	public static final String USERS_DISCONNECT_FULL = USERS_BASE + USERS_DISCONNECT;
	
	public static final String USERS_ACCESS_DATE = "/v1/lastaccessdate";
	public static final String USERS_ACCESS_DATE_FULL = USERS_BASE + USERS_ACCESS_DATE;
	public static final String USERS_PREFS = "/v1/preferences";
	public static final String USERS_PREFS_FULL = USERS_BASE + USERS_PREFS;
}
