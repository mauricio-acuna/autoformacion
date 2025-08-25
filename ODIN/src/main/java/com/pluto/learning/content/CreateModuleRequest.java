package com.pluto.learning.content;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;

public class CreateModuleRequest {
    
    @NotBlank(message = "Title is required")
    private String title;
    
    @NotBlank(message = "Description is required")
    private String description;
    
    @NotNull(message = "Duration in weeks is required")
    @Positive(message = "Duration must be positive")
    private Integer durationWeeks;
    
    @NotNull(message = "Skill ID is required")
    private Long skillId;
    
    private List<Long> prerequisiteIds;
    
    // Default constructor
    public CreateModuleRequest() {}
    
    // Getters and setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public Integer getDurationWeeks() { return durationWeeks; }
    public void setDurationWeeks(Integer durationWeeks) { this.durationWeeks = durationWeeks; }
    
    public Long getSkillId() { return skillId; }
    public void setSkillId(Long skillId) { this.skillId = skillId; }
    
    public List<Long> getPrerequisiteIds() { return prerequisiteIds; }
    public void setPrerequisiteIds(List<Long> prerequisiteIds) { this.prerequisiteIds = prerequisiteIds; }
}
