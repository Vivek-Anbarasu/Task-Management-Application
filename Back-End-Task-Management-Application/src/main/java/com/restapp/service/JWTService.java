package com.restapp.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JWTService {


    private static final String SECRET="UMGWByE8Ja/FyDFLqqOnKCN4GiFd+cm01UQnk+HTZjYAOUxTu7tEMyfXTBePrxQ4wNDfcmGymX0KgnS/9FGKvA==";
	private static final long TOKEN_VALIDITY = 1000*60*30; // 30 minutes


    public String extractSubject(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser() .verifyWith(getSigningKey()).build().parseSignedClaims(token) .getPayload();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String subject = extractSubject(token);
        String creds[] = subject.split(" ");
        return (creds[0].equals(userDetails.getUsername()) && !isTokenExpired(token));
    }


    public String generateToken(String userName, String password){
        Map<String,Object> claims=new HashMap<>();
        return createToken(claims,userName,password);
    }

    private String createToken(Map<String, Object> claims, String userName, String password) {
        return Jwts.builder().claims(claims).subject(userName+" "+password).issuedAt(new Date(System.currentTimeMillis())).expiration(new Date(System.currentTimeMillis()+TOKEN_VALIDITY))
        		.signWith(getSigningKey()).compact();
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes= Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
