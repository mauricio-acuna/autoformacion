package com.pluto.learning.community;

import com.pluto.learning.auth.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad para gestión de grupos de estudio colaborativo
 */
@Entity
@Table(name = "study_groups")
public class StudyGroup {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "name", nullable = false, length = 200)
    private String name;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;
    
    @Column(name = "course_name")
    private String courseName; // Nombre del curso asociado
    
    @Enumerated(EnumType.STRING)
    @Column(name = "study_focus")
    private StudyFocus studyFocus;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "difficulty_level")
    private DifficultyLevel difficultyLevel;
    
    @Column(name = "max_members")
    private Integer maxMembers = 20;
    
    @Column(name = "current_members")
    private Integer currentMembers = 1; // Creator es el primer miembro
    
    @Column(name = "is_private")
    private Boolean isPrivate = false;
    
    @Column(name = "is_active")
    private Boolean isActive = true;
    
    @Column(name = "meeting_schedule")
    private String meetingSchedule; // e.g., "Martes y Jueves 8PM"
    
    @Column(name = "timezone")
    private String timezone;
    
    @Column(name = "video_call_link")
    private String videoCallLink;
    
    @Column(name = "chat_channel_id")
    private String chatChannelId; // Para integración con sistemas de chat
    
    @Column(name = "tags")
    private String tags; // Comma-separated tags
    
    @Column(name = "study_goals", columnDefinition = "TEXT")
    private String studyGoals;
    
    @Column(name = "current_topic")
    private String currentTopic;
    
    @Column(name = "next_session_date")
    private LocalDateTime nextSessionDate;
    
    @Column(name = "total_sessions")
    private Integer totalSessions = 0;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Enums
    public enum StudyFocus {
        PROGRAMMING_FUNDAMENTALS,
        WEB_DEVELOPMENT,
        DATA_STRUCTURES_ALGORITHMS,
        SYSTEM_DESIGN,
        DATABASE_DESIGN,
        MOBILE_DEVELOPMENT,
        MACHINE_LEARNING,
        CYBERSECURITY,
        DEVOPS,
        INTERVIEW_PREPARATION,
        CERTIFICATION_PREP,
        PROJECT_COLLABORATION,
        CODE_REVIEW,
        COMPETITIVE_PROGRAMMING,
        OPEN_SOURCE_CONTRIBUTION
    }
    
    public enum DifficultyLevel {
        BEGINNER,
        INTERMEDIATE,
        ADVANCED,
        EXPERT,
        MIXED_LEVELS
    }
    
    // Constructors
    public StudyGroup() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public StudyGroup(String name, String description, User creator, StudyFocus studyFocus) {
        this();
        this.name = name;
        this.description = description;
        this.creator = creator;
        this.studyFocus = studyFocus;
    }
    
    // Business methods
    public boolean canJoin() {
        return isActive && currentMembers < maxMembers && !isPrivate;
    }
    
    public boolean isFull() {
        return currentMembers >= maxMembers;
    }
    
    public void incrementMemberCount() {
        if (currentMembers < maxMembers) {
            this.currentMembers++;
            this.updatedAt = LocalDateTime.now();
        }
    }
    
    public void decrementMemberCount() {
        if (currentMembers > 1) { // Creator siempre permanece
            this.currentMembers--;
            this.updatedAt = LocalDateTime.now();
        }
    }
    
    public void incrementSessionCount() {
        this.totalSessions++;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getGroupStatus() {
        if (!isActive) return "Inactive";
        if (isFull()) return "Full";
        if (currentMembers <= 2) return "Looking for Members";
        if (currentMembers >= maxMembers * 0.8) return "Almost Full";
        return "Active";
    }
    
    public String getActivityLevel() {
        if (totalSessions == null || totalSessions == 0) return "New Group";
        if (totalSessions >= 50) return "Very Active";
        if (totalSessions >= 20) return "Active";
        if (totalSessions >= 5) return "Moderate";
        return "Getting Started";
    }
    
    public Double getMembershipProgress() {
        return (double) currentMembers / maxMembers;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public User getCreator() { return creator; }
    public void setCreator(User creator) { this.creator = creator; }
    
    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }
    
    public StudyFocus getStudyFocus() { return studyFocus; }
    public void setStudyFocus(StudyFocus studyFocus) { this.studyFocus = studyFocus; }
    
    public DifficultyLevel getDifficultyLevel() { return difficultyLevel; }
    public void setDifficultyLevel(DifficultyLevel difficultyLevel) { this.difficultyLevel = difficultyLevel; }
    
    public Integer getMaxMembers() { return maxMembers; }
    public void setMaxMembers(Integer maxMembers) { this.maxMembers = maxMembers; }
    
    public Integer getCurrentMembers() { return currentMembers; }
    public void setCurrentMembers(Integer currentMembers) { this.currentMembers = currentMembers; }
    
    public Boolean getIsPrivate() { return isPrivate; }
    public void setIsPrivate(Boolean isPrivate) { this.isPrivate = isPrivate; }
    
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    
    public String getMeetingSchedule() { return meetingSchedule; }
    public void setMeetingSchedule(String meetingSchedule) { this.meetingSchedule = meetingSchedule; }
    
    public String getTimezone() { return timezone; }
    public void setTimezone(String timezone) { this.timezone = timezone; }
    
    public String getVideoCallLink() { return videoCallLink; }
    public void setVideoCallLink(String videoCallLink) { this.videoCallLink = videoCallLink; }
    
    public String getChatChannelId() { return chatChannelId; }
    public void setChatChannelId(String chatChannelId) { this.chatChannelId = chatChannelId; }
    
    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }
    
    public String getStudyGoals() { return studyGoals; }
    public void setStudyGoals(String studyGoals) { this.studyGoals = studyGoals; }
    
    public String getCurrentTopic() { return currentTopic; }
    public void setCurrentTopic(String currentTopic) { this.currentTopic = currentTopic; }
    
    public LocalDateTime getNextSessionDate() { return nextSessionDate; }
    public void setNextSessionDate(LocalDateTime nextSessionDate) { this.nextSessionDate = nextSessionDate; }
    
    public Integer getTotalSessions() { return totalSessions; }
    public void setTotalSessions(Integer totalSessions) { this.totalSessions = totalSessions; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
