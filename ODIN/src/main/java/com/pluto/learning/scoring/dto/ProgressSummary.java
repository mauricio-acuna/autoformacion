package com.pluto.learning.scoring.dto;

import com.pluto.learning.scoring.UserProgress;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.List;

public class ProgressSummary {
    
    private int totalSkillsStarted;
    private int totalSkillsCompleted;
    private int totalSkillsMastered;
    private int totalPointsEarned;
    private int totalTimeSpentHours;
    private double averageCompletionPercentage;
    private List<ProgressDetail> skillProgress;
    
    // Constructor
    public ProgressSummary(int totalSkillsStarted, int totalSkillsCompleted, int totalSkillsMastered,
                          int totalPointsEarned, int totalTimeSpentHours, double averageCompletionPercentage,
                          List<UserProgress> userProgress) {
        this.totalSkillsStarted = totalSkillsStarted;
        this.totalSkillsCompleted = totalSkillsCompleted;
        this.totalSkillsMastered = totalSkillsMastered;
        this.totalPointsEarned = totalPointsEarned;
        this.totalTimeSpentHours = totalTimeSpentHours;
        this.averageCompletionPercentage = averageCompletionPercentage;
        this.skillProgress = userProgress.stream()
            .map(this::mapToProgressDetail)
            .toList();
    }
    
    private ProgressDetail mapToProgressDetail(UserProgress progress) {
        return new ProgressDetail(
            progress.getSkill().getId(),
            progress.getSkill().getName(),
            progress.getSkill().getDescription(),
            progress.getCompletionPercentage(),
            progress.getMasteryScore(),
            progress.getTotalPointsEarned(),
            progress.getTotalTimeSpentHours(),
            progress.getStatus().toString(),
            progress.getCurrentModule() != null ? progress.getCurrentModule().getTitle() : null,
            progress.getStartedAt(),
            progress.getCompletedAt(),
            progress.getLastActivityAt()
        );
    }
    
    // Getters and Setters
    public int getTotalSkillsStarted() {
        return totalSkillsStarted;
    }
    
    public void setTotalSkillsStarted(int totalSkillsStarted) {
        this.totalSkillsStarted = totalSkillsStarted;
    }
    
    public int getTotalSkillsCompleted() {
        return totalSkillsCompleted;
    }
    
    public void setTotalSkillsCompleted(int totalSkillsCompleted) {
        this.totalSkillsCompleted = totalSkillsCompleted;
    }
    
    public int getTotalSkillsMastered() {
        return totalSkillsMastered;
    }
    
    public void setTotalSkillsMastered(int totalSkillsMastered) {
        this.totalSkillsMastered = totalSkillsMastered;
    }
    
    public int getTotalPointsEarned() {
        return totalPointsEarned;
    }
    
    public void setTotalPointsEarned(int totalPointsEarned) {
        this.totalPointsEarned = totalPointsEarned;
    }
    
    public int getTotalTimeSpentHours() {
        return totalTimeSpentHours;
    }
    
    public void setTotalTimeSpentHours(int totalTimeSpentHours) {
        this.totalTimeSpentHours = totalTimeSpentHours;
    }
    
    public double getAverageCompletionPercentage() {
        return averageCompletionPercentage;
    }
    
    public void setAverageCompletionPercentage(double averageCompletionPercentage) {
        this.averageCompletionPercentage = averageCompletionPercentage;
    }
    
    public List<ProgressDetail> getSkillProgress() {
        return skillProgress;
    }
    
    public void setSkillProgress(List<ProgressDetail> skillProgress) {
        this.skillProgress = skillProgress;
    }
    
    // Clase interna para detalles de progreso individual
    public static class ProgressDetail {
        private Long skillId;
        private String skillName;
        private String skillDescription;
        private Integer completionPercentage;
        private Integer masteryScore;
        private Integer totalPointsEarned;
        private Integer totalTimeSpentHours;
        private String status;
        private String currentModule;
        
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private java.time.LocalDateTime startedAt;
        
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private java.time.LocalDateTime completedAt;
        
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private java.time.LocalDateTime lastActivityAt;
        
        public ProgressDetail(Long skillId, String skillName, String skillDescription, Integer completionPercentage,
                             Integer masteryScore, Integer totalPointsEarned, Integer totalTimeSpentHours,
                             String status, String currentModule, java.time.LocalDateTime startedAt,
                             java.time.LocalDateTime completedAt, java.time.LocalDateTime lastActivityAt) {
            this.skillId = skillId;
            this.skillName = skillName;
            this.skillDescription = skillDescription;
            this.completionPercentage = completionPercentage;
            this.masteryScore = masteryScore;
            this.totalPointsEarned = totalPointsEarned;
            this.totalTimeSpentHours = totalTimeSpentHours;
            this.status = status;
            this.currentModule = currentModule;
            this.startedAt = startedAt;
            this.completedAt = completedAt;
            this.lastActivityAt = lastActivityAt;
        }
        
        // Getters and Setters
        public Long getSkillId() { return skillId; }
        public void setSkillId(Long skillId) { this.skillId = skillId; }
        
        public String getSkillName() { return skillName; }
        public void setSkillName(String skillName) { this.skillName = skillName; }
        
        public String getSkillDescription() { return skillDescription; }
        public void setSkillDescription(String skillDescription) { this.skillDescription = skillDescription; }
        
        public Integer getCompletionPercentage() { return completionPercentage; }
        public void setCompletionPercentage(Integer completionPercentage) { this.completionPercentage = completionPercentage; }
        
        public Integer getMasteryScore() { return masteryScore; }
        public void setMasteryScore(Integer masteryScore) { this.masteryScore = masteryScore; }
        
        public Integer getTotalPointsEarned() { return totalPointsEarned; }
        public void setTotalPointsEarned(Integer totalPointsEarned) { this.totalPointsEarned = totalPointsEarned; }
        
        public Integer getTotalTimeSpentHours() { return totalTimeSpentHours; }
        public void setTotalTimeSpentHours(Integer totalTimeSpentHours) { this.totalTimeSpentHours = totalTimeSpentHours; }
        
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        
        public String getCurrentModule() { return currentModule; }
        public void setCurrentModule(String currentModule) { this.currentModule = currentModule; }
        
        public java.time.LocalDateTime getStartedAt() { return startedAt; }
        public void setStartedAt(java.time.LocalDateTime startedAt) { this.startedAt = startedAt; }
        
        public java.time.LocalDateTime getCompletedAt() { return completedAt; }
        public void setCompletedAt(java.time.LocalDateTime completedAt) { this.completedAt = completedAt; }
        
        public java.time.LocalDateTime getLastActivityAt() { return lastActivityAt; }
        public void setLastActivityAt(java.time.LocalDateTime lastActivityAt) { this.lastActivityAt = lastActivityAt; }
    }
}
