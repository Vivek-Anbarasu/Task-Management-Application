package com.restapp.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restapp.dto.GetTaskResponse;
import com.restapp.dto.SaveTaskRequest;
import com.restapp.dto.UpdateTaskRequest;
import com.restapp.entity.Task;
import com.restapp.filter.JwtAuthFilter;
import com.restapp.service.JWTService;
import com.restapp.service.TaskService;

@WebMvcTest(TaskManagementController.class)
@AutoConfigureMockMvc(addFilters = false)
public class TaskManagementControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private TaskService taskService;

	@MockBean
	private JWTService jwtService;

	@MockBean
	private JwtAuthFilter jwtAuthFilter;

	@Test
	public void testGetTask() throws Exception {
		GetTaskResponse task = GetTaskResponse.builder().
				taskId(100).title("Task Title").
				description("Task Description").status("In Progress").build();

		when(taskService.getTask(100)).thenReturn(task);
		mockMvc.perform(get("/v1/getTask/{taskId}", "100")).andExpect(status().isOk());
	}

	@Test
	public void testGetTaskThrowException() throws Exception {
		when(taskService.getTask(100)).thenThrow(NullPointerException.class);
		mockMvc.perform(get("/v1/getTask/{taskId}", "100")).andExpect(status().isInternalServerError());
	}

	@Test
	public void testGetTaskNoRecords() throws Exception {
		when(taskService.getTask(100)).thenReturn(null);
		mockMvc.perform(get("/v1/getTask/{taskId}", "100")).andExpect(status().isNotFound());
	}

	@Test
	public void testSaveTaskMandatoryParams() throws Exception {
		SaveTaskRequest request = new SaveTaskRequest();
		request.setTitle("Test Title");
		request.setDescription("Task Description");

		mockMvc.perform(post("/v1/saveTask").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))).andExpect(status().isBadRequest());
	}

	@Test
	public void testSaveTaskTitleAlreadyExists() throws Exception {
		SaveTaskRequest request = new SaveTaskRequest();
		request.setTitle("Test Title");
		request.setStatus("In Progress");
		request.setDescription("Task Description");

		when(taskService.findByTitle("Test Title")).thenReturn(new Task());

		mockMvc.perform(post("/v1/saveTask").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))).andExpect(status().isBadRequest());
	}

	@Test
	public void testSaveTaskFailed() throws Exception {
		SaveTaskRequest request = new SaveTaskRequest();
		request.setTitle("Test Title");
		request.setStatus("In Progress");
		request.setDescription("Task Description");

		when(taskService.findByTitle(request.getTitle())).thenReturn(null);
		when(taskService.saveTask(request)).thenReturn(100);

		mockMvc.perform(post("/v1/saveTask").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))).andExpect(status().isInternalServerError());
	}

	@Test
	public void testSaveTaskSuccess() throws Exception {
		SaveTaskRequest request = new SaveTaskRequest();
		request.setTitle("Test Title");
		request.setStatus("In Progress");
		request.setDescription("Task Description");

		when(taskService.findByTitle("Test Title")).thenReturn(null);
		when(taskService.saveTask(any())).thenReturn(100);

		mockMvc.perform(post("/v1/saveTask").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))).andExpect(status().isCreated());
	}

	@Test
	public void testSaveTaskException() throws Exception {
		SaveTaskRequest request = new SaveTaskRequest();
		request.setTitle("Test Title");
		request.setStatus("In Progress");
		request.setDescription("Task Description");

		when(taskService.findByTitle(request.getTitle())).thenThrow(NullPointerException.class);

		mockMvc.perform(post("/v1/saveTask").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))).andExpect(status().isInternalServerError());
	}

	@Test
	public void testDeleteTask() throws Exception {
		GetTaskResponse task = GetTaskResponse.builder().
				taskId(100).title("Task Title").
				description("Task Description").status("In Progress").build();

		when(taskService.getTask(100)).thenReturn(task);
		when(taskService.deleteTask(100)).thenReturn(true);
		mockMvc.perform(delete("/v1/deleteTask/{taskId}", "100")).andExpect(status().isNoContent());
	}

	@Test
	public void testDeleteTaskError() throws Exception {
		GetTaskResponse task = GetTaskResponse.builder().
				taskId(100).title("Task Title").
				description("Task Description").status("In Progress").build();

		when(taskService.getTask(100)).thenReturn(task);
		when(taskService.deleteTask(100)).thenReturn(false);
		mockMvc.perform(delete("/v1/deleteTask/{taskId}", "100")).andExpect(status().isInternalServerError());
	}

	@Test
	public void testDeleteFailed() throws Exception {
		when(taskService.getTask(100)).thenReturn(null);
		mockMvc.perform(delete("/v1/deleteTask/{taskId}", "100")).andExpect(status().isNotFound());
	}

	@Test
	public void testDeleteException() throws Exception {
		when(taskService.getTask(100)).thenThrow(NullPointerException.class);
		mockMvc.perform(delete("/v1/deleteTask/{taskId}", "100")).andExpect(status().isInternalServerError());
	}

	@Test
	public void testGetAllTasks() throws Exception {
		List<GetTaskResponse> responseList = new ArrayList<>();
		GetTaskResponse task = GetTaskResponse.builder().
				taskId(100).title("Task Title").
				description("Task Description").status("In Progress").build();
		responseList.add(task);
		when(taskService.getAllTasks()).thenReturn(responseList);

		mockMvc.perform(get("/v1/getAllTasks").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}

	@Test
	public void testGetAllTasksNoData() throws Exception {
		when(taskService.getAllTasks()).thenReturn(null);
		mockMvc.perform(get("/v1/getAllTasks").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	public void testGetAllTasks_Exception() throws Exception {
		when(taskService.getAllTasks()).thenThrow(new NullPointerException());
		mockMvc.perform(get("/v1/getAllTasks").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError());
	}

	@Test
	public void testUpdateTask_Exception() throws Exception {
		UpdateTaskRequest updateRequest = new UpdateTaskRequest();
		updateRequest.setTaskId(103);
		updateRequest.setDescription("Task Description");
		updateRequest.setTitle("Task Title");
		updateRequest.setStatus("In Progress");

		when(taskService.findByTitle(updateRequest.getTitle())).thenReturn(new Task());
		when(taskService.updateTask(updateRequest)).thenReturn(true);

		mockMvc.perform(MockMvcRequestBuilders.put("/v1/updateTask").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updateRequest))).andExpect(status().isInternalServerError());
	}

	@Test
	public void testUpdateTask_Success() throws Exception {
		UpdateTaskRequest updateRequest = new UpdateTaskRequest();
		updateRequest.setTaskId(103);
		updateRequest.setDescription("Task Description");
		updateRequest.setTitle("Task Title");
		updateRequest.setStatus("In Progress");

		when(taskService.updateTask(any())).thenReturn(true);

		mockMvc.perform(put("/v1/updateTask").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updateRequest))).andExpect(status().isOk())
				.andExpect(content().string("Successfully Updated"));
	}



	@Test
	public void testUpdateTask_NotFound() throws Exception {
		UpdateTaskRequest updateRequest = new UpdateTaskRequest();
		updateRequest.setTaskId(100);
		updateRequest.setDescription("Task Description");
		updateRequest.setTitle("Task Title");
		updateRequest.setStatus("In Progress");
		when(taskService.findByTitle(updateRequest.getTitle())).thenReturn(null);
		when(taskService.updateTask(updateRequest)).thenReturn(false);

		mockMvc.perform(put("/v1/updateTask").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updateRequest))).andExpect(status().isNotFound())
				.andExpect(content().string("No records found for taskId = " + updateRequest.getTaskId()));
	}

}