package com.restapp.entity;


import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "Tasks")
@Getter @Setter
public class Task implements Serializable {


	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "TaskId")
	private Integer taskId;
	
	@Column(name = "Title", length = 20)	
	private String title;
	
	@Column(name = "Description")	
	private String description;
	
	@Column(name = "Status")	
	private String status;


}