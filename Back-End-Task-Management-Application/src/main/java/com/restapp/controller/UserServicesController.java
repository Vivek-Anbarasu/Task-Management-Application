package com.restapp.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.restapp.dto.AuthRequest;
import com.restapp.dto.AuthResponse;
import com.restapp.entity.UserInfo;
import com.restapp.service.JWTService;
import com.restapp.service.RegistrationService;

@RestController
@RequestMapping("/user")
public class UserServicesController {

    @Autowired
    private RegistrationService registrationService;
    
    @Autowired
    private JWTService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping(path = "/new-registration", produces = MediaType.APPLICATION_JSON_VALUE)
    public String addNewUser(@RequestBody UserInfo userInfo) {

    	Optional<UserInfo> optuserInfo = registrationService.findByName(userInfo.getName());
    	
    	if(optuserInfo.isPresent()) {
    		return "User Name is not available. Please choose another User Name";
    	}
    	
        return registrationService.addUser(userInfo);
    }

    @PostMapping(path = "/authenticate", produces = MediaType.APPLICATION_JSON_VALUE)
    public AuthResponse authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
    	
    	System.out.println("Request recieved "+authRequest.getUsername());
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        AuthResponse authResponse;
        if (authentication.isAuthenticated()) {
        	authResponse = new AuthResponse();
        	authResponse.setAccessToken(jwtService.generateToken(authRequest.getUsername(), authRequest.getPassword()));
        } else {
        	authResponse = new AuthResponse();
        	authResponse.setAccessToken("Credentials not valid");
        }
		return authResponse;
    }
    
    @PostMapping(path = "/refresh", produces = MediaType.APPLICATION_JSON_VALUE)
    public AuthResponse refreshToken(@RequestHeader("Token") String jwtToken) {
    	String subject = jwtService.extractSubject(jwtToken);
    	String creds[] = subject.split(" ");
    	AuthRequest authRequest = new AuthRequest();
    	authRequest.setUsername(creds[0]);
    	authRequest.setPassword(creds[1]);
    	return authenticateAndGetToken(authRequest);
    }
}
