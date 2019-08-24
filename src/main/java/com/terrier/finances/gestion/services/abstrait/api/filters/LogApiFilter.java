package com.terrier.finances.gestion.services.abstrait.api.filters;

import java.io.IOException;
import java.util.UUID;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.terrier.finances.gestion.communs.api.security.ApiConfigEnum;

/**
 * Logger des API
 * @author vzwingma
 *
 */
@Service
public class LogApiFilter implements ClientResponseFilter, ClientRequestFilter {

	

	public static final Logger LOGGER = LoggerFactory.getLogger( LogApiFilter.class );
	
	
	/**
	 * Log de la requête
	 */
	@Override
	public void filter(ClientRequestContext requestContext) throws IOException {
		String apiCorrID = UUID.randomUUID().toString();
		org.slf4j.MDC.put(ApiConfigEnum.HEADER_API_CORRELATION_ID, "[API="+apiCorrID+"]");
		requestContext.getHeaders().add(ApiConfigEnum.HEADER_API_CORRELATION_ID, apiCorrID);
		
		LOGGER.info("[{} :: {}]", requestContext.getMethod(), requestContext.getUri());
	}

	/**
	 * Log de la réponse
	 */
	@Override
	public void filter(ClientRequestContext requestContext, ClientResponseContext responseContext) throws IOException {
		LOGGER.info("[{} :: {}] : [{}]", requestContext.getMethod(), requestContext.getUri(), responseContext.getStatus());
	}

}
