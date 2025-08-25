package com.pluto.learning.assessments;

import com.pluto.learning.content.Module;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "labs")
@EntityListeners(AuditingEntityListener.class)
public class Lab {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    @Column(nullable = false, length = 200)
    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "module_id", nullable = false)
    private Module module;
    
    @NotBlank
    @Column(name = "github_template_url", nullable = false, length = 500)
    private String githubTemplateUrl;
    
    @Column(name = "starter_code", columnDefinition = "TEXT")
    private String starterCode;
    
    @Column(name = "instructions", columnDefinition = "TEXT", nullable = false)
    private String instructions;
    
    @Min(1)
    @Column(name = "max_attempts", nullable = false)
    private Integer maxAttempts = 5;
    
    @Min(1)
    @Column(name = "time_limit_hours")
    private Integer timeLimitHours;
    
    @Min(0)
    @Column(name = "points", nullable = false)
    private Integer points = 100;
    
    @Column(nullable = false)
    private Boolean active = true;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "evaluation_type", nullable = false)
    private EvaluationType evaluationType = EvaluationType.AUTOMATED;
    
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    public enum EvaluationType {
        AUTOMATED,    // Evaluación automática con tests
        MANUAL,       // Evaluación manual por instructor
        HYBRID        // Combinación de ambas
    }
    
    // Constructors
    public Lab() {}
    
    public Lab(String title, String description, Module module, String githubTemplateUrl, String instructions) {
        this.title = title;
        this.description = description;
        this.module = module;
        this.githubTemplateUrl = githubTemplateUrl;
        this.instructions = instructions;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
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
    
    public Module getModule() {
        return module;
    }
    
    public void setModule(Module module) {
        this.module = module;
    }
    
    public String getGithubTemplateUrl() {
        return githubTemplateUrl;
    }
    
    public void setGithubTemplateUrl(String githubTemplateUrl) {
        this.githubTemplateUrl = githubTemplateUrl;
    }
    
    public String getStarterCode() {
        return starterCode;
    }
    
    public void setStarterCode(String starterCode) {
        this.starterCode = starterCode;
    }
    
    public String getInstructions() {
        return instructions;
    }
    
    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }
    
    public Integer getMaxAttempts() {
        return maxAttempts;
    }
    
    public void setMaxAttempts(Integer maxAttempts) {
        this.maxAttempts = maxAttempts;
    }
    
    public Integer getTimeLimitHours() {
        return timeLimitHours;
    }
    
    public void setTimeLimitHours(Integer timeLimitHours) {
        this.timeLimitHours = timeLimitHours;
    }
    
    public Integer getPoints() {
        return points;
    }
    
    public void setPoints(Integer points) {
        this.points = points;
    }
    
    public Boolean getActive() {
        return active;
    }
    
    public void setActive(Boolean active) {
        this.active = active;
    }
    
    public EvaluationType getEvaluationType() {
        return evaluationType;
    }
    
    public void setEvaluationType(EvaluationType evaluationType) {
        this.evaluationType = evaluationType;
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
