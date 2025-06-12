package com.restapp.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import com.restapp.entity.Task;

import jakarta.persistence.LockModeType;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>{

	Optional<Task> findByTitle(String title);
	 
	 @Lock(LockModeType.PESSIMISTIC_WRITE)
	 Task save(Task task);
	 
    @Lock(LockModeType.PESSIMISTIC_WRITE)
	 Optional<Task> findById(Long id);
}