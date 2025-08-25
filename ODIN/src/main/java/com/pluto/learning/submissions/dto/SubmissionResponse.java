package com.pluto.learning.submissions.dto;

import com.pluto.learning.submissions.LabSubmission;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class SubmissionResponse {
    
    private Long id;
    private Long labId;
    private Long userId;
    private String githubRepoUrl;
    private String commitHash;
    private String branchName;
    private LabSubmission.SubmissionStatus status;
    private Integer automatedScore;
    private Integer manualScore;
    private Integer finalScore;
    private String testResults;
    private String instructorFeedback;
    private Integer attemptNumber;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime submittedAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime evaluatedAt;
    
    private Boolean passed;
    
    // Constructors
    public SubmissionResponse() {}
    
    public SubmissionResponse(Long id, Long labId, Long userId, String githubRepoUrl, String commitHash, 
                             String branchName, LabSubmission.SubmissionStatus status, Integer automatedScore, 
                             Integer manualScore, Integer finalScore, String testResults, String instructorFeedback, 
                             Integer attemptNumber, LocalDateTime submittedAt, LocalDateTime evaluatedAt, Boolean passed) {
        this.id = id;
        this.labId = labId;
        this.userId = userId;
        this.githubRepoUrl = githubRepoUrl;
        this.commitHash = commitHash;
        this.branchName = branchName;
        this.status = status;
        this.automatedScore = automatedScore;
        this.manualScore = manualScore;
        this.finalScore = finalScore;
        this.testResults = testResults;
        this.instructorFeedback = instructorFeedback;
        this.attemptNumber = attemptNumber;
        this.submittedAt = submittedAt;
        this.evaluatedAt = evaluatedAt;
        this.passed = passed;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getLabId() {
        return labId;
    }
    
    public void setLabId(Long labId) {
        this.labId = labId;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
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
    
    public LabSubmission.SubmissionStatus getStatus() {
        return status;
    }
    
    public void setStatus(LabSubmission.SubmissionStatus status) {
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
    
    public Boolean getPassed() {
        return passed;
    }
    
    public void setPassed(Boolean passed) {
        this.passed = passed;
    }
}
