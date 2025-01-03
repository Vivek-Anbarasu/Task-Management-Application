package com.restapp.util;

import java.util.Base64;

public class JWTDecoder {
	public static void main(String[] args) {
		String[] jwtToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJWaXZlazEgQXlzaHUxIiwiaWF0IjoxNzM1ODg4NzczLCJleHAiOjE3MzU4OTA1NzN9.cfHRTpdKTH_lBwtQ_6yLOJe2VHmZiUQTS4Y6Calg-rz-imOPPkUXxsfoIYP8R0-xMGfxseb5kTPuVAGH_ptkZw"
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
