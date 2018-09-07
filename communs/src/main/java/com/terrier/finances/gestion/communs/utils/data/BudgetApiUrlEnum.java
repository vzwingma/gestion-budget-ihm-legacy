package com.terrier.finances.gestion.communs.utils.data;

public class BudgetApiUrlEnum {

	public static final String ROOT_BASE = "/rest";

	/**
	 * Statut
	 */
	public static final String STATUT_BASE = "/statut";
	
	/**
	 * Authentification / Utilisateurs
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
	
	/**
	 * Comptes
	 */
	public static final String COMPTES_BASE = "/comptes";
	public static final String COMPTES_LIST = "/v1/list";
	public static final String COMPTES_LIST_FULL = COMPTES_BASE + COMPTES_LIST;
	
	public static final String COMPTES_ID = "/v1";
	public static final String COMPTES_ID_FULL = COMPTES_BASE + COMPTES_ID;
}
