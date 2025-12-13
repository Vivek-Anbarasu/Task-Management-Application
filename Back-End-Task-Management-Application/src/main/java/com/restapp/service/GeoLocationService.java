package com.restapp.service;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.Country;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

@Service
public class GeoLocationService {

    private DatabaseReader dbReader;

    public String getCountryCode(String ipAddress) throws IOException, GeoIp2Exception {
    	
    	File database = new ClassPathResource("GeoLite2-City.mmdb").getFile();
        dbReader = new DatabaseReader.Builder(database).build();
        
        InetAddress ipAddressObj = InetAddress.getByName(ipAddress);
        CityResponse response = dbReader.city(ipAddressObj);
        Country country = response.getCountry();
        return country.getIsoCode(); // Returns country code, e.g., "US"
    }
}
