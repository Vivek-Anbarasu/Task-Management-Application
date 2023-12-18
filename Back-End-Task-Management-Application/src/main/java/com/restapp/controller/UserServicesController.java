package com.restapp.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @PostMapping("/new-registration")
    public String addNewUser(@RequestBody UserInfo userInfo) {

    	Optional<UserInfo> optuserInfo = registrationService.findByName(userInfo.getName());
    	
    	if(optuserInfo.isPresent()) {
    		return "User Name is not available. Please choose another User Name";
    	}
    	
        return registrationService.addUser(userInfo);
    }

    @PostMapping("/authenticate")
    public AuthResponse authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
    	
    	System.out.println("Request recieved "+authRequest.getUsername());
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        AuthResponse authResponse;
        if (authentication.isAuthenticated()) {
        	authResponse = new AuthResponse();
        	authResponse.setAccessToken(jwtService.generateToken(authRequest.getUsername()));
        } else {
        	authResponse = new AuthResponse();
        	authResponse.setAccessToken("Credentials not valid");
        }
		return authResponse;
    }
}
