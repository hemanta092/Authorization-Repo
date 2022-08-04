package com.api.authorization.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class JwtUtil {

	private String secretKey = "tweet";

	public String extractUsername(String token) {
		log.info("ExtractUsername Service Method called!!!");
		return extractClaim(token, Claims::getSubject);
	}

	public Date extractExpiration(String token) {
		log.info("extractExpiration Service Method called!!!");
		return extractClaim(token, Claims::getExpiration);
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		log.info("ExtractClaims Service Method called!!!");
		final Claims claims = extractAllClaims(token);
		log.info("Claims: {}",claims.toString());
		return claimsResolver.apply(claims);
	}

	private Claims extractAllClaims(String token) {
		log.info("ExtractAllClaims Service Method called!!!");
		return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
	}

	private Boolean isTokenExpired(String token) {
		log.info("isTokenExpired Service Method called!!!");
		return extractExpiration(token).before(new Date());
	}

	public String generateToken(UserDetails userDetails) {
		log.info("GenerateToken Service Method called!!!");
		Map<String, Object> claims = new HashMap<>();
		log.info("Claims: {}",claims.toString());
		return createToken(claims, userDetails.getUsername());
	}

	private String createToken(Map<String, Object> claims, String subject) {
		log.info("createToken Service Method called!!!");
		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 3))// token for 3 hrs
				.signWith(SignatureAlgorithm.HS256, secretKey).compact();
	}

	public Boolean validateToken(String token, UserDetails userDetails) {
		log.info("validateToken Service Method called!!!");
		final String username = extractUsername(token);
        log.info("UserName: {}",username);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}

	public Boolean validateToken(String token) {
		log.info("isTokenExpired Service Method called!!!");
		return !isTokenExpired(token);
	}
}