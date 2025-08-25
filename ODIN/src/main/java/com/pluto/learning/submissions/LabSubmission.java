package com.pluto.learning.submissions;

import com.pluto.learning.assessments.Lab;
import com.pluto.learning.auth.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "lab_submissions")
@EntityListeners(AuditingEntityListener.class)
public class LabSubmission {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lab_id", nullable = false)
    private Lab lab;
    
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @NotBlank
    @Column(name = "github_repo_url", nullable = false, length = 500)
    private String githubRepoUrl;
    
    @Column(name = "commit_hash", length = 40)
    private String commitHash;
    
    @Column(name = "branch_name", length = 100)
    private String branchName = "main";
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubmissionStatus status = SubmissionStatus.SUBMITTED;
    
    @Min(0)
    @Max(100)
    @Column(name = "automated_score")
    private Integer automatedScore;
    
    @Min(0)
    @Max(100)
    @Column(name = "manual_score")
    private Integer manualScore;
    
    @Min(0)
    @Max(100)
    @Column(name = "final_score")
    private Integer finalScore;
    
    @Column(name = "test_results", columnDefinition = "TEXT")
    private String testResults;
    
    @Column(name = "instructor_feedback", columnDefinition = "TEXT")
    private String instructorFeedback;
    
    @Column(name = "execution_logs", columnDefinition = "TEXT")
    private String executionLogs;
    
    @Min(1)
    @Column(name = "attempt_number", nullable = false)
    private Integer attemptNumber = 1;
    
    @Column(name = "submitted_at", nullable = false)
    private LocalDateTime submittedAt;
    
    @Column(name = "evaluated_at")
    private LocalDateTime evaluatedAt;
    
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    public enum SubmissionStatus {
        SUBMITTED,      // Enviado, pendiente de evaluación
        EVALUATING,     // En proceso de evaluación
        EVALUATED,      // Evaluación automática completada
        REVIEW_PENDING, // Esperando revisión manual
        REVIEWED,       // Revisión manual completada
        APPROVED,       // Aprobado
        REJECTED        // Rechazado
    }
    
    // Constructors
    public LabSubmission() {}
    
    public LabSubmission(Lab lab, User user, String githubRepoUrl) {
        this.lab = lab;
        this.user = user;
        this.githubRepoUrl = githubRepoUrl;
        this.submittedAt = LocalDateTime.now();
    }
    
    // Business methods
    public boolean isPassed() {
        if (finalScore == null) return false;
        return finalScore >= 70; // 70% passing threshold
    }
    
    public boolean needsManualReview() {
        return status == SubmissionStatus.REVIEW_PENDING || 
               lab.getEvaluationType() == Lab.EvaluationType.MANUAL ||
               lab.getEvaluationType() == Lab.EvaluationType.HYBRID;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Lab getLab() {
        return lab;
    }
    
    public void setLab(Lab lab) {
        this.lab = lab;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public String getGithubRepoUrl() {
        return githubRepoUrl;
    }
    
    public void setGithubRepoUrl(String githubRepoUrl) {
        this.githubRepoUrl = githubRepoUrl;
    }
    
    public String getCommitHash() {
        return commitHash;
    }
    
    public void setCommitHash(String commitHash) {
        this.commitHash = commitHash;
    }
    
    public String getBranchName() {
        return branchName;
    }
    
    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }
    
    public SubmissionStatus getStatus() {
        return status;
    }
    
    public void setStatus(SubmissionStatus status) {
        this.status = status;
    }
    
    public Integer getAutomatedScore() {
        return automatedScore;
    }
    
    public void setAutomatedScore(Integer automatedScore) {
        this.automatedScore = automatedScore;
    }
    
    public Integer getManualScore() {
        return manualScore;
    }
    
    public void setManualScore(Integer manualScore) {
        this.manualScore = manualScore;
    }
    
    public Integer getFinalScore() {
        return finalScore;
    }
    
    public void setFinalScore(Integer finalScore) {
        this.finalScore = finalScore;
    }
    
    public String getTestResults() {
        return testResults;
    }
    
    public void setTestResults(String testResults) {
        this.testResults = testResults;
    }
    
    public String getInstructorFeedback() {
        return instructorFeedback;
    }
    
    public void setInstructorFeedback(String instructorFeedback) {
        this.instructorFeedback = instructorFeedback;
    }
    
    public String getExecutionLogs() {
        return executionLogs;
    }
    
    public void setExecutionLogs(String executionLogs) {
        this.executionLogs = executionLogs;
    }
    
    public Integer getAttemptNumber() {
        return attemptNumber;
    }
    
    public void setAttemptNumber(Integer attemptNumber) {
        this.attemptNumber = attemptNumber;
    }
    
    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }
    
    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }
    
    public LocalDateTime getEvaluatedAt() {
        return evaluatedAt;
    }
    
    public void setEvaluatedAt(LocalDateTime evaluatedAt) {
        this.evaluatedAt = evaluatedAt;
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
