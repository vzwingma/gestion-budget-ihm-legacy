/**
 * 
 */
package com.terrier.finances.gestion.services.abstrait.api.converters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.terrier.finances.gestion.communs.api.model.Info;
import com.terrier.finances.gestion.communs.comptes.model.v12.CompteBancaire;
import org.junit.jupiter.api.Test;
import org.springframework.core.ResolvableType;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ReactiveHttpInputMessage;
import org.springframework.http.converter.HttpMessageNotWritableException;
import reactor.core.publisher.Flux;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author vzwingma
 *
 */
public class TestAPIReader {


	@SuppressWarnings("unchecked")
	@Test
	public void testReaderListAPIObjects() throws HttpMessageNotWritableException, IOException{
		@SuppressWarnings("rawtypes")
		APIObjectModelReader reader = new APIObjectModelReader();

		assertTrue(reader.canRead(ResolvableType.forClass(CompteBancaire.class), MediaType.APPLICATION_JSON));
		assertFalse(reader.canRead(ResolvableType.forClass(List.class), MediaType.APPLICATION_XML));

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

		Flux<CompteBancaire> obj = reader.read(
					ResolvableType.forClass(CompteBancaire.class), getReactiveHttpInputMessage(entityStream, jsonComptes.length()), null);
		assertEquals("c1", obj.blockFirst().getId());
	}



	@Test
	public void testConvertInfo() throws IOException {

		String jsonInfo = "{" + 
				"    \"app\": {" + 
				"        \"description\": \"Services de l'application de Gestion de Budget\"," + 
				"        \"name\": \"API de services de budget\"," + 
				"        \"version\": \"7.3.1-SNAPSHOT\"" + 
				"    }" + 
				"}";
		InputStream entityStream = new ByteArrayInputStream(jsonInfo.getBytes(StandardCharsets.UTF_8));
		APIObjectModelReader<Info> reader = new APIObjectModelReader<>();
		Info boRead = reader.readMono(ResolvableType.forClass(Info.class), getReactiveHttpInputMessage(entityStream, jsonInfo.length()), null).block();
		assertEquals("7.3.1-SNAPSHOT", boRead.getApp().getVersion());
	}


	private ReactiveHttpInputMessage getReactiveHttpInputMessage(final InputStream data, int size) {
		return new ReactiveHttpInputMessage() {

			@Override
			public HttpHeaders getHeaders() { return null; }

			@Override
			public Flux<DataBuffer> getBody() {
				return DataBufferUtils.readInputStream(
						() -> data, 
						new DefaultDataBufferFactory(), 
						size);
			}
		};
	}
}
