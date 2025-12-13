package com.restapp.controller;

import com.restapp.dto.AuthRequest;
import com.restapp.entity.UserInfo;
import com.restapp.service.JWTService;
import com.restapp.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

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
    public ResponseEntity<?> authenticate(@RequestBody AuthRequest authRequest) {
    	
    	System.out.println("Request recieved "+authRequest.getUsername());
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));

        String jwtToken = jwtService.generateToken(authRequest.getUsername(), authRequest.getPassword());
        if(jwtToken != null){
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(jwtToken);
            return new ResponseEntity<>("Authentication Successful",headers, HttpStatus.OK);
        }else{
            return new ResponseEntity<>("Username/Password is not valid",HttpStatus.FORBIDDEN);
        }

    }
    
//    @PostMapping(path = "/refresh", produces = MediaType.APPLICATION_JSON_VALUE)
//    public AuthResponse refreshToken(@RequestHeader("Token") String jwtToken) {
//    	String subject = jwtService.extractSubject(jwtToken);
//    	String creds[] = subject.split(" ");
//    	AuthRequest authRequest = new AuthRequest();
//    	authRequest.setUsername(creds[0]);
//    	authRequest.setPassword(creds[1]);
//    	return authenticateAndGetToken(authRequest);
//    }
}
