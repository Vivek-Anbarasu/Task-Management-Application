package com.restapp.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.restapp.dao.UserInfoRepository;
import com.restapp.entity.UserInfo;

@Service
public class RegistrationService {


    @Autowired
    private UserInfoRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    
    public Optional<UserInfo> findByName(String name) {
    	return repository.findByName(name);
    }

    public String addUser(UserInfo userInfo) {
        userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
        repository.save(userInfo);
        return "User Succesfully Registered";
    }
}
