package com.restapp.service;



import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.restapp.dao.TaskRepository;
import com.restapp.dto.GetTaskResponse;
import com.restapp.dto.SaveTaskRequest;
import com.restapp.dto.UpdateTaskRequest;
import com.restapp.entity.Task;


@Service
public class TaskServiceImpl implements TaskService {
	
	@Autowired
	TaskRepository taskRepository;
	
	@Override
	@Transactional
	public int saveTask(SaveTaskRequest saveRequest) throws Exception {
		Task task = new Task();
		task.setTitle(saveRequest.getTitle());
		task.setDescription(saveRequest.getDescription());
		task.setStatus(saveRequest.getStatus());
		Task savedTask = taskRepository.save(task);
		return savedTask.getTaskId();
	}

	@Override
	@Transactional
	public GetTaskResponse getTask(Integer taskId) throws Exception{
		GetTaskResponse getResponse = null;
		Optional<Task> result = taskRepository.findById(taskId.longValue());
		if (result.isPresent()) {
		Task task = result.get();
		getResponse = GetTaskResponse.builder().
				taskId(task.getTaskId()).title(task.getTitle()).
				description(task.getDescription()).status(task.getStatus())
				.build();
		}
		return getResponse;
	}

	@Override
	@Transactional
	public boolean deleteTask(Integer TaskId) throws Exception{
		taskRepository.deleteById(TaskId.longValue());
		return true;
	}

	@Override
	@Transactional
	public boolean updateTask(UpdateTaskRequest updateRequest) throws Exception {
		Optional<Task> result = taskRepository.findById(updateRequest.getTaskId().longValue());
		if (result.isPresent()) {
			Task task = result.get();
			task.setTitle(updateRequest.getTitle());
			task.setDescription(updateRequest.getDescription());
			task.setStatus(updateRequest.getStatus());
			taskRepository.save(task);
			return true;
		}
		return false;
	}

	@Override
	@Transactional(readOnly = true)
	public List<GetTaskResponse> getAllTasks() throws Exception {
		List<Task> empList = taskRepository.findAll();

		 return empList.stream().map(task -> GetTaskResponse.builder().
					taskId(task.getTaskId()).title(task.getTitle()).
					description(task.getDescription()).status(task.getStatus())
					.build()).toList();
	}
	
	@Override
	@Transactional(readOnly = true)
	public Task findByTitle(String title) throws Exception {
		Optional<Task> task = taskRepository.findByTitle(title);
		if(task.isPresent()) {
    		return task.get();
    	}
		return null;
	}

}
