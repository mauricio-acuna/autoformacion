package com.pluto.learning.content;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateSkillRequest {
    
    @NotBlank(message = "Name is required")
    private String name;
    
    @NotBlank(message = "Description is required")
    private String description;
    
    @NotNull(message = "Category is required")
    private SkillCategory category;
    
    @NotNull(message = "Level is required")
    private SkillLevel level;
    
    // Default constructor
    public CreateSkillRequest() {}
    
    // Constructor with parameters
    public CreateSkillRequest(String name, String description, SkillCategory category, SkillLevel level) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.level = level;
    }
    
    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public SkillCategory getCategory() { return category; }
    public void setCategory(SkillCategory category) { this.category = category; }
    
    public SkillLevel getLevel() { return level; }
    public void setLevel(SkillLevel level) { this.level = level; }
}
