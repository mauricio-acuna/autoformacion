package com.pluto.learning.community;

import com.pluto.learning.auth.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad para gestión de posts en foros de discusión
 */
@Entity
@Table(name = "forum_posts")
public class ForumPost {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "forum_id", nullable = false)
    private DiscussionForum forum;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_post_id")
    private ForumPost parentPost; // Para respuestas/hilos
    
    @Column(name = "title", length = 300)
    private String title;
    
    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "post_type")
    private PostType postType;
    
    @Column(name = "is_pinned")
    private Boolean isPinned = false;
    
    @Column(name = "is_locked")
    private Boolean isLocked = false;
    
    @Column(name = "is_deleted")
    private Boolean isDeleted = false;
    
    @Column(name = "upvotes")
    private Integer upvotes = 0;
    
    @Column(name = "downvotes")
    private Integer downvotes = 0;
    
    @Column(name = "reply_count")
    private Integer replyCount = 0;
    
    @Column(name = "view_count")
    private Integer viewCount = 0;
    
    @Column(name = "tags")
    private String tags; // Comma-separated tags
    
    @Column(name = "code_snippet", columnDefinition = "TEXT")
    private String codeSnippet;
    
    @Column(name = "programming_language")
    private String programmingLanguage;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @Column(name = "last_activity_at")
    private LocalDateTime lastActivityAt;
    
    // Tipos de posts
    public enum PostType {
        DISCUSSION,     // Discusión general
        QUESTION,       // Pregunta
        ANSWER,         // Respuesta a pregunta
        ANNOUNCEMENT,   // Anuncio
        TUTORIAL,       // Tutorial
        CODE_REVIEW,    // Solicitud de revisión de código
        PROJECT_SHARE,  // Compartir proyecto
        POLL,           // Encuesta
        JOB_POSTING,    // Oferta de trabajo
        STUDY_GROUP     // Grupo de estudio
    }
    
    // Constructors
    public ForumPost() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.lastActivityAt = LocalDateTime.now();
    }
    
    public ForumPost(DiscussionForum forum, User author, String title, String content, PostType postType) {
        this();
        this.forum = forum;
        this.author = author;
        this.title = title;
        this.content = content;
        this.postType = postType;
    }
    
    // Business methods
    public void incrementUpvotes() {
        this.upvotes = (this.upvotes == null ? 0 : this.upvotes) + 1;
        this.lastActivityAt = LocalDateTime.now();
    }
    
    public void decrementUpvotes() {
        this.upvotes = Math.max(0, (this.upvotes == null ? 0 : this.upvotes) - 1);
        this.lastActivityAt = LocalDateTime.now();
    }
    
    public void incrementDownvotes() {
        this.downvotes = (this.downvotes == null ? 0 : this.downvotes) + 1;
        this.lastActivityAt = LocalDateTime.now();
    }
    
    public void decrementDownvotes() {
        this.downvotes = Math.max(0, (this.downvotes == null ? 0 : this.downvotes) - 1);
        this.lastActivityAt = LocalDateTime.now();
    }
    
    public void incrementReplyCount() {
        this.replyCount = (this.replyCount == null ? 0 : this.replyCount) + 1;
        this.lastActivityAt = LocalDateTime.now();
    }
    
    public void decrementReplyCount() {
        this.replyCount = Math.max(0, (this.replyCount == null ? 0 : this.replyCount) - 1);
        this.lastActivityAt = LocalDateTime.now();
    }
    
    public void incrementViewCount() {
        this.viewCount = (this.viewCount == null ? 0 : this.viewCount) + 1;
    }
    
    public Integer getScore() {
        int up = upvotes == null ? 0 : upvotes;
        int down = downvotes == null ? 0 : downvotes;
        return up - down;
    }
    
    public boolean isThread() {
        return parentPost == null;
    }
    
    public boolean isReply() {
        return parentPost != null;
    }
    
    public String getPopularityLevel() {
        int score = getScore();
        int views = viewCount == null ? 0 : viewCount;
        
        if (score >= 50 || views >= 1000) return "Very Popular";
        if (score >= 20 || views >= 500) return "Popular";
        if (score >= 10 || views >= 100) return "Moderate";
        if (score >= 5 || views >= 50) return "Rising";
        return "New";
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public DiscussionForum getForum() { return forum; }
    public void setForum(DiscussionForum forum) { this.forum = forum; }
    
    public User getAuthor() { return author; }
    public void setAuthor(User author) { this.author = author; }
    
    public ForumPost getParentPost() { return parentPost; }
    public void setParentPost(ForumPost parentPost) { this.parentPost = parentPost; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    
    public PostType getPostType() { return postType; }
    public void setPostType(PostType postType) { this.postType = postType; }
    
    public Boolean getIsPinned() { return isPinned; }
    public void setIsPinned(Boolean isPinned) { this.isPinned = isPinned; }
    
    public Boolean getIsLocked() { return isLocked; }
    public void setIsLocked(Boolean isLocked) { this.isLocked = isLocked; }
    
    public Boolean getIsDeleted() { return isDeleted; }
    public void setIsDeleted(Boolean isDeleted) { this.isDeleted = isDeleted; }
    
    public Integer getUpvotes() { return upvotes; }
    public void setUpvotes(Integer upvotes) { this.upvotes = upvotes; }
    
    public Integer getDownvotes() { return downvotes; }
    public void setDownvotes(Integer downvotes) { this.downvotes = downvotes; }
    
    public Integer getReplyCount() { return replyCount; }
    public void setReplyCount(Integer replyCount) { this.replyCount = replyCount; }
    
    public Integer getViewCount() { return viewCount; }
    public void setViewCount(Integer viewCount) { this.viewCount = viewCount; }
    
    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }
    
    public String getCodeSnippet() { return codeSnippet; }
    public void setCodeSnippet(String codeSnippet) { this.codeSnippet = codeSnippet; }
    
    public String getProgrammingLanguage() { return programmingLanguage; }
    public void setProgrammingLanguage(String programmingLanguage) { this.programmingLanguage = programmingLanguage; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public LocalDateTime getLastActivityAt() { return lastActivityAt; }
    public void setLastActivityAt(LocalDateTime lastActivityAt) { this.lastActivityAt = lastActivityAt; }
}
