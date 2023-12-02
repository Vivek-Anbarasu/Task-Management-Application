package com.restapp.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.restapp.entity.UserInfo;

import java.util.Optional;

public interface UserInfoRepository extends JpaRepository<UserInfo, Integer> {
    Optional<UserInfo> findByName(String username);
}
