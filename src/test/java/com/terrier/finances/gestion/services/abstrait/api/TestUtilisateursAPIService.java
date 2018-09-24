package com.terrier.finances.gestion.services.abstrait.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

import com.terrier.finances.gestion.communs.api.security.JwtConfig;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class TestUtilisateursAPIService {

	
	@Test
	public void testToken() {
		Long c = Calendar.getInstance().getTimeInMillis();
		
		String token = Jwts.builder()
				.setSubject("5484268384b7ff1e5f26b692")
				.setId("adc70a6a-79e9-4725-b72e-92d2a256b7ba")
				.claim(JwtConfig.JWT_CLAIM_USERID_HEADER, "5484268384b7ff1e5f26b692")
				.setIssuedAt(new Date(c))
				.setIssuer("Budget-Services v1")
				.setExpiration(new Date(c + JwtConfig.JWT_EXPIRATION_S * 1000))  // in milliseconds
				.signWith(SignatureAlgorithm.HS512, JwtConfig.JWT_SECRET_KEY.getBytes())
				.compact();
		
		assertNotNull(token);
		Claims claims = JwtConfig.getJWTClaims(token);
		assertNotNull(claims);
		assertEquals("adc70a6a-79e9-4725-b72e-92d2a256b7ba", claims.getId());
		assertEquals("5484268384b7ff1e5f26b692", claims.get(JwtConfig.JWT_CLAIM_USERID_HEADER));
		
	}
	
	
	@Test(expected=ExpiredJwtException.class)
	public void testExpiredToken() {
		String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ2endpbmdtYW5uIiwianRpIjoiYWRjNzBhNmEtNzllOS00NzI1LWI3MmUtOTJkMmEyNTZiN2JhIiwiYXV0aG9yaXRpZXMiOlsiRFJPSVRfQ0xPVFVSRV9CVURHRVQiLCJEUk9JVF9SQVpfQlVER0VUIl0sIlVTRVJJRCI6IjU0ODQyNjgzODRiN2ZmMWU1ZjI2YjY5MiIsImlhdCI6MTUzNzUzOTQyMCwiaXNzIjoiQnVkZ2V0LVNlcnZpY2VzIHY3LjAuMC1TTkFQU0hPVCIsImV4cCI6MTUzNzU0MzAyMH0.T4WbIgy8FpN3faJfZtiM8OQNGE8P_uBel63t-hniyA2mDRjbLTYxcuSwGvzG7sF8g8lKY_-y-Ex8Jr2HQ9evRQ";
		
		assertNotNull(token);
		Claims claims = JwtConfig.getJWTClaims(token);
		assertNotNull(claims);
	}
}
