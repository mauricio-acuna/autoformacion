package com.pluto.learning.content;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "lessons")
public class Lesson {
    
    @Id
    private String id;
    
    @NotNull
    private Long moduleId;
    
    @NotBlank
    private String title;
    
    @NotBlank
    private String content;
    
    @NotNull
    private Integer orderIndex;
    
    @NotNull
    private Integer estimatedMinutes;
    
    private List<String> objectives;
    
    private List<ResourceLink> resources;
    
    private List<String> codeExamples;
    
    @CreatedDate
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    private LocalDateTime updatedAt;
    
    // Constructors
    public Lesson() {}
    
    public Lesson(Long moduleId, String title, String content, Integer orderIndex, Integer estimatedMinutes) {
        this.moduleId = moduleId;
        this.title = title;
        this.content = content;
        this.orderIndex = orderIndex;
        this.estimatedMinutes = estimatedMinutes;
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public Long getModuleId() { return moduleId; }
    public void setModuleId(Long moduleId) { this.moduleId = moduleId; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    
    public Integer getOrderIndex() { return orderIndex; }
    public void setOrderIndex(Integer orderIndex) { this.orderIndex = orderIndex; }
    
    public Integer getEstimatedMinutes() { return estimatedMinutes; }
    public void setEstimatedMinutes(Integer estimatedMinutes) { this.estimatedMinutes = estimatedMinutes; }
    
    public List<String> getObjectives() { return objectives; }
    public void setObjectives(List<String> objectives) { this.objectives = objectives; }
    
    public List<ResourceLink> getResources() { return resources; }
    public void setResources(List<ResourceLink> resources) { this.resources = resources; }
    
    public List<String> getCodeExamples() { return codeExamples; }
    public void setCodeExamples(List<String> codeExamples) { this.codeExamples = codeExamples; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}

class ResourceLink {
    private String title;
    private String url;
    private String type; // "official", "blog", "video", "documentation"
    
    public ResourceLink() {}
    
    public ResourceLink(String title, String url, String type) {
        this.title = title;
        this.url = url;
        this.type = type;
    }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}
