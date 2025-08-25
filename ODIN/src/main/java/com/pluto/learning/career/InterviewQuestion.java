package com.pluto.learning.career;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad para gestión de preguntas y respuestas de práctica de entrevistas
 */
@Entity
@Table(name = "interview_questions")
public class InterviewQuestion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "question_text", nullable = false, columnDefinition = "TEXT")
    private String questionText;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private QuestionCategory category;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "difficulty_level", nullable = false)
    private DifficultyLevel difficultyLevel;
    
    @Column(name = "suggested_answer", columnDefinition = "TEXT")
    private String suggestedAnswer;
    
    @Column(name = "time_limit_seconds")
    private Integer timeLimitSeconds;
    
    @Column(name = "tags", length = 500)
    private String tags; // Comma-separated tags
    
    @Column(name = "active", nullable = false)
    private Boolean active = true;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    public enum QuestionCategory {
        BEHAVIORAL,          // Preguntas de comportamiento
        TECHNICAL,           // Preguntas técnicas
        SYSTEM_DESIGN,       // Diseño de sistemas
        CODING_CHALLENGE,    // Desafíos de programación
        CULTURAL_FIT,        // Ajuste cultural
        SITUATIONAL,         // Preguntas situacionales
        LEADERSHIP,          // Liderazgo
        PROBLEM_SOLVING      // Resolución de problemas
    }
    
    public enum DifficultyLevel {
        JUNIOR,      // Nivel junior
        MID,         // Nivel intermedio
        SENIOR,      // Nivel senior
        LEAD,        // Nivel líder
        PRINCIPAL    // Nivel principal/arquitecto
    }
    
    // Constructors
    public InterviewQuestion() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public InterviewQuestion(String questionText, QuestionCategory category, DifficultyLevel difficultyLevel) {
        this();
        this.questionText = questionText;
        this.category = category;
        this.difficultyLevel = difficultyLevel;
    }
    
    // Business methods
    public boolean isTimedQuestion() {
        return timeLimitSeconds != null && timeLimitSeconds > 0;
    }
    
    public String getFormattedTimeLimit() {
        if (timeLimitSeconds == null) return "No time limit";
        
        int minutes = timeLimitSeconds / 60;
        int seconds = timeLimitSeconds % 60;
        
        if (minutes > 0) {
            return String.format("%d min %d sec", minutes, seconds);
        } else {
            return String.format("%d sec", seconds);
        }
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getQuestionText() { return questionText; }
    public void setQuestionText(String questionText) { this.questionText = questionText; }
    
    public QuestionCategory getCategory() { return category; }
    public void setCategory(QuestionCategory category) { this.category = category; }
    
    public DifficultyLevel getDifficultyLevel() { return difficultyLevel; }
    public void setDifficultyLevel(DifficultyLevel difficultyLevel) { this.difficultyLevel = difficultyLevel; }
    
    public String getSuggestedAnswer() { return suggestedAnswer; }
    public void setSuggestedAnswer(String suggestedAnswer) { this.suggestedAnswer = suggestedAnswer; }
    
    public Integer getTimeLimitSeconds() { return timeLimitSeconds; }
    public void setTimeLimitSeconds(Integer timeLimitSeconds) { this.timeLimitSeconds = timeLimitSeconds; }
    
    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }
    
    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
