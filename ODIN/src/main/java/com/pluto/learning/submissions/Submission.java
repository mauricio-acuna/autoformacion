package com.pluto.learning.submissions;

import com.pluto.learning.auth.User;
import com.pluto.learning.assessments.Lab;
import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad para submissions de laboratorios y código
 */
@Entity
@Table(name = "submissions")
public class Submission {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lab_id", nullable = false)
    private Lab lab;
    
    @Column(name = "code_content", columnDefinition = "TEXT")
    private String codeContent;
    
    @Column(name = "github_url")
    private String githubUrl;
    
    @Column(name = "live_demo_url")
    private String liveDemoUrl;
    
    @Column(name = "submitted_at", nullable = false)
    private LocalDateTime submittedAt;
    
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private SubmissionStatus status;
    
    @Column(name = "score")
    private Integer score;
    
    @Column(name = "feedback", columnDefinition = "TEXT")
    private String feedback;
    
    @Column(name = "graded_at")
    private LocalDateTime gradedAt;
    
    @Column(name = "graded_by")
    private String gradedBy; // 'AUTO' for automated grading
    
    // Constructors
    public Submission() {}
    
    public Submission(User user, Lab lab, String codeContent) {
        this.user = user;
        this.lab = lab;
        this.codeContent = codeContent;
        this.submittedAt = LocalDateTime.now();
        this.status = SubmissionStatus.PENDING;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    
    public Lab getLab() { return lab; }
    public void setLab(Lab lab) { this.lab = lab; }
    
    public String getCodeContent() { return codeContent; }
    public void setCodeContent(String codeContent) { this.codeContent = codeContent; }
    
    public String getGithubUrl() { return githubUrl; }
    public void setGithubUrl(String githubUrl) { this.githubUrl = githubUrl; }
    
    public String getLiveDemoUrl() { return liveDemoUrl; }
    public void setLiveDemoUrl(String liveDemoUrl) { this.liveDemoUrl = liveDemoUrl; }
    
    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; }
    
    public SubmissionStatus getStatus() { return status; }
    public void setStatus(SubmissionStatus status) { this.status = status; }
    
    public Integer getScore() { return score; }
    public void setScore(Integer score) { this.score = score; }
    
    public String getFeedback() { return feedback; }
    public void setFeedback(String feedback) { this.feedback = feedback; }
    
    public LocalDateTime getGradedAt() { return gradedAt; }
    public void setGradedAt(LocalDateTime gradedAt) { this.gradedAt = gradedAt; }
    
    public String getGradedBy() { return gradedBy; }
    public void setGradedBy(String gradedBy) { this.gradedBy = gradedBy; }
}
