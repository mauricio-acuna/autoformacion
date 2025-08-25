package com.pluto.learning.progress;

import com.pluto.learning.auth.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "learning_activities")
@EntityListeners(AuditingEntityListener.class)
public class LearningActivity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "activity_type", nullable = false)
    private ActivityType activityType;
    
    @NotNull
    @Column(name = "entity_id", nullable = false)
    private Long entityId;
    
    @NotBlank
    @Column(name = "entity_type", nullable = false, length = 50)
    private String entityType;
    
    @NotBlank
    @Column(name = "activity_name", nullable = false)
    private String activityName;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "points_earned")
    private Integer pointsEarned;
    
    @Column(name = "time_spent_minutes")
    private Integer timeSpentMinutes;
    
    @Column(name = "score_achieved")
    private Integer scoreAchieved;
    
    @Column(name = "resource_id")
    private Long resourceId;
    
    @Column(name = "resource_type", length = 50)
    private String resourceType;
    
    @Column(name = "metadata", columnDefinition = "TEXT")
    private String metadata; // JSON para datos adicionales
    
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    public enum ActivityType {
        LESSON_STARTED,         // Usuario inició una lección
        LESSON_COMPLETED,       // Usuario completó una lección
        QUIZ_STARTED,           // Usuario inició un quiz
        QUIZ_COMPLETED,         // Usuario completó un quiz
        QUIZ_PASSED,            // Usuario aprobó un quiz
        LAB_STARTED,            // Usuario inició un laboratorio
        LAB_SUBMITTED,          // Usuario envió un laboratorio
        LAB_PASSED,             // Usuario aprobó un laboratorio
        MODULE_STARTED,         // Usuario inició un módulo
        MODULE_COMPLETED,       // Usuario completó un módulo
        SKILL_STARTED,          // Usuario inició una habilidad
        SKILL_COMPLETED,        // Usuario completó una habilidad
        SKILL_MASTERED,         // Usuario dominó una habilidad
        LOGIN,                  // Usuario se conectó
        LOGOUT,                 // Usuario se desconectó
        ACHIEVEMENT_EARNED      // Usuario obtuvo un logro
    }
    
    // Constructors
    public LearningActivity() {}
    
    public LearningActivity(User user, ActivityType activityType, Long entityId, String entityType, String activityName) {
        this.user = user;
        this.activityType = activityType;
        this.entityId = entityId;
        this.entityType = entityType;
        this.activityName = activityName;
    }
    
    // Constructor para compatibilidad con ScoringService
    public LearningActivity(User user, String activityType, Long resourceId, String resourceType, Integer scoreAchieved) {
        this.user = user;
        this.activityType = ActivityType.valueOf(activityType);
        this.resourceId = resourceId;
        this.resourceType = resourceType;
        this.scoreAchieved = scoreAchieved;
        this.entityId = resourceId;
        this.entityType = resourceType;
        this.activityName = activityType + " - " + resourceType + " " + resourceId;
    }
    
    // Static factory methods
    public static LearningActivity lessonCompleted(User user, Long lessonId, String lessonTitle, Integer timeSpent) {
        LearningActivity activity = new LearningActivity(user, ActivityType.LESSON_COMPLETED, lessonId, "LESSON", lessonTitle);
        activity.setTimeSpentMinutes(timeSpent);
        activity.setDescription("Completó la lección: " + lessonTitle);
        return activity;
    }
    
    public static LearningActivity quizPassed(User user, Long quizId, String quizTitle, Integer score, Integer points) {
        LearningActivity activity = new LearningActivity(user, ActivityType.QUIZ_PASSED, quizId, "QUIZ", quizTitle);
        activity.setPointsEarned(points);
        activity.setDescription("Aprobó el quiz: " + quizTitle + " con puntuación de " + score + "%");
        return activity;
    }
    
    public static LearningActivity labPassed(User user, Long labId, String labTitle, Integer points) {
        LearningActivity activity = new LearningActivity(user, ActivityType.LAB_PASSED, labId, "LAB", labTitle);
        activity.setPointsEarned(points);
        activity.setDescription("Aprobó el laboratorio: " + labTitle);
        return activity;
    }
    
    public static LearningActivity skillCompleted(User user, Long skillId, String skillName, Integer totalPoints) {
        LearningActivity activity = new LearningActivity(user, ActivityType.SKILL_COMPLETED, skillId, "SKILL", skillName);
        activity.setPointsEarned(totalPoints);
        activity.setDescription("Completó la habilidad: " + skillName);
        return activity;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public ActivityType getActivityType() {
        return activityType;
    }
    
    public void setActivityType(ActivityType activityType) {
        this.activityType = activityType;
    }
    
    public Long getEntityId() {
        return entityId;
    }
    
    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }
    
    public String getEntityType() {
        return entityType;
    }
    
    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }
    
    public String getActivityName() {
        return activityName;
    }
    
    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
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
    
    public String getMetadata() {
        return metadata;
    }
    
    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
