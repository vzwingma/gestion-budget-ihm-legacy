/**
 * 
 */
package com.terrier.finances.gestion.services.communs.api.converters;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.time.LocalDateTime;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;

import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * Reader d'un local date time
 * @author vzwingma
 *
 */
public class LocalDateTimeMessageReader implements MessageBodyReader<LocalDateTime> {
	
	private ObjectMapper mapper = new ObjectMapper();
	
	
	@Override
	public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {

		boolean readable = type.isAssignableFrom(LocalDateTime.class);
		System.err.println("********" + readable);
		return readable;
	}


	@Override
	public LocalDateTime readFrom(Class<LocalDateTime> type, Type genericType, Annotation[] annotations,
			MediaType mediaType, MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
					throws IOException, WebApplicationException {
		return mapper.readValue(entityStream, type);
	}

}
