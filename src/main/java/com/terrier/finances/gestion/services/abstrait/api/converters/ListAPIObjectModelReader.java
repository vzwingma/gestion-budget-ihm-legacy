package com.terrier.finances.gestion.services.abstrait.api.converters;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.terrier.finances.gestion.communs.abstrait.AbstractAPIObjectModel;
import com.terrier.finances.gestion.communs.budget.model.BudgetMensuel;
import com.terrier.finances.gestion.communs.comptes.model.CompteBancaire;
import com.terrier.finances.gestion.communs.parametrages.model.CategorieOperation;

/**
 * Reader d'un {@link ListAPIObjectModelReader}
 * @author vzwingma
 *
 * @param <T> type géré par le reader
 */
public class ListAPIObjectModelReader<T extends AbstractAPIObjectModel> implements MessageBodyReader<List<T>> {

	private ObjectMapper mapper = new ObjectMapper();


	private static final Logger LOGGER = LoggerFactory.getLogger( ListAPIObjectModelReader.class );

	// Classe des objets Budget, gérés
	@SuppressWarnings("unchecked")
	private Class<T>[] classesAPIObjectModel = new Class[]{ CategorieOperation.class, CompteBancaire.class, BudgetMensuel.class };


	/* (non-Javadoc)
	 * @see javax.ws.rs.ext.MessageBodyReader#isReadable(java.lang.Class, java.lang.reflect.Type, java.lang.annotation.Annotation[], javax.ws.rs.core.MediaType)
	 */
	@Override
	public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		boolean read = MediaType.APPLICATION_JSON_TYPE.equals(mediaType) && Collection.class.isAssignableFrom(type);
		LOGGER.trace("isReadable : {}#{} -> {}", type, mediaType, read);
		return read;
	}

	/* (non-Javadoc)
	 * @see javax.ws.rs.ext.MessageBodyReader#readFrom(java.lang.Class, java.lang.reflect.Type, java.lang.annotation.Annotation[], javax.ws.rs.core.MediaType, javax.ws.rs.core.MultivaluedMap, java.io.InputStream)
	 */
	@Override
	public List<T> readFrom(Class<List<T>> type, Type genericType, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
					throws IOException {

		String entity = IOUtils.toString(entityStream, "UTF-8");
		
		for (Class<T> classeAPIObjectModel : classesAPIObjectModel) {
			List<T> listeObjects = tryToParse(entity, classeAPIObjectModel);
			if(listeObjects != null){
				return listeObjects;
			}
		}
		return new ArrayList<>();
	}

	/**
	 * @param entity
	 * @param classeAPIObjectModel
	 * @return null si erreur dans le parse
	 */
	private List<T> tryToParse(String entity, Class<T> classeAPIObjectModel){
		try{
	
			CollectionType javaType = mapper.getTypeFactory().constructCollectionType(List.class, classeAPIObjectModel);
			List<T> liste = mapper.readValue(entity, javaType);
			LOGGER.debug("TryToParse in {} -> {} ", classeAPIObjectModel, liste != null);
			return liste;
		}
		catch(Exception e){
			LOGGER.trace("Erreur lors du try to parse : ", e);
		}
		return null;
	}
}
