package com.restapp.entity;


import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;


@Entity
@Table(name = "Tasks")
@Getter @Setter @EqualsAndHashCode 
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