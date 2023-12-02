package com.restapp.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rest.DeleteTaskRequest;
import com.rest.DeleteTaskResponse;
import com.rest.GetTaskRequest;
import com.rest.GetTaskResponse;
import com.rest.SaveTaskRequest;
import com.rest.SaveTaskResponse;
import com.rest.UpdateTaskRequest;
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
		
		GetTaskRequest request = new GetTaskRequest(100);
		
		when(taskService.getTask(100)).thenReturn(task);
		
		ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(request);
		
		mockMvc.perform(post("/v1/getTask")
			    .contentType("application/json")
				.content(json))
			    .andExpect(status().isOk());
	}
	
	@Test 
	public void testGetTaskThrowException() throws Exception{
		GetTaskResponse task = new GetTaskResponse();
		task.setTaskId(100);
		task.setDescription("Task Description");
		task.setTitle("Task Title");
		task.setStatus("In Progress");
		
		GetTaskRequest request = new GetTaskRequest(100);
		
		when(taskService.getTask(100)).thenThrow(NullPointerException.class);
		
		ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(request);
		
		mockMvc.perform(post("/v1/getTask")
			    .contentType("application/json")
				.content(json))
			    .andExpect(status().isBadRequest());
	}
	
	@Test 
	public void testGetTaskNoRecords() throws Exception{		
		GetTaskRequest request = new GetTaskRequest(100);
		
		when(taskService.getTask(100)).thenReturn(null);
		
		ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(request);
		
		mockMvc.perform(post("/v1/getTask")
			    .contentType("application/json")
				.content(json))
			    .andExpect(status().isBadRequest());
	}
    
	@Test 
	public void testSaveTask() throws Exception{		
		SaveTaskRequest request = new SaveTaskRequest();
		request.setTitle("Test Title");
		request.setStatus("In Progress");
		request.setDescription("Task Description");
		
		when(taskService.saveTask(request)).thenReturn(true);
		when(taskService.findByTitle("Test Title")).thenReturn(true);
		
		ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(request);
		mockMvc.perform(post("/v1/saveTask")
			    .contentType("application/json")
				.content(json))
			    .andExpect(status().isOk());
	}
	
	@Test 
	public void testSaveNotExists() throws Exception{		
		SaveTaskRequest request = new SaveTaskRequest();
		request.setTitle("Test Title");
		request.setStatus("In Progress");
		request.setDescription("Task Description");
		
		when(taskService.saveTask(request)).thenReturn(true);
		when(taskService.findByTitle("Test Title")).thenReturn(false);
		
		ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(request);
		mockMvc.perform(post("/v1/saveTask")
			    .contentType("application/json")
				.content(json))
			    .andExpect(status().isOk());
	}
	
	@Test 
	public void testSaveTaskFailed() throws Exception{		
		SaveTaskRequest request = new SaveTaskRequest();
		request.setTitle("Test Title");
		request.setStatus("In Progress");
		request.setDescription("Task Description");
		
		when(taskService.saveTask(request)).thenReturn(false);
		
		ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(request);
		System.out.println(json);
		mockMvc.perform(post("/v1/saveTask")
			    .contentType("application/json")
				.content(json))
			    .andExpect(status().isInternalServerError());
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
		System.out.println(json);
		mockMvc.perform(post("/v1/saveTask")
			    .contentType("application/json")
				.content(json))
			    .andExpect(status().isInternalServerError());
	}
	
	@Test 
	public void testSaveTaskBadRequest() throws Exception{		
		SaveTaskRequest request = new SaveTaskRequest();
		request.setTitle("Test Title");
		request.setDescription("Task Description");
		
		when(taskService.saveTask(request)).thenReturn(true);
		
		ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(request);
		System.out.println(json);
		mockMvc.perform(post("/v1/saveTask")
			    .contentType("application/json")
				.content(json))
			    .andExpect(status().isInternalServerError());
	}
	
	@Test 
	public void testUpdateTask() throws Exception{		
		UpdateTaskRequest request = new UpdateTaskRequest();
		request.setTaskId(100);
		request.setTitle("Test Title");
		request.setStatus("In Progress");
		request.setDescription("Task Description");
		
		when(taskService.updateTask(request)).thenReturn(true);
		
		ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(request);
		mockMvc.perform(post("/v1/updateTask")
			    .contentType("application/json")
				.content(json))
			    .andExpect(status().isOk());
	}
	
	@Test 
	public void testUpdateException() throws Exception{		
		UpdateTaskRequest request = new UpdateTaskRequest();
		request.setTaskId(100);
		request.setTitle("Test Title");
		request.setStatus("In Progress");
		request.setDescription("Task Description");
		
		when(taskService.updateTask(request)).thenThrow(NullPointerException.class);
		
		ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(request);
		mockMvc.perform(post("/v1/updateTask")
			    .contentType("application/json")
				.content(json))
			    .andExpect(status().isBadRequest());
	}
	
	@Test 
	public void testUpdateBadRequest() throws Exception{		
		UpdateTaskRequest request = new UpdateTaskRequest();
		request.setTaskId(100);
		request.setTitle("Test Title");
		request.setDescription("Task Description");
		
		when(taskService.updateTask(request)).thenReturn(true);
		
		ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(request);
		mockMvc.perform(post("/v1/updateTask")
			    .contentType("application/json")
				.content(json))
			    .andExpect(status().isBadRequest());
	}
	
	@Test 
	public void testDeleteTask() throws Exception{
		DeleteTaskRequest request = new DeleteTaskRequest(100);
		
		when(taskService.deleteTask(100)).thenReturn(true);
		
		ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(request);
		
		mockMvc.perform(post("/v1/deleteTask")
			    .contentType("application/json")
				.content(json))
			    .andExpect(status().isOk());
	}
	
	@Test 
	public void testDeleteFailed() throws Exception{
		DeleteTaskRequest request = new DeleteTaskRequest(100);
		
		when(taskService.deleteTask(100)).thenReturn(false);
		
		ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(request);
		
		mockMvc.perform(post("/v1/deleteTask")
			    .contentType("application/json")
				.content(json))
			    .andExpect(status().isBadRequest());
	}
	
	@Test 
	public void testDeleteException() throws Exception{
		DeleteTaskRequest request = new DeleteTaskRequest(100);
		
		when(taskService.deleteTask(100)).thenThrow(NullPointerException.class);
		
		ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(request);
		
		mockMvc.perform(post("/v1/deleteTask")
			    .contentType("application/json")
				.content(json))
			    .andExpect(status().isBadRequest());
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

		mockMvc.perform(post("/v1/getAllTasks")
			    .contentType("application/json"))
			    .andExpect(status().isOk());
	}
	
	@Test 
	public void testGetAllTasksNoData() throws Exception{		

		when(taskService.getAllTasks()).thenReturn(null);

		mockMvc.perform(post("/v1/getAllTasks")
			    .contentType("application/json"))
			    .andExpect(status().isOk());
	}


	
}