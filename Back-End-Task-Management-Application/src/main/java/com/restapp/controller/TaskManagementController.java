package com.restapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.restapp.dto.GetTaskResponse;
import com.restapp.dto.SaveTaskRequest;
import com.restapp.dto.UpdateTaskRequest;
import com.restapp.entity.Task;
import com.restapp.service.TaskService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;

@RestController
@SecurityRequirement(name = "Bearer Authentication")
@RequestMapping("/v1")
@CrossOrigin(origins = {"http://localhost:8080","http://localhost:3000"})
@Slf4j
public class TaskManagementController{

	@Autowired
	private TaskService taskService;

	@GetMapping("/getTask/{taskId}")
	public ResponseEntity<GetTaskResponse> getTask(@NotNull(message="TaskId is mandatory") @PathVariable("taskId") Integer taskId) {
		GetTaskResponse getResponse = null;
		try {
			getResponse = taskService.getTask(taskId);
			if (getResponse == null) {
				log.error("No records found for taskId = " + taskId);
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}else {
				return new ResponseEntity<>(getResponse, HttpStatus.OK);
			}
		} catch (Exception e) {
			log.error("Error in getTask", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/saveTask")
	public ResponseEntity<String> saveTask(@Valid @RequestBody SaveTaskRequest saveRequest) {
		int taskId;
		try {
			Task task = taskService.findByTitle(saveRequest.getTitle());
			if (task != null) {
				log.error("Title already exists: " + saveRequest.getTitle());
				return new ResponseEntity<>("Title already exists", HttpStatus.BAD_REQUEST);
			} else {
				taskId = taskService.saveTask(saveRequest);
				if (taskId == 0) {
					log.error("Failed to saveTask"+saveRequest.getTitle());
					return new ResponseEntity<>("Failed to save", HttpStatus.INTERNAL_SERVER_ERROR);
				}else {
					log.info("Successfully Saved"+saveRequest.getTitle());
					return new ResponseEntity<>(String.valueOf(taskId), HttpStatus.CREATED);
				}
			}
		} catch (Exception e) {
			log.error("Failed to saveTask", e);
			return new ResponseEntity<>("Failed to save", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/updateTask")
	public ResponseEntity<String> updateTask(@Valid @RequestBody UpdateTaskRequest updateRequest) {
		boolean response = false;
		try {
			Task task = taskService.findByTitle(updateRequest.getTitle());
			if (task != null && (task.getTaskId().intValue() != updateRequest.getTaskId().intValue())) {
				log.error("Title already exists: " + updateRequest.getTitle());
				return new ResponseEntity<>("Title already exists", HttpStatus.BAD_REQUEST);
			} else {
				response = taskService.updateTask(updateRequest);
				if (response) {
					log.info("Succesfully Updated: " + updateRequest.getTitle());
					return new ResponseEntity<>("Successfully Updated", HttpStatus.OK);
				} else {
					log.error("No records found for taskId = " + updateRequest.getTaskId());
					return new ResponseEntity<>("No records found for taskId = " + updateRequest.getTaskId(),HttpStatus.NOT_FOUND);
				}
			}
		} catch (Exception e) {
			log.error("Failed to update Task", e);
			return new ResponseEntity<>("Failed to update task", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/deleteTask/{taskId}")
	public ResponseEntity<Void> deleteTask(@NotNull(message="TaskId is mandatory") @PathVariable("taskId") Integer taskId) {
		boolean response;
		GetTaskResponse getResponse = null;
		try {
			getResponse = taskService.getTask(taskId);
			if (getResponse == null) {
				log.error("No records found for taskId = " + taskId);
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			} else {
				response = taskService.deleteTask(taskId);
				if (!response) {
					log.error("Error in deleteTask");
					return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
				}else {
					log.info("Succesfully Deleted :" + taskId);
					return new ResponseEntity<>(HttpStatus.NO_CONTENT);
				}
			}
		} catch (Exception e) {
			log.error("Error in deleteTask", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/getAllTasks")
	public ResponseEntity<List<GetTaskResponse>> getAllTasks() {
		List<GetTaskResponse> responseList = null;
		try {
			responseList = taskService.getAllTasks();
			if (responseList == null) {
				log.info("No records found");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}else {
				return new ResponseEntity<>(responseList, HttpStatus.OK);
			}
		} catch (Exception e) {
			log.error("Error in updateTask", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}