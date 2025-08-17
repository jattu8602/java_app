package com.example.myapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;

public class TodoRequest {

    @NotBlank(message = "Title is required")
    @Size(min = 1, max = 100, message = "Title must be between 1 and 100 characters")
    private String title;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    @Min(value = 1, message = "Priority must be between 1 and 3")
    @Max(value = 3, message = "Priority must be between 1 and 3")
    private Integer priority;

    // Default constructor
    public TodoRequest() {}

    // Constructor with fields
    public TodoRequest(String title, String description, Integer priority) {
        this.title = title;
        this.description = description;
        this.priority = priority != null ? priority : 1;
    }

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPriority() {
        return priority != null ? priority : 1;
    }

    public void setPriority(Integer priority) {
        this.priority = priority != null ? priority : 1;
    }
}
