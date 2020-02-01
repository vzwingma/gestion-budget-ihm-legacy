package com.terrier.finances.gestion.services.abstrait.api.converters;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonpCharacterEscapes;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.terrier.finances.gestion.communs.abstrait.AbstractAPIObjectModel;

/**
 * Reader d'un {@link APIObjectModelReader}
 * @author vzwingma
 *
 */
public class APIObjectModelReader<T extends AbstractAPIObjectModel> implements MessageBodyReader<T> {


	private static final Logger LOGGER = LoggerFactory.getLogger( APIObjectModelReader.class );
	
	private ObjectMapper mapper;
	
	public APIObjectModelReader() {
		JsonFactory factory = new JsonFactory();
		factory.setCharacterEscapes(new JsonpCharacterEscapes());
		mapper = new ObjectMapper(factory).deactivateDefaultTyping();
	}
	
	
	@Override
	public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return isAbstractAPIObjectModel(type, mediaType);
	}

	
	private boolean isAbstractAPIObjectModel(Class<?> clazz, MediaType mediaType){
		boolean read = MediaType.APPLICATION_JSON.equals(mediaType) && AbstractAPIObjectModel.class.isAssignableFrom(clazz);
		LOGGER.trace("isReadable : {}#{} -> {}", clazz, mediaType, read);
		return read;
	}

	@Override
	public T readFrom(Class<T> type, Type genericType, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
					throws IOException {
		return mapper.readValue(entityStream, type);
	}
}
