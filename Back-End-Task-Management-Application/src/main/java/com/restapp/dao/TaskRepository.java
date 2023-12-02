package com.restapp.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.restapp.entity.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>{
	Optional<Task> findByTitle(String title);
}