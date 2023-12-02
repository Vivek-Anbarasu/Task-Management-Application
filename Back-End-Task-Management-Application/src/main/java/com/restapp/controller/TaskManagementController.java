package com.restapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.rest.DeleteTaskRequest;
import com.rest.DeleteTaskResponse;
import com.rest.GetTaskRequest;
import com.rest.GetTaskResponse;
import com.rest.SaveTaskRequest;
import com.rest.SaveTaskResponse;
import com.rest.TaskApi;
import com.rest.UpdateTaskRequest;
import com.rest.UpdateTaskResponse;
import com.restapp.entity.Task;
import com.restapp.exception.ApplicationException;
import com.restapp.service.TaskService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;

@RestController
@SecurityRequirement(name = "Bearer Authentication")
@CrossOrigin(origins = "http://localhost:3000/")
@Slf4j
public class TaskManagementController implements TaskApi {

	@Autowired
	private TaskService taskService;

	@Override
	public ResponseEntity<GetTaskResponse> getTask(GetTaskRequest getRequest) {
		GetTaskResponse getResponse = null;
		try {
			getResponse = taskService.getTask(getRequest.getTaskId());
		} catch (Exception e) {
			log.error("Error in getTask", e);
		}
		if (getResponse == null) {
			log.error("No records found for taskId = " + getRequest.getTaskId());
			throw new ApplicationException(HttpStatus.BAD_REQUEST,
					"No records found for taskId = " + getRequest.getTaskId());
		}
		return new ResponseEntity<>(getResponse, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<SaveTaskResponse> saveTask(SaveTaskRequest saveRequest) {
		boolean response = false;
		SaveTaskResponse saveResponse = null;
		try {
			if (ObjectUtils.isEmpty(saveRequest.getTitle()) || ObjectUtils.isEmpty(saveRequest.getStatus())) {
				throw new ApplicationException(HttpStatus.BAD_REQUEST, "Task Title and Status is mandatory");
			}
			if (taskService.findByTitle(saveRequest.getTitle())) {
				saveResponse = new SaveTaskResponse();
				saveResponse.setMessage("Title already exists: " + saveRequest.getTitle());
				log.error("Title already exists: " + saveRequest.getTitle());
				return new ResponseEntity<>(saveResponse, HttpStatus.OK);
			} else {
				response = taskService.saveTask(saveRequest);
			}
		} catch (Exception e) {
			log.error("Error in saveTask", e);
		}
		if (response) {
			saveResponse = new SaveTaskResponse();
			saveResponse.setMessage("Successfully Saved");
			log.info("Successfully Saved: " + saveRequest.getTitle());
			return new ResponseEntity<>(saveResponse, HttpStatus.OK);
		} else {
			saveResponse = new SaveTaskResponse();
			saveResponse.setMessage("Failed to save");
			log.info("Failed to save: " + saveRequest.getTitle());
			return new ResponseEntity<>(saveResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public ResponseEntity<UpdateTaskResponse> updateTask(UpdateTaskRequest updateRequest) {
		boolean response = false;
		UpdateTaskResponse updateResponse = null;
		try {
			if (ObjectUtils.isEmpty(updateRequest.getTaskId()) || ObjectUtils.isEmpty(updateRequest.getStatus())) {
				throw new ApplicationException(HttpStatus.BAD_REQUEST, "Task Title and Status is mandatory");
			}
			Task task = taskService.findByTitleTask(updateRequest.getTitle());
			if (task !=  null && (task.getTaskId().intValue() != updateRequest.getTaskId().intValue())) {
				updateResponse = new UpdateTaskResponse();
				updateResponse.setMessage("Title already exists: " + updateRequest.getTitle());
				log.error("Title already exists: " + updateRequest.getTitle());
				return new ResponseEntity<>(updateResponse, HttpStatus.OK);
			} else {
			response = taskService.updateTask(updateRequest);
			}
		} catch (Exception e) {
			log.error("Error in updateTask", e);
		}
		if (response) {
			updateResponse = new UpdateTaskResponse();
			updateResponse.setMessage("Successfully Updated");
			log.info("Succesfully Updated: " + updateRequest.getTitle());
			return new ResponseEntity<>(updateResponse, HttpStatus.OK);
		} else {
			log.error("No records found for getTaskId = " + updateRequest.getTaskId());
			throw new ApplicationException(HttpStatus.BAD_REQUEST,
					"No records found for getTaskId = " + updateRequest.getTaskId());
		}
	}

	@Override
	public ResponseEntity<DeleteTaskResponse> deleteTask(DeleteTaskRequest deleteRequest) {
		boolean response = false;
		try {
			response = taskService.deleteTask(deleteRequest.getTaskId());
		} catch (Exception e) {
			log.error("Error in updateTask", e);
		}
		if (response) {
			DeleteTaskResponse deleteResponse = new DeleteTaskResponse();
			deleteResponse.setMessage("Succesfully Deleted");
			log.info("Succesfully Deleted :" + deleteRequest.getTaskId());
			return new ResponseEntity<>(deleteResponse, HttpStatus.OK);
		} else {
			log.error("No records found for getTaskId = " + deleteRequest.getTaskId());
			throw new ApplicationException(HttpStatus.BAD_REQUEST,
					"No records found for getTaskId = " + deleteRequest.getTaskId());
		}
	}

	@Override
	public ResponseEntity<List<GetTaskResponse>> getAllTasks() {
		List<GetTaskResponse> responseList = null;
		try {
			responseList = taskService.getAllTasks();
			if (responseList == null) {
				log.info("No records found");
				throw new ApplicationException(HttpStatus.BAD_REQUEST, "No records found ");
			}
		} catch (Exception e) {
			log.error("Error in updateTask", e);
		}
		return new ResponseEntity<>(responseList, HttpStatus.OK);
	}

}