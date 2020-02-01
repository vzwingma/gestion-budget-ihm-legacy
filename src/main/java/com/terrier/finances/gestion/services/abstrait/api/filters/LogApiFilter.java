package com.terrier.finances.gestion.services.abstrait.api.filters;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;

import com.terrier.finances.gestion.communs.api.security.ApiHeaderIdEnum;

import reactor.core.publisher.Mono;

/**
 * Logger des API
 * @author vzwingma
 *
 */
@Service
public class LogApiFilter implements ExchangeFilterFunction {

	

	public static final Logger LOGGER = LoggerFactory.getLogger( LogApiFilter.class );
	

	/**
	 * Log de la réponse
	 */
//	@Override
//	public void filter(ClientRequestContext requestContext, ClientResponseContext responseContext) throws IOException {
//		LOGGER.info("Statut HTTP : [{}]", responseContext.getStatus());
//	}

	/**
	 * Log de la requête
	 */
	@Override
	public Mono<ClientResponse> filter(ClientRequest requestContext, ExchangeFunction next) {
		String apiCorrID = UUID.randomUUID().toString();
		org.slf4j.MDC.put(ApiHeaderIdEnum.HEADER_API_CORRELATION_ID, "[API="+apiCorrID+"]");
		requestContext.headers().add(ApiHeaderIdEnum.HEADER_API_CORRELATION_ID, apiCorrID);
		
		LOGGER.info("{} :: {}", requestContext.method(), requestContext.url());
		return next.exchange(requestContext);
	}

}
