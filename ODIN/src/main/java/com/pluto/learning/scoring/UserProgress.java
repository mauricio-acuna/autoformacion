package com.pluto.learning.scoring;

import com.pluto.learning.auth.User;
import com.pluto.learning.content.Skill;
import com.pluto.learning.content.Module;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_progress")
@EntityListeners(AuditingEntityListener.class)
public class UserProgress {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "skill_id", nullable = false)
    private Skill skill;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "current_module_id")
    private Module currentModule;
    
    @Min(0)
    @Max(100)
    @Column(name = "completion_percentage", nullable = false)
    private Integer completionPercentage = 0;
    
    @Min(0)
    @Max(100)
    @Column(name = "mastery_score", nullable = false)
    private Integer masteryScore = 0;
    
    @Min(0)
    @Column(name = "total_points_earned", nullable = false)
    private Integer totalPointsEarned = 0;
    
    @Min(0)
    @Column(name = "total_time_spent_hours", nullable = false)
    private Integer totalTimeSpentHours = 0;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ProgressStatus status = ProgressStatus.NOT_STARTED;
    
    @Column(name = "started_at")
    private LocalDateTime startedAt;
    
    @Column(name = "completed_at")
    private LocalDateTime completedAt;
    
    @Column(name = "last_activity_at")
    private LocalDateTime lastActivityAt;
    
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    public enum ProgressStatus {
        NOT_STARTED,    // No ha comenzado
        IN_PROGRESS,    // En progreso
        COMPLETED,      // Completado (100%)
        MASTERED        // Dominado (score alto + tiempo)
    }
    
    // Constructors
    public UserProgress() {}
    
    public UserProgress(User user, Skill skill) {
        this.user = user;
        this.skill = skill;
        this.lastActivityAt = LocalDateTime.now();
    }
    
    // Business methods
    public boolean isCompleted() {
        return completionPercentage >= 100 && status == ProgressStatus.COMPLETED;
    }
    
    public boolean isMastered() {
        return masteryScore >= 85 && completionPercentage >= 100;
    }
    
    public void updateProgress(int newCompletionPercentage, int newMasteryScore, int additionalPoints, int additionalHours) {
        this.completionPercentage = Math.max(this.completionPercentage, newCompletionPercentage);
        this.masteryScore = Math.max(this.masteryScore, newMasteryScore);
        this.totalPointsEarned += additionalPoints;
        this.totalTimeSpentHours += additionalHours;
        this.lastActivityAt = LocalDateTime.now();
        
        // Update status based on progress
        if (this.completionPercentage >= 100) {
            if (this.masteryScore >= 85) {
                this.status = ProgressStatus.MASTERED;
            } else {
                this.status = ProgressStatus.COMPLETED;
            }
            this.completedAt = LocalDateTime.now();
        } else if (this.completionPercentage > 0) {
            this.status = ProgressStatus.IN_PROGRESS;
            if (this.startedAt == null) {
                this.startedAt = LocalDateTime.now();
            }
        }
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
    
    public Skill getSkill() {
        return skill;
    }
    
    public void setSkill(Skill skill) {
        this.skill = skill;
    }
    
    public Module getCurrentModule() {
        return currentModule;
    }
    
    public void setCurrentModule(Module currentModule) {
        this.currentModule = currentModule;
    }
    
    public Integer getCompletionPercentage() {
        return completionPercentage;
    }
    
    public void setCompletionPercentage(Integer completionPercentage) {
        this.completionPercentage = completionPercentage;
    }
    
    public Integer getMasteryScore() {
        return masteryScore;
    }
    
    public void setMasteryScore(Integer masteryScore) {
        this.masteryScore = masteryScore;
    }
    
    public Integer getTotalPointsEarned() {
        return totalPointsEarned;
    }
    
    public void setTotalPointsEarned(Integer totalPointsEarned) {
        this.totalPointsEarned = totalPointsEarned;
    }
    
    public Integer getTotalTimeSpentHours() {
        return totalTimeSpentHours;
    }
    
    public void setTotalTimeSpentHours(Integer totalTimeSpentHours) {
        this.totalTimeSpentHours = totalTimeSpentHours;
    }
    
    public ProgressStatus getStatus() {
        return status;
    }
    
    public void setStatus(ProgressStatus status) {
        this.status = status;
    }
    
    public LocalDateTime getStartedAt() {
        return startedAt;
    }
    
    public void setStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }
    
    public LocalDateTime getCompletedAt() {
        return completedAt;
    }
    
    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }
    
    public LocalDateTime getLastActivityAt() {
        return lastActivityAt;
    }
    
    public void setLastActivityAt(LocalDateTime lastActivityAt) {
        this.lastActivityAt = lastActivityAt;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
