package com.terrier.finances.gestion.ui.communs.abstrait.api;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.terrier.finances.gestion.communs.abstrait.AbstractAPIObjectModel;

/**
 * Reader d'un {@link APIObjectModelReader}
 * @author vzwingma
 *
 */
public class APIObjectModelReader<T extends AbstractAPIObjectModel> implements MessageBodyReader<T> {


	private static final Logger LOGGER = LoggerFactory.getLogger( APIObjectModelReader.class );
	
	private ObjectMapper mapper = new ObjectMapper();
	
	@Override
	public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return isAbstractAPIObjectModel(type, mediaType);
	}

	
	private boolean isAbstractAPIObjectModel(Class<?> clazz, MediaType mediaType){
		LOGGER.debug("isReadable : {}/{} : {}", clazz, mediaType, AbstractAPIObjectModel.class.isAssignableFrom(clazz));
		return MediaType.APPLICATION_JSON_TYPE.equals(mediaType) && AbstractAPIObjectModel.class.isAssignableFrom(clazz);
	}

	@Override
	public T readFrom(Class<T> type, Type genericType, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
					throws IOException, WebApplicationException {
		return mapper.readValue(entityStream, type);
	}
}
