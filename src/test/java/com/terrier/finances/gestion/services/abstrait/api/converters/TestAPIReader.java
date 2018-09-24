/**
 * 
 */
package com.terrier.finances.gestion.services.abstrait.api.converters;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.junit.Test;
import org.springframework.http.converter.HttpMessageNotWritableException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.terrier.finances.gestion.communs.abstrait.AbstractAPIObjectModel;
import com.terrier.finances.gestion.communs.budget.model.BudgetMensuel;
import com.terrier.finances.gestion.communs.comptes.model.CompteBancaire;
import com.terrier.finances.gestion.communs.parametrages.model.CategorieOperation;
import com.terrier.finances.gestion.communs.utilisateur.model.Utilisateur;
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
		InputStream entityStream = new ByteArrayInputStream(jsonComptes.getBytes(StandardCharsets.UTF_8));
		
		List<CompteBancaire> obj = reader.readFrom(ArrayList.class, null, null, MediaType.APPLICATION_JSON_TYPE, null, entityStream);
		assertEquals("c1", obj.get(0).getId());
	}
	
	

	
	@Test
	public void testConvertBudget() throws Exception {

		// Budget
		CompteBancaire c1 = new CompteBancaire();
		c1.setActif(true);
		c1.setId("C1");
		c1.setLibelle("Libelle1");
		c1.setOrdre(1);

		BudgetMensuel bo = new BudgetMensuel();
		bo.setCompteBancaire(c1);
		bo.setMois(Month.JANUARY);
		bo.setAnnee(2018);
		bo.setActif(false);
		bo.setId("BUDGETTEST");
		bo.setSoldeFin(0D);
		bo.setSoldeNow(1000D);
		Calendar c = Calendar.getInstance();
		bo.setDateMiseAJour(c);
		bo.setResultatMoisPrecedent(0D, 100D);
		
		CategorieOperation cat = new CategorieOperation();
		cat.setCategorie(true);
		cat.setId("IdTest");
		cat.setLibelle("LibelleTest");
		bo.getTotalParCategories().put(cat.getId(), new Double[]{ 100D, 200D});
		
		CategorieOperation ssCat = new CategorieOperation();
		ssCat.setCategorie(false);
		ssCat.setId("IdTest");
		ssCat.setLibelle("LibelleTest");
		bo.getTotalParSSCategories().put(ssCat.getId(), new Double[]{ 100D, 200D});

		
		Utilisateur user = new Utilisateur();
		user.setId("userTest");
		user.setLibelle("userTest");
		user.setLogin("userTest");
		
		APIObjectModelReader<BudgetMensuel> reader = new APIObjectModelReader<>();
		assertTrue(reader.isReadable(BudgetMensuel.class, null, null, MediaType.APPLICATION_JSON_TYPE));
		

		String jsonComptes = new ObjectMapper().writeValueAsString(bo);
		InputStream entityStream = new ByteArrayInputStream(jsonComptes.getBytes(StandardCharsets.UTF_8));
		
		BudgetMensuel boRead = reader.readFrom(BudgetMensuel.class, null, null, MediaType.APPLICATION_JSON_TYPE, null, entityStream);
		assertEquals(boRead.getClass().getName(), BudgetMensuel.class.getName());

		assertEquals(bo.getId(), boRead.getId());
		assertEquals(bo.getMarge(), boRead.getMarge());
		assertEquals(bo.getSoldeReelFin(), boRead.getSoldeReelFin(), 1);
		assertEquals(bo.getSoldeReelNow(), boRead.getSoldeReelNow(), 1);
		
		assertEquals(1, boRead.getTotalParCategories().size());
		assertEquals("IdTest", boRead.getTotalParCategories().keySet().iterator().next());
		assertEquals(1, boRead.getTotalParSSCategories().size());
		assertEquals("IdTest", boRead.getTotalParSSCategories().keySet().iterator().next());
	}
	
	
	@Test
	public void testConvertMap() throws IOException{
		// Budget
		BudgetMensuel bo = new BudgetMensuel();
		bo.setId("BUDGETTEST");
		bo.setSoldeFin(0D);
		bo.setSoldeNow(1000D);
		
		CategorieOperation cat = new CategorieOperation();
		cat.setCategorie(true);
		cat.setId("IdTest");
		cat.setLibelle("LibelleTest");
		bo.getTotalParCategories().put(cat.getId(), new Double[]{ 100D, 200D});
		
		CategorieOperation ssCat = new CategorieOperation();
		ssCat.setCategorie(false);
		ssCat.setId("IdTest");
		ssCat.setLibelle("LibelleTest");
		bo.getTotalParSSCategories().put(ssCat.getId(), new Double[]{ 100D, 200D});
		
		APIObjectModelReader<BudgetMensuel> reader = new APIObjectModelReader<>();
		assertTrue(reader.isReadable(BudgetMensuel.class, null, null, MediaType.APPLICATION_JSON_TYPE));

		String jsonComptes = new ObjectMapper().writeValueAsString(bo);
		InputStream entityStream = new ByteArrayInputStream(jsonComptes.getBytes(StandardCharsets.UTF_8));
		
		
		BudgetMensuel boRead = reader.readFrom(BudgetMensuel.class, null, null, MediaType.APPLICATION_JSON_TYPE, null, entityStream);
		assertEquals(boRead.getClass().getName(), BudgetMensuel.class.getName());

		assertEquals(1, boRead.getTotalParCategories().size());
		assertEquals("IdTest", boRead.getTotalParCategories().keySet().iterator().next());
		assertEquals(1, boRead.getTotalParSSCategories().size());
		assertEquals("IdTest", boRead.getTotalParSSCategories().keySet().iterator().next());
	}
}
