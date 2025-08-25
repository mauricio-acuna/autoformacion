package com.pluto.learning.career;

import com.pluto.learning.auth.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad para gestión de respuestas a preguntas de entrevista
 */
@Entity
@Table(name = "interview_responses")
public class InterviewResponse {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private InterviewQuestion question;
    
    @Column(name = "response_text", columnDefinition = "TEXT")
    private String responseText;
    
    @Column(name = "response_time_seconds")
    private Integer responseTimeSeconds;
    
    @Column(name = "ai_feedback", columnDefinition = "TEXT")
    private String aiFeedback;
    
    @Column(name = "ai_score")
    private Integer aiScore; // 0-100
    
    @Column(name = "practice_session_id")
    private String practiceSessionId; // UUID para agrupar respuestas de una sesión
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    // Constructors
    public InterviewResponse() {
        this.createdAt = LocalDateTime.now();
    }
    
    public InterviewResponse(User user, InterviewQuestion question, String responseText) {
        this();
        this.user = user;
        this.question = question;
        this.responseText = responseText;
    }
    
    // Business methods
    public boolean isWithinTimeLimit() {
        if (question.getTimeLimitSeconds() == null || responseTimeSeconds == null) {
            return true;
        }
        return responseTimeSeconds <= question.getTimeLimitSeconds();
    }
    
    public String getPerformanceLevel() {
        if (aiScore == null) return "Not evaluated";
        
        if (aiScore >= 90) return "Excellent";
        if (aiScore >= 80) return "Good";
        if (aiScore >= 70) return "Satisfactory";
        if (aiScore >= 60) return "Needs Improvement";
        return "Poor";
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    
    public InterviewQuestion getQuestion() { return question; }
    public void setQuestion(InterviewQuestion question) { this.question = question; }
    
    public String getResponseText() { return responseText; }
    public void setResponseText(String responseText) { this.responseText = responseText; }
    
    public Integer getResponseTimeSeconds() { return responseTimeSeconds; }
    public void setResponseTimeSeconds(Integer responseTimeSeconds) { this.responseTimeSeconds = responseTimeSeconds; }
    
    public String getAiFeedback() { return aiFeedback; }
    public void setAiFeedback(String aiFeedback) { this.aiFeedback = aiFeedback; }
    
    public Integer getAiScore() { return aiScore; }
    public void setAiScore(Integer aiScore) { this.aiScore = aiScore; }
    
    public String getPracticeSessionId() { return practiceSessionId; }
    public void setPracticeSessionId(String practiceSessionId) { this.practiceSessionId = practiceSessionId; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
