package com.restapp.controller;

import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.restapp.service.GeoLocationService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class LocationController {

	@Autowired
	private  GeoLocationService geoLocationService;


    @GetMapping("/track")
    public String trackUserCountry(HttpServletRequest request) {
        String ipAddress = request.getRemoteAddr();
        String localAddress = request.getLocalAddr();
        System.out.println(localAddress);
        System.out.println(ipAddress);
        
        try {
            String countryCode = geoLocationService.getCountryCode(ipAddress);
            return "User is from: " + countryCode;
        } catch (IOException | GeoIp2Exception e) {
            e.printStackTrace();
            return "Could not determine location.";
        }
    }
	
}
