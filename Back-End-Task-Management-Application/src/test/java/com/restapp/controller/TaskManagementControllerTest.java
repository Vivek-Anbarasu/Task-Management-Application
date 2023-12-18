package com.restapp.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restapp.dto.GetTaskResponse;
import com.restapp.dto.SaveTaskRequest;
import com.restapp.dto.UpdateTaskRequest;
import com.restapp.filter.JwtAuthFilter;
import com.restapp.service.JWTService;
import com.restapp.service.TaskService;

@WebMvcTest(TaskManagementController.class)
@AutoConfigureMockMvc(addFilters = false)
public class TaskManagementControllerTest{

	@Autowired
    private MockMvc mockMvc;
	
    @MockBean
	private TaskService taskService;
    
    @MockBean
    private JWTService jwtService;
    
    @MockBean
    private JwtAuthFilter jwtAuthFilter;

	@Test 
	public void testGetTask() throws Exception{
		GetTaskResponse task = new GetTaskResponse();
		task.setTaskId(100);
		task.setDescription("Task Description");
		task.setTitle("Task Title");
		task.setStatus("In Progress");
		
		when(taskService.getTask(100)).thenReturn(task);
		mockMvc.perform(get("/v1/getTask/{taskId}","100")).andExpect(status().isOk());
	}
	
	@Test 
	public void testGetTaskThrowException() throws Exception{
		GetTaskResponse task = new GetTaskResponse();
		task.setTaskId(100);
		task.setDescription("Task Description");
		task.setTitle("Task Title");
		task.setStatus("In Progress");

		when(taskService.getTask(100)).thenThrow(NullPointerException.class);
		mockMvc.perform(get("/v1/getTask/{taskId}","100")).andExpect(status().isInternalServerError());
	}
	
	@Test 
	public void testGetTaskNoRecords() throws Exception{		
		when(taskService.getTask(100)).thenReturn(null);
		mockMvc.perform(get("/v1/getTask/{taskId}","100")).andExpect(status().isNotFound());
	}
	
	@Test 
	public void testSaveTaskMandatoryParams() throws Exception{		
		SaveTaskRequest request = new SaveTaskRequest();
		request.setTitle("Test Title");
		request.setDescription("Task Description");
		
		ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(request);
		mockMvc.perform(post("/v1/saveTask").contentType("application/json").content(json)).andExpect(status().isBadRequest());
	}
    
	@Test 
	public void testSaveTaskTitleAlreadyExists() throws Exception{		
		SaveTaskRequest request = new SaveTaskRequest();
		request.setTitle("Test Title");
		request.setStatus("In Progress");
		request.setDescription("Task Description");
		
		when(taskService.findByTitle("Test Title")).thenReturn(true);
		
		ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(request);
		mockMvc.perform(post("/v1/saveTask").contentType("application/json").content(json)).andExpect(status().isBadRequest());
	}
	

	

	
	@Test 
	public void testSaveTaskFailed() throws Exception{		
		SaveTaskRequest request = new SaveTaskRequest();
		request.setTitle("Test Title");
		request.setStatus("In Progress");
		request.setDescription("Task Description");
		
		when(taskService.findByTitle("Test Title")).thenReturn(false);
		when(taskService.saveTask(request)).thenReturn(false);
		
		ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(request);
		mockMvc.perform(post("/v1/saveTask").contentType("application/json").content(json)).andExpect(status().isInternalServerError());
	}
	
	@Test 
	public void testSaveTaskException() throws Exception{		
		SaveTaskRequest request = new SaveTaskRequest();
		request.setTitle("Test Title");
		request.setStatus("In Progress");
		request.setDescription("Task Description");
		
		when(taskService.saveTask(request)).thenThrow(NullPointerException.class);
		
		ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(request);
		mockMvc.perform(post("/v1/saveTask").contentType("application/json").content(json)).andExpect(status().isInternalServerError());
	}
	



	

	
	@Test 
	public void testDeleteTask() throws Exception{
		GetTaskResponse task = new GetTaskResponse();
		task.setTaskId(100);
		task.setDescription("Task Description");
		task.setTitle("Task Title");
		task.setStatus("In Progress");
		
		when(taskService.getTask(100)).thenReturn(task);
		when(taskService.deleteTask(100)).thenReturn(true);
		mockMvc.perform(delete("/v1/deleteTask/{taskId}","100")).andExpect(status().isNoContent());
	}
	
	@Test 
	public void testDeleteFailed() throws Exception{
		GetTaskResponse task = new GetTaskResponse();
		task.setTaskId(100);
		task.setDescription("Task Description");
		task.setTitle("Task Title");
		task.setStatus("In Progress");
		
		when(taskService.getTask(100)).thenReturn(null);
		mockMvc.perform(delete("/v1/deleteTask/{taskId}","100")).andExpect(status().isNotFound());
	}
	
	@Test 
	public void testDeleteException() throws Exception{
		when(taskService.getTask(100)).thenThrow(NullPointerException.class);
		mockMvc.perform(delete("/v1/deleteTask/{taskId}","100")).andExpect(status().isInternalServerError());
	}
	
	
	@Test 
	public void testGetAllTasks() throws Exception{		
		List<GetTaskResponse> responseList = new ArrayList<>();
		GetTaskResponse task = new GetTaskResponse();
		task.setTaskId(100);
		task.setDescription("Task Description");
		task.setTitle("Task Title");
		task.setStatus("In Progress");
		responseList.add(task);
		when(taskService.getAllTasks()).thenReturn(responseList);

		mockMvc.perform(get("/v1/getAllTasks")
			    .contentType("application/json"))
			    .andExpect(status().isOk());
	}
	
	@Test 
	public void testGetAllTasksNoData() throws Exception{		
		when(taskService.getAllTasks()).thenReturn(null);
		mockMvc.perform(get("/v1/getAllTasks").contentType("application/json")).andExpect(status().isNotFound());
	}

}