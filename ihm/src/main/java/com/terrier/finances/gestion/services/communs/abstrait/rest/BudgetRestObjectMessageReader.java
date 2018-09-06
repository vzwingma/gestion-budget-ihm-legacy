/**
 * 
 */
package com.terrier.finances.gestion.services.communs.abstrait.rest;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.terrier.finances.gestion.communs.abstrait.AbstractRestObjectModel;

/**
 * @author vzwingma
 * @param <T>
 *
 */
public class BudgetRestObjectMessageReader<T extends AbstractRestObjectModel> implements MessageBodyReader<T> {
	
	private ObjectMapper mapper = new ObjectMapper();
	
	
	@Override
	public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return true;
	}

	@Override
	public T readFrom(Class<T> type, Type genericType, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
					throws IOException, WebApplicationException {
		return mapper.readValue(entityStream, type);
	}

}
