package com.pluto.learning.scoring.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ActivityRequest {
    
    @NotBlank(message = "El tipo de actividad es requerido")
    private String activityType;
    
    @NotNull(message = "El ID del recurso es requerido")
    private Long resourceId;
    
    @NotBlank(message = "El tipo de recurso es requerido")
    private String resourceType;
    
    private Long skillId;
    
    @Min(value = 0, message = "Los puntos deben ser mayor o igual a 0")
    private Integer pointsEarned = 0;
    
    @Min(value = 0, message = "El tiempo debe ser mayor o igual a 0")
    private Integer timeSpentMinutes;
    
    @Min(value = 0, message = "La puntuación debe ser mayor o igual a 0")
    private Integer scoreAchieved;
    
    private String metadata;
    
    // Constructors
    public ActivityRequest() {}
    
    public ActivityRequest(String activityType, Long resourceId, String resourceType, Long skillId, Integer pointsEarned) {
        this.activityType = activityType;
        this.resourceId = resourceId;
        this.resourceType = resourceType;
        this.skillId = skillId;
        this.pointsEarned = pointsEarned;
    }
    
    // Getters and Setters
    public String getActivityType() {
        return activityType;
    }
    
    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }
    
    public Long getResourceId() {
        return resourceId;
    }
    
    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }
    
    public String getResourceType() {
        return resourceType;
    }
    
    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }
    
    public Long getSkillId() {
        return skillId;
    }
    
    public void setSkillId(Long skillId) {
        this.skillId = skillId;
    }
    
    public Integer getPointsEarned() {
        return pointsEarned;
    }
    
    public void setPointsEarned(Integer pointsEarned) {
        this.pointsEarned = pointsEarned;
    }
    
    public Integer getTimeSpentMinutes() {
        return timeSpentMinutes;
    }
    
    public void setTimeSpentMinutes(Integer timeSpentMinutes) {
        this.timeSpentMinutes = timeSpentMinutes;
    }
    
    public Integer getScoreAchieved() {
        return scoreAchieved;
    }
    
    public void setScoreAchieved(Integer scoreAchieved) {
        this.scoreAchieved = scoreAchieved;
    }
    
    public String getMetadata() {
        return metadata;
    }
    
    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }
}
