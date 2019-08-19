/**
 * 
 */
package com.terrier.finances.gestion.services.abstrait.api.filters;

import java.io.IOException;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author vzwingma
 *
 */
@Service
public class CsrfApiFilters implements ClientRequestFilter, ClientResponseFilter {

	
	/**
	 * Logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(CsrfApiFilters.class);
	static final String DEFAULT_CSRF_COOKIE_NAME = "XSRF-TOKEN";
	static final String DEFAULT_CSRF_HEADER_NAME = "X-XSRF-TOKEN";
	
	private String csrfToken;
	/**
	 * Request filter - Ajout du token CSRF
	 */
	@Override
	public void filter(ClientRequestContext rc) throws IOException {
		if(csrfToken != null) {
			LOGGER.info("[HEADER X-SRF-TOKEN : {}] > {}", this.hashCode(), csrfToken);
			rc.getHeaders().add(DEFAULT_CSRF_HEADER_NAME, csrfToken);
		}
	}


	/**
	 * Response Filter
	 */
	@Override
	public void filter(ClientRequestContext requestContext, ClientResponseContext responseContext) throws IOException {
		csrfToken = responseContext.getCookies().get(DEFAULT_CSRF_COOKIE_NAME).getValue();
		LOGGER.info("[Cookie XSRF-TOKEN : {}] < {}", this.hashCode(), csrfToken);
	}
}
