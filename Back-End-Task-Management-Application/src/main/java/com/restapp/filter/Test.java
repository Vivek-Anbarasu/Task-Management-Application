package com.restapp.filter;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Encoders;

public class Test {
public static void main(String[] args) {
    SecretKey key = Jwts.SIG.HS512.key().build();
    String secretString = Encoders.BASE64.encode(key.getEncoded());
    System.out.println(secretString);
}
}
