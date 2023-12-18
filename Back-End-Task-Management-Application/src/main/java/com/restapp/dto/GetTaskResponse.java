package com.restapp.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class GetTaskResponse {

  private Integer taskId;

  private String title;

  private String description;

  private String status;

 
}

