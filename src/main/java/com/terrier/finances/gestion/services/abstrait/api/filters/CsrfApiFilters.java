/**
 * 
 */
package com.terrier.finances.gestion.services.abstrait.api.filters;

import java.io.IOException;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;

import org.glassfish.jersey.client.filter.CsrfProtectionFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author vzwingma
 *
 */
@Service
public class CsrfApiFilters extends CsrfProtectionFilter implements ClientResponseFilter {


	/**
	 * Logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(CsrfApiFilters.class);
	
	private static final String DEFAULT_CSRF_COOKIE_NAME = "XSRF-TOKEN";
	
	public static final String DEFAULT_CSRF_HEADER_NAME = "X-XSRF-TOKEN";
	public static final String ADD_CSRF_HEADER_NAME = "X-CSRF-TOKEN";	
	public static final String HEADER_NAME = "X-Requested-By";

	// Valeur du token
	private String csrfToken;

	/**
	 * Request filter - Ajout du token CSRF
	 */
	@Override
	public void filter(ClientRequestContext rc) throws IOException {
		LOGGER.debug("[HEADER X-SRF-TOKEN] > {}", csrfToken);
		rc.getHeaders().add(DEFAULT_CSRF_HEADER_NAME, csrfToken);
		rc.getHeaders().add(ADD_CSRF_HEADER_NAME, csrfToken);
		rc.getHeaders().add(HEADER_NAME, csrfToken);
	}


	/**
	 * Response Filter
	 */
	@Override
	public void filter(ClientRequestContext requestContext, ClientResponseContext responseContext) throws IOException {
		csrfToken = responseContext.getCookies().get(DEFAULT_CSRF_COOKIE_NAME).getValue();
		LOGGER.debug("[Cookie XSRF-TOKEN] < {}", csrfToken);
	}


	/**
	 * @return the csrfToken
	 */
	public String getCsrfToken() {
		return csrfToken;
	}
}
