package com.pluto.learning.submissions.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateSubmissionRequest {
    
    @NotBlank(message = "La URL del repositorio de GitHub es requerida")
    @Size(max = 500, message = "La URL no puede exceder 500 caracteres")
    private String githubRepoUrl;
    
    @Size(max = 40, message = "El hash del commit no puede exceder 40 caracteres")
    private String commitHash;
    
    @Size(max = 100, message = "El nombre de la rama no puede exceder 100 caracteres")
    private String branchName = "main";
    
    // Constructors
    public CreateSubmissionRequest() {}
    
    public CreateSubmissionRequest(String githubRepoUrl, String commitHash, String branchName) {
        this.githubRepoUrl = githubRepoUrl;
        this.commitHash = commitHash;
        this.branchName = branchName;
    }
    
    // Getters and Setters
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
}
