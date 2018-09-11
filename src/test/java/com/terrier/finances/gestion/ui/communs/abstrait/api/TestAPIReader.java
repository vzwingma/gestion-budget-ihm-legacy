/**
 * 
 */
package com.terrier.finances.gestion.ui.communs.abstrait.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.junit.Test;
import org.springframework.http.converter.HttpMessageNotWritableException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.terrier.finances.gestion.communs.abstrait.AbstractAPIObjectModel;
import com.terrier.finances.gestion.communs.comptes.model.CompteBancaire;
import com.terrier.finances.gestion.communs.utilisateur.model.api.AuthLoginAPIObject;

/**
 * @author vzwingma
 *
 */
public class TestAPIReader {

	@Test
	public void testConverterAPIObject() throws HttpMessageNotWritableException, IOException{
		APIObjectModelReader<AuthLoginAPIObject> reader = new APIObjectModelReader<>();
		
		assertFalse(AuthLoginAPIObject.class.isAssignableFrom(AbstractAPIObjectModel.class));
		assertTrue(AbstractAPIObjectModel.class.isAssignableFrom(AuthLoginAPIObject.class));
		assertTrue(reader.isReadable(AuthLoginAPIObject.class, null, null, MediaType.APPLICATION_JSON_TYPE));

		assertFalse(reader.isReadable(AuthLoginAPIObject.class, null, null, MediaType.APPLICATION_XML_TYPE));

		assertFalse(reader.isReadable(List.class, null, null, MediaType.APPLICATION_JSON_TYPE));

		InputStream entityStream = new ByteArrayInputStream("{\"login\":\"Test\",\"motDePasse\":\"Test\"}".getBytes(StandardCharsets.UTF_8));
		AuthLoginAPIObject obj = reader.readFrom(AuthLoginAPIObject.class, null, null, MediaType.APPLICATION_JSON_TYPE, null, entityStream);
		assertEquals(obj.getClass().getName(), AuthLoginAPIObject.class.getName());
		assertEquals("Test", obj.getLogin());
		
		assertTrue(reader.isReadable(CompteBancaire.class, null, null, MediaType.APPLICATION_JSON_TYPE));

	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testReaderListAPIObjects() throws HttpMessageNotWritableException, IOException{
		@SuppressWarnings("rawtypes")
		ListAPIObjectModelReader reader = new ListAPIObjectModelReader();
		
		assertFalse(reader.isReadable(AuthLoginAPIObject.class, null, null, MediaType.APPLICATION_JSON_TYPE));
		assertTrue(reader.isReadable(List.class, null, null, MediaType.APPLICATION_JSON_TYPE));
		assertFalse(reader.isReadable(List.class, null, null, MediaType.APPLICATION_XML_TYPE));
		
		List<CompteBancaire> listeComptes = new ArrayList<>();
		CompteBancaire c1 = new CompteBancaire();
		c1.setActif(true);
		c1.setId("c1");
		c1.setLibelle("C1");
		c1.setOrdre(0);
		listeComptes.add(c1);
		CompteBancaire c2 = new CompteBancaire();
		c2.setActif(true);
		c2.setId("c2");
		c2.setLibelle("c2");
		c2.setOrdre(0);
		listeComptes.add(c2);
		String jsonComptes = new ObjectMapper().writeValueAsString(listeComptes);
		System.err.println(jsonComptes);
		InputStream entityStream = new ByteArrayInputStream(jsonComptes.getBytes(StandardCharsets.UTF_8));
		
		List<CompteBancaire> obj = reader.readFrom(ArrayList.class, null, null, MediaType.APPLICATION_JSON_TYPE, null, entityStream);
		assertEquals("c1", obj.get(0).getId());

	}
}
