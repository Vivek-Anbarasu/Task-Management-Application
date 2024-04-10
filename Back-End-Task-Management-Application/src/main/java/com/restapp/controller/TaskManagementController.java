package com.restapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import com.restapp.exception.BadRequest;
import com.restapp.exception.InternalServerError;
import com.restapp.exception.NotFound;
import com.restapp.service.TaskService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;

@RestController
@SecurityRequirement(name = "Bearer Authentication")
@RequestMapping("/v1")
@CrossOrigin(origins = {"http://localhost:3000"})
@Slf4j
public class TaskManagementController{

	@Autowired
	private TaskService taskService;

	@GetMapping(path = "/getTask/{taskId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<GetTaskResponse> getTask(@NotNull(message="TaskId is mandatory") @PathVariable("taskId") Integer taskId) {
		GetTaskResponse getResponse = null;
		try {
			getResponse = taskService.getTask(taskId);
			if (getResponse == null) {
				log.error("No records found for taskId = " + taskId);
				throw new NotFound("No records found for taskId = " + taskId);
			}else {
				return new ResponseEntity<>(getResponse, HttpStatus.OK);
			}
		} catch (Exception e) {
			log.error("Error in getTask", e);
			throw new InternalServerError("Error in getTask" + e.getMessage());
		}
	}

	@PostMapping(path = "/saveTask", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> saveTask(@Valid @RequestBody SaveTaskRequest saveRequest) {
		int taskId;
		try {
			Task task = taskService.findByTitle(saveRequest.getTitle());
			if (task != null) {
				log.error("Title already exists: " + saveRequest.getTitle());
				throw new BadRequest("Title already exists");
			} else {
				taskId = taskService.saveTask(saveRequest);
				if (taskId == 0) {
					log.error("Failed to saveTask"+saveRequest.getTitle());
					throw new InternalServerError("Failed to saveTask "+saveRequest.getTitle());
				}else {
					log.info("Successfully Saved"+saveRequest.getTitle());
					return new ResponseEntity<>(String.valueOf(taskId), HttpStatus.CREATED);
				}
			}
		} catch (Exception e) {
			log.error("Failed to saveTask", e);
			throw new InternalServerError("Failed to save " + e.getMessage());
		}
	}

	@PutMapping(path = "/updateTask", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> updateTask(@Valid @RequestBody UpdateTaskRequest updateRequest) {
		boolean response = false;
		try {
			Task task = taskService.findByTitle(updateRequest.getTitle());
			if (task != null && (task.getTaskId().intValue() != updateRequest.getTaskId().intValue())) {
				log.error("Title already exists: " + updateRequest.getTitle());
				throw new BadRequest("Title already exists: " + updateRequest.getTitle());
			} else {
				response = taskService.updateTask(updateRequest);
				if (response) {
					log.info("Succesfully Updated: " + updateRequest.getTitle());
					return new ResponseEntity<>("Successfully Updated", HttpStatus.OK);
				} else {
					log.error("No records found for taskId = " + updateRequest.getTaskId());
					throw new NotFound("No records found for taskId = " + updateRequest.getTaskId());
				}
			}
		} catch (Exception e) {
			log.error("Failed to update Task", e);
			throw new InternalServerError("Failed to update Task " + e.getMessage());
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
				throw new NotFound("No records found for taskId = " + taskId);
			} else {
				response = taskService.deleteTask(taskId);
				if (!response) {
					log.error("Error in deleteTask");
					throw new InternalServerError("Error in deleteTask ");
				}else {
					log.info("Succesfully Deleted :" + taskId);
					return new ResponseEntity<>(HttpStatus.NO_CONTENT);
				}
			}
		} catch (Exception e) {
			log.error("Error in deleteTask", e);
			throw new InternalServerError("Error in deleteTask "+e.getMessage());
		}
	}

	@GetMapping(path = "/getAllTasks" , produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<GetTaskResponse>> getAllTasks() {
		List<GetTaskResponse> responseList = null;
		try {
			responseList = taskService.getAllTasks();
			if (responseList == null) {
				log.info("No records found");
				throw new NotFound("No records found ");
			}else {
				return new ResponseEntity<>(responseList, HttpStatus.OK);
			}
		} catch (Exception e) {
			log.error("Error in getAllTasks", e);
			throw new InternalServerError("Error in getAllTasks "+e.getMessage());
		}
	}
}