package com.example.spring_boot_api_jwt_ad.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String instructor;

    
    public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getInstructor() {
		return instructor;
	}
	public void setInstructor(String instructor) {
		this.instructor = instructor;
	}
	// Các getters và setters
    public Course() {
        // Empty constructor required by JPA
    }
    // Constructor
    public Course(String name, String instructor) {
        this.name = name;
        this.instructor = instructor;
    }

	public Course(Long id, String name, String instructor) {
		this.id = id;
		this.name = name;
		this.instructor = instructor;
	}

	@Override
	public String toString() {
		return "Course [id=" + id + ", name=" + name + ", instructor=" + instructor + "]";
	}
    
    
}
