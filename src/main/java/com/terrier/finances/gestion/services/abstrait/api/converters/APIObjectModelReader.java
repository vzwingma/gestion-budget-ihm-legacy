package com.terrier.finances.gestion.services.abstrait.api.converters;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ResolvableType;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.MediaType;
import org.springframework.http.ReactiveHttpInputMessage;
import org.springframework.http.codec.HttpMessageReader;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonpCharacterEscapes;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.terrier.finances.gestion.communs.abstrait.AbstractAPIObjectModel;
import com.terrier.finances.gestion.communs.budget.model.BudgetMensuel;
import com.terrier.finances.gestion.communs.comptes.model.CompteBancaire;
import com.terrier.finances.gestion.communs.comptes.model.api.IntervallesCompteAPIObject;
import com.terrier.finances.gestion.communs.operations.model.LigneOperation;
import com.terrier.finances.gestion.communs.operations.model.api.LibellesOperationsAPIObject;
import com.terrier.finances.gestion.communs.parametrages.model.CategorieOperation;
import com.terrier.finances.gestion.communs.utilisateur.model.api.AuthLoginAPIObject;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Reader d'un {@link APIObjectModelReader}
 * @author vzwingma
 *
 */
public class APIObjectModelReader<T extends AbstractAPIObjectModel> implements HttpMessageReader<T> {



	private static final Logger LOGGER = LoggerFactory.getLogger( APIObjectModelReader.class );

	// Classe des objets Budget, gérés
	@SuppressWarnings("unchecked")
	private Class<T>[] classesAPIObjectModel = new Class[]{ 
			AuthLoginAPIObject.class,
			CategorieOperation.class, 
			CompteBancaire.class, 
			BudgetMensuel.class,
			IntervallesCompteAPIObject.class, 
			LibellesOperationsAPIObject.class,
			LigneOperation.class
	};

	private ObjectMapper mapper;


	/**
	 *  COnstructeur
	 */
	public APIObjectModelReader() {
		JsonFactory factory = new JsonFactory();
		factory.setCharacterEscapes(new JsonpCharacterEscapes());
		mapper = new ObjectMapper(factory).deactivateDefaultTyping();
	}



	/**
	 * MediaType : JSON
	 */
	@Override
	public List<MediaType> getReadableMediaTypes() {
		return Arrays.asList(MediaType.APPLICATION_JSON);
	}

	@Override
	public boolean canRead(ResolvableType elementType, MediaType mediaType) {
		LOGGER.info("isReadable ? : {}#{}", elementType, mediaType);
		boolean read = getReadableMediaTypes().contains(mediaType) 
				&& (Collection.class.isAssignableFrom(elementType.getClass())
						|| AbstractAPIObjectModel.class.isAssignableFrom(elementType.getRawClass()));
		LOGGER.info("isReadable   :-> {}", read);
		return read;
	}

	/**
	 * Read
	 */
	@Override
	public Flux<T> read(ResolvableType elementType, ReactiveHttpInputMessage message, Map<String, Object> hints) {
		LOGGER.info("TryToReadFlux in {} -> {} ", elementType, hints);
		return message.getBody().flatMap(buffer -> {
			List<T> listeObjects = tryToParseList(buffer, elementType.getRawClass());
			return listeObjects != null ? Flux.fromIterable(listeObjects) : Flux.empty();
		});
	}
	/**
	 * @param entity
	 * @param classeAPIObjectModel
	 * @return null si erreur dans le parse
	 */
	private List<T> tryToParseList(DataBuffer entity, Class<?> classeAPIObjectModel){
		InputStream is = entity.asInputStream();

		try{
			LOGGER.info("TryToParseList in {}", classeAPIObjectModel);
			CollectionType javaType = mapper.getTypeFactory().constructCollectionType(List.class, classeAPIObjectModel);
			List<T> liste = mapper.readValue(is, javaType);
			LOGGER.info("TryToParseList in {} -> {} ", classeAPIObjectModel, liste != null);
			if(liste != null) {
				return liste;
			}
		}
		catch(Exception e){
			LOGGER.info("Erreur lors du try to parse : ", e);
		}
		return null;
	}




	@Override
	public Mono<T> readMono(ResolvableType elementType, ReactiveHttpInputMessage message, Map<String, Object> hints) {
		LOGGER.info("TryToReadMono in {} -> {} ", elementType, hints);
		return message.getBody().flatMap(buffer -> {
			return Mono.just(tryToParse(buffer, elementType.getRawClass()));
		}).next();

	}

	/**
	 * @param entity
	 * @param classeAPIObjectModel
	 * @return null si erreur dans le parse
	 */
	private T tryToParse(DataBuffer entity, Class<?> classeAPIObjectModel){

		try{
			@SuppressWarnings("unchecked")
			T objectFromData = (T)mapper.readValue(entity.asInputStream(), classeAPIObjectModel);
			LOGGER.trace("TryToParse in {} -> {} ", classeAPIObjectModel, objectFromData != null);
			if(objectFromData != null) {
				return objectFromData;
			}
		}
		catch(Exception e){
			LOGGER.info("Erreur lors du try to parse : ", e);
		}
		return null;
	}
}
