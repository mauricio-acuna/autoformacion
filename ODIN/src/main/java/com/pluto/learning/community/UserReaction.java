package com.pluto.learning.community;

import com.pluto.learning.auth.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad para gestión de reacciones de usuarios en posts
 */
@Entity
@Table(name = "user_reactions", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "post_id"}))
public class UserReaction {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private ForumPost post;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "reaction_type", nullable = false)
    private ReactionType reactionType;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    // Tipos de reacciones
    public enum ReactionType {
        UPVOTE,     // Me gusta / Útil
        DOWNVOTE,   // No me gusta / No útil
        LOVE,       // Me encanta
        HELPFUL,    // Muy útil
        SOLVED,     // Resuelve el problema
        BOOKMARK,   // Guardar para después
        REPORT      // Reportar contenido
    }
    
    // Constructors
    public UserReaction() {
        this.createdAt = LocalDateTime.now();
    }
    
    public UserReaction(User user, ForumPost post, ReactionType reactionType) {
        this();
        this.user = user;
        this.post = post;
        this.reactionType = reactionType;
    }
    
    // Business methods
    public boolean isPositiveReaction() {
        return reactionType == ReactionType.UPVOTE || 
               reactionType == ReactionType.LOVE || 
               reactionType == ReactionType.HELPFUL ||
               reactionType == ReactionType.SOLVED;
    }
    
    public boolean isNegativeReaction() {
        return reactionType == ReactionType.DOWNVOTE ||
               reactionType == ReactionType.REPORT;
    }
    
    public int getScoreValue() {
        switch (reactionType) {
            case UPVOTE: return 1;
            case DOWNVOTE: return -1;
            case LOVE: return 2;
            case HELPFUL: return 2;
            case SOLVED: return 3;
            case BOOKMARK: return 0;
            case REPORT: return -2;
            default: return 0;
        }
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    
    public ForumPost getPost() { return post; }
    public void setPost(ForumPost post) { this.post = post; }
    
    public ReactionType getReactionType() { return reactionType; }
    public void setReactionType(ReactionType reactionType) { this.reactionType = reactionType; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
