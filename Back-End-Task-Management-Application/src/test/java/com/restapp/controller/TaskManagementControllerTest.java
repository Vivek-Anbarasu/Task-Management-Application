package com.restapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restapp.dto.GetTaskResponse;
import com.restapp.dto.SaveTaskRequest;
import com.restapp.dto.UpdateTaskRequest;
import com.restapp.entity.Task;
import com.restapp.filter.JwtAuthFilter;
import com.restapp.service.JWTService;
import com.restapp.service.TaskService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
	@SneakyThrows
	public void testGetTask(){
		GetTaskResponse task = GetTaskResponse.builder().
				taskId(100).title("Task Title").
				description("Task Description").status("In Progress").build();

		when(taskService.getTask(100)).thenReturn(task);
		mockMvc.perform(get("/v1/getTask/{taskId}", "100")).andExpect(status().isOk());
	}

	@Test
	@SneakyThrows
	public void testGetTaskThrowException() {
		when(taskService.getTask(100)).thenThrow(NullPointerException.class);
		mockMvc.perform(get("/v1/getTask/{taskId}", "100")).andExpect(status().isInternalServerError());
	}

	@Test
	@SneakyThrows
	public void testGetTaskNoRecords(){
		when(taskService.getTask(100)).thenReturn(null);
		mockMvc.perform(get("/v1/getTask/{taskId}", "100")).andExpect(status().isNotFound());
	}

	@Test
	@SneakyThrows
	public void testSaveTaskMandatoryParams(){
		SaveTaskRequest request = new SaveTaskRequest();
		request.setTitle("Test Title");
		request.setDescription("Task Description");

		mockMvc.perform(post("/v1/saveTask").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))).andExpect(status().isBadRequest());
	}

	@Test
	@SneakyThrows
	public void testSaveTaskTitleAlreadyExists(){
		SaveTaskRequest request = new SaveTaskRequest();
		request.setTitle("Test Title");
		request.setStatus("In Progress");
		request.setDescription("Task Description");

		when(taskService.findByTitle("Test Title")).thenReturn(new Task());

		mockMvc.perform(post("/v1/saveTask").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))).andExpect(status().isBadRequest());
	}

	@Test
	@SneakyThrows
	public void testSaveTaskFailed(){
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
	@SneakyThrows
	public void testSaveTaskSuccess(){
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
	@SneakyThrows
	public void testSaveTaskException(){
		SaveTaskRequest request = new SaveTaskRequest();
		request.setTitle("Test Title");
		request.setStatus("In Progress");
		request.setDescription("Task Description");

		when(taskService.findByTitle(request.getTitle())).thenThrow(NullPointerException.class);

		mockMvc.perform(post("/v1/saveTask").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))).andExpect(status().isInternalServerError());
	}

	@Test
	@SneakyThrows
	public void testDeleteTask(){
		GetTaskResponse task = GetTaskResponse.builder().
				taskId(100).title("Task Title").
				description("Task Description").status("In Progress").build();

		when(taskService.getTask(100)).thenReturn(task);
		when(taskService.deleteTask(100)).thenReturn(true);
		mockMvc.perform(delete("/v1/deleteTask/{taskId}", "100")).andExpect(status().isNoContent());
	}

	@Test
	@SneakyThrows
	public void testDeleteTaskError(){
		GetTaskResponse task = GetTaskResponse.builder().
				taskId(100).title("Task Title").
				description("Task Description").status("In Progress").build();

		when(taskService.getTask(100)).thenReturn(task);
		when(taskService.deleteTask(100)).thenReturn(false);
		mockMvc.perform(delete("/v1/deleteTask/{taskId}", "100")).andExpect(status().isInternalServerError());
	}

	@Test
	@SneakyThrows
	public void testDeleteFailed(){
		when(taskService.getTask(100)).thenReturn(null);
		mockMvc.perform(delete("/v1/deleteTask/{taskId}", "100")).andExpect(status().isNotFound());
	}

	@Test
	@SneakyThrows
	public void testDeleteException(){
		when(taskService.getTask(100)).thenThrow(NullPointerException.class);
		mockMvc.perform(delete("/v1/deleteTask/{taskId}", "100")).andExpect(status().isInternalServerError());
	}

	@Test
	@SneakyThrows
	public void testGetAllTasks(){
		List<GetTaskResponse> responseList = new ArrayList<>();
		GetTaskResponse task = GetTaskResponse.builder().
				taskId(100).title("Task Title").
				description("Task Description").status("In Progress").build();
		responseList.add(task);
		when(taskService.getAllTasks()).thenReturn(responseList);

		mockMvc.perform(get("/v1/getAllTasks").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}

	@Test
	@SneakyThrows
	public void testGetAllTasksNoData(){
		when(taskService.getAllTasks()).thenReturn(null);
		mockMvc.perform(get("/v1/getAllTasks").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	@SneakyThrows
	public void testGetAllTasks_Exception(){
		when(taskService.getAllTasks()).thenThrow(new NullPointerException());
		mockMvc.perform(get("/v1/getAllTasks").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError());
	}

	@Test
	@SneakyThrows
	public void testUpdateTask_Exception(){
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
	@SneakyThrows
	public void testUpdateTask_Success(){
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
	@SneakyThrows
	public void testUpdateTask_NotFound(){
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