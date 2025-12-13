package com.restapp.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Encoders;

import javax.crypto.SecretKey;

public class JWTSecretGenerator {
	public static void main(String[] args) {
		SecretKey key = Jwts.SIG.HS512.key().build();
		String encodedSecret = Encoders.BASE64.encode(key.getEncoded());
		System.out.println(encodedSecret);
	}
}
