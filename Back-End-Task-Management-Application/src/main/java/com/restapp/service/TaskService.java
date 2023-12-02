package com.restapp.service;

import java.util.List;

import com.rest.GetTaskResponse;
import com.rest.SaveTaskRequest;
import com.rest.UpdateTaskRequest;
import com.restapp.entity.Task;

public interface TaskService 
{
	boolean saveTask(SaveTaskRequest saveRequest) throws Exception;

	GetTaskResponse getTask(Integer TaskId)throws Exception;

	boolean deleteTask(Integer TaskId)throws Exception;

	boolean updateTask(UpdateTaskRequest updateRequest)throws Exception;
	
	List<GetTaskResponse> getAllTasks() throws Exception;
	
	boolean findByTitle(String title) throws Exception;
	
	Task findByTitleTask(String title) throws Exception;
}
