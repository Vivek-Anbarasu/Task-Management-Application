package com.restapp.util;

import java.util.Base64;

public class JWTDecoder {
	public static void main(String[] args) {
		String[] jwtToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJWaXZlazEgQXlzaHUxIiwiaWF0IjoxNzYxMzAzMzE5LCJleHAiOjE3NjEzMDUxMTl9.UP_hS_zfvWgyZnNQSOWS9CmoYoGebJF5Ex4GtHlX6JAS17d38a4PwXl2Mc7j8MkYQtEupVKyLmNmTslmNYlz9w"
				.split("\\.");
		Base64.Decoder decoder = Base64.getUrlDecoder();
		
		String header = new String(decoder.decode(jwtToken[0]));
		String payload = new String(decoder.decode(jwtToken[1]));
		String signature = new String(decoder.decode(jwtToken[2]));
		
		System.out.println("Header = "+ header);
		System.out.println("payload = "+ payload);
		System.out.println("signature = "+ signature);
		
		// iat - Issued at time is in seconds
		
		long validity = 1730288520 -  1730286720; 
		System.out.println("Minutes = "+validity/60);
		
		System.out.println(System.currentTimeMillis()/1000);
		
//		long expiry = System.currentTimeMillis()/1000 -  1730286720;
//		System.out.println("Minutes = "+expiry/60);

	}
}
