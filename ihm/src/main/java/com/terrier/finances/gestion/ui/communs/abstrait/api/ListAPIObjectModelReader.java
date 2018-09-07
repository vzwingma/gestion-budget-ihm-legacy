package com.terrier.finances.gestion.ui.communs.abstrait.api;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.terrier.finances.gestion.communs.abstrait.AbstractAPIObjectModel;

/**
 * Reader d'un {@link ListAPIObjectModelReader}
 * @author vzwingma
 *
 */
public class ListAPIObjectModelReader<T extends AbstractAPIObjectModel> implements MessageBodyReader<List<T>> {
	
	private ObjectMapper mapper = new ObjectMapper();

	Class<T> c;
	
	public ListAPIObjectModelReader(Class<T> c) {
		this.c = c;
	}
	
	@Override
	public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return MediaType.APPLICATION_JSON_TYPE.equals(mediaType) && Collection.class.isAssignableFrom(type);
	}
	
	@Override
	public List<T> readFrom(Class<List<T>> type, Type genericType, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
					throws IOException, WebApplicationException {

		CollectionType javaType = mapper.getTypeFactory().constructCollectionType(List.class, c);
		return mapper.readValue(entityStream, javaType);
	}
}
