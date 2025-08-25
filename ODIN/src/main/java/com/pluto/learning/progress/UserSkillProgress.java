package com.pluto.learning.progress;

import com.pluto.learning.auth.User;
import com.pluto.learning.content.Skill;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_skill_progress")
@EntityListeners(AuditingEntityListener.class)
public class UserSkillProgress {
    
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
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProgressStatus status = ProgressStatus.NOT_STARTED;
    
    @Min(0)
    @Max(100)
    @Column(name = "completion_percentage", nullable = false)
    private Integer completionPercentage = 0;
    
    @Min(0)
    @Column(name = "total_modules")
    private Integer totalModules = 0;
    
    @Min(0)
    @Column(name = "completed_modules")
    private Integer completedModules = 0;
    
    @Min(0)
    @Column(name = "total_lessons")
    private Integer totalLessons = 0;
    
    @Min(0)
    @Column(name = "completed_lessons")
    private Integer completedLessons = 0;
    
    @Min(0)
    @Column(name = "total_labs")
    private Integer totalLabs = 0;
    
    @Min(0)
    @Column(name = "passed_labs")
    private Integer passedLabs = 0;
    
    @Min(0)
    @Column(name = "total_quizzes")
    private Integer totalQuizzes = 0;
    
    @Min(0)
    @Column(name = "passed_quizzes")
    private Integer passedQuizzes = 0;
    
    @Min(0)
    @Column(name = "total_points_earned")
    private Integer totalPointsEarned = 0;
    
    @Min(0)
    @Column(name = "total_points_possible")
    private Integer totalPointsPossible = 0;
    
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
        NOT_STARTED,     // No ha comenzado
        IN_PROGRESS,     // En progreso
        COMPLETED,       // Completado
        MASTERED         // Dominado (puntuación alta)
    }
    
    // Constructors
    public UserSkillProgress() {}
    
    public UserSkillProgress(User user, Skill skill) {
        this.user = user;
        this.skill = skill;
    }
    
    // Business methods
    public boolean isCompleted() {
        return status == ProgressStatus.COMPLETED || status == ProgressStatus.MASTERED;
    }
    
    public boolean isMastered() {
        return status == ProgressStatus.MASTERED;
    }
    
    public double getScorePercentage() {
        if (totalPointsPossible == 0) return 0.0;
        return (double) totalPointsEarned / totalPointsPossible * 100;
    }
    
    public void updateProgress() {
        // Calcular porcentaje de completación basado en módulos completados
        if (totalModules > 0) {
            this.completionPercentage = (completedModules * 100) / totalModules;
        }
        
        // Actualizar estado basado en progreso
        if (completionPercentage == 0) {
            this.status = ProgressStatus.NOT_STARTED;
        } else if (completionPercentage == 100) {
            // Considerar "mastered" si tiene alta puntuación
            if (getScorePercentage() >= 90) {
                this.status = ProgressStatus.MASTERED;
                this.completedAt = LocalDateTime.now();
            } else {
                this.status = ProgressStatus.COMPLETED;
                this.completedAt = LocalDateTime.now();
            }
        } else {
            this.status = ProgressStatus.IN_PROGRESS;
            if (startedAt == null) {
                this.startedAt = LocalDateTime.now();
            }
        }
        
        this.lastActivityAt = LocalDateTime.now();
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
    
    public ProgressStatus getStatus() {
        return status;
    }
    
    public void setStatus(ProgressStatus status) {
        this.status = status;
    }
    
    public Integer getCompletionPercentage() {
        return completionPercentage;
    }
    
    public void setCompletionPercentage(Integer completionPercentage) {
        this.completionPercentage = completionPercentage;
    }
    
    public Integer getTotalModules() {
        return totalModules;
    }
    
    public void setTotalModules(Integer totalModules) {
        this.totalModules = totalModules;
    }
    
    public Integer getCompletedModules() {
        return completedModules;
    }
    
    public void setCompletedModules(Integer completedModules) {
        this.completedModules = completedModules;
    }
    
    public Integer getTotalLessons() {
        return totalLessons;
    }
    
    public void setTotalLessons(Integer totalLessons) {
        this.totalLessons = totalLessons;
    }
    
    public Integer getCompletedLessons() {
        return completedLessons;
    }
    
    public void setCompletedLessons(Integer completedLessons) {
        this.completedLessons = completedLessons;
    }
    
    public Integer getTotalLabs() {
        return totalLabs;
    }
    
    public void setTotalLabs(Integer totalLabs) {
        this.totalLabs = totalLabs;
    }
    
    public Integer getPassedLabs() {
        return passedLabs;
    }
    
    public void setPassedLabs(Integer passedLabs) {
        this.passedLabs = passedLabs;
    }
    
    public Integer getTotalQuizzes() {
        return totalQuizzes;
    }
    
    public void setTotalQuizzes(Integer totalQuizzes) {
        this.totalQuizzes = totalQuizzes;
    }
    
    public Integer getPassedQuizzes() {
        return passedQuizzes;
    }
    
    public void setPassedQuizzes(Integer passedQuizzes) {
        this.passedQuizzes = passedQuizzes;
    }
    
    public Integer getTotalPointsEarned() {
        return totalPointsEarned;
    }
    
    public void setTotalPointsEarned(Integer totalPointsEarned) {
        this.totalPointsEarned = totalPointsEarned;
    }
    
    public Integer getTotalPointsPossible() {
        return totalPointsPossible;
    }
    
    public void setTotalPointsPossible(Integer totalPointsPossible) {
        this.totalPointsPossible = totalPointsPossible;
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
