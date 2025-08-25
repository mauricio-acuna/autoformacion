package com.pluto.learning.community;

import com.pluto.learning.auth.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad para gestión de foros de discusión
 */
@Entity
@Table(name = "discussion_forums")
public class DiscussionForum {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "name", nullable = false, length = 200)
    private String name;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private ForumCategory category;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "moderator_id")
    private User moderator;
    
    @Column(name = "is_active")
    private Boolean isActive = true;
    
    @Column(name = "participant_count")
    private Integer participantCount = 0;
    
    @Column(name = "post_count")
    private Integer postCount = 0;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Categorías de foros
    public enum ForumCategory {
        GENERAL_DISCUSSION,
        TECHNICAL_HELP,
        PROJECT_SHOWCASE,
        CAREER_ADVICE,
        STUDY_GROUPS,
        PROGRAMMING_LANGUAGES,
        FRAMEWORKS_LIBRARIES,
        ALGORITHMS_DATA_STRUCTURES,
        SYSTEM_DESIGN,
        CODE_REVIEW,
        OPEN_SOURCE,
        INDUSTRY_NEWS,
        INTERVIEW_PREP,
        FREELANCING,
        REMOTE_WORK
    }
    
    // Constructors
    public DiscussionForum() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public DiscussionForum(String name, String description, ForumCategory category) {
        this();
        this.name = name;
        this.description = description;
        this.category = category;
    }
    
    // Business methods
    public void incrementPostCount() {
        this.postCount = (this.postCount == null ? 0 : this.postCount) + 1;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void decrementPostCount() {
        this.postCount = Math.max(0, (this.postCount == null ? 0 : this.postCount) - 1);
        this.updatedAt = LocalDateTime.now();
    }
    
    public void incrementParticipantCount() {
        this.participantCount = (this.participantCount == null ? 0 : this.participantCount) + 1;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void decrementParticipantCount() {
        this.participantCount = Math.max(0, (this.participantCount == null ? 0 : this.participantCount) - 1);
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getActivityLevel() {
        if (postCount == null || postCount == 0) return "Inactive";
        if (postCount >= 100) return "Very Active";
        if (postCount >= 50) return "Active";
        if (postCount >= 10) return "Moderate";
        return "Low Activity";
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public ForumCategory getCategory() { return category; }
    public void setCategory(ForumCategory category) { this.category = category; }
    
    public User getModerator() { return moderator; }
    public void setModerator(User moderator) { this.moderator = moderator; }
    
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    
    public Integer getParticipantCount() { return participantCount; }
    public void setParticipantCount(Integer participantCount) { this.participantCount = participantCount; }
    
    public Integer getPostCount() { return postCount; }
    public void setPostCount(Integer postCount) { this.postCount = postCount; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
