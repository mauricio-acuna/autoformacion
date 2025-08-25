package com.pluto.learning.community;

import com.pluto.learning.auth.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad para gestión de membresías en grupos de estudio
 */
@Entity
@Table(name = "study_group_memberships",
       uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "study_group_id"}))
public class StudyGroupMembership {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_group_id", nullable = false)
    private StudyGroup studyGroup;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private MemberRole role;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private MembershipStatus status;
    
    @Column(name = "is_active")
    private Boolean isActive = true;
    
    @Column(name = "sessions_attended")
    private Integer sessionsAttended = 0;
    
    @Column(name = "contributions_count")
    private Integer contributionsCount = 0;
    
    @Column(name = "last_activity_date")
    private LocalDateTime lastActivityDate;
    
    @Column(name = "joined_at", nullable = false)
    private LocalDateTime joinedAt;
    
    @Column(name = "left_at")
    private LocalDateTime leftAt;
    
    // Enums
    public enum MemberRole {
        OWNER,      // Creador del grupo
        MODERATOR,  // Moderador/Co-líder
        MEMBER,     // Miembro regular
        OBSERVER    // Solo observa, participación limitada
    }
    
    public enum MembershipStatus {
        PENDING,    // Solicitud pendiente de aprobación
        ACTIVE,     // Miembro activo
        INACTIVE,   // Inactivo temporalmente
        BANNED,     // Expulsado del grupo
        LEFT        // Salió voluntariamente
    }
    
    // Constructors
    public StudyGroupMembership() {
        this.joinedAt = LocalDateTime.now();
        this.lastActivityDate = LocalDateTime.now();
    }
    
    public StudyGroupMembership(User user, StudyGroup studyGroup, MemberRole role) {
        this();
        this.user = user;
        this.studyGroup = studyGroup;
        this.role = role;
        this.status = MembershipStatus.ACTIVE;
    }
    
    // Business methods
    public void incrementSessionsAttended() {
        this.sessionsAttended++;
        this.lastActivityDate = LocalDateTime.now();
    }
    
    public void incrementContributions() {
        this.contributionsCount++;
        this.lastActivityDate = LocalDateTime.now();
    }
    
    public void updateActivity() {
        this.lastActivityDate = LocalDateTime.now();
    }
    
    public void leaveGroup() {
        this.status = MembershipStatus.LEFT;
        this.isActive = false;
        this.leftAt = LocalDateTime.now();
    }
    
    public void banFromGroup() {
        this.status = MembershipStatus.BANNED;
        this.isActive = false;
        this.leftAt = LocalDateTime.now();
    }
    
    public boolean canModerate() {
        return (role == MemberRole.OWNER || role == MemberRole.MODERATOR) 
               && status == MembershipStatus.ACTIVE;
    }
    
    public boolean isOwner() {
        return role == MemberRole.OWNER;
    }
    
    public String getParticipationLevel() {
        if (sessionsAttended == null || sessionsAttended == 0) return "New Member";
        
        int attended = sessionsAttended;
        int contributions = contributionsCount == null ? 0 : contributionsCount;
        
        if (attended >= 20 && contributions >= 15) return "Highly Active";
        if (attended >= 10 && contributions >= 8) return "Active";
        if (attended >= 5 && contributions >= 3) return "Regular";
        if (attended >= 2 || contributions >= 1) return "Participating";
        return "Observer";
    }
    
    public Long getDaysInGroup() {
        return java.time.temporal.ChronoUnit.DAYS.between(joinedAt.toLocalDate(), 
                                                          LocalDateTime.now().toLocalDate());
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    
    public StudyGroup getStudyGroup() { return studyGroup; }
    public void setStudyGroup(StudyGroup studyGroup) { this.studyGroup = studyGroup; }
    
    public MemberRole getRole() { return role; }
    public void setRole(MemberRole role) { this.role = role; }
    
    public MembershipStatus getStatus() { return status; }
    public void setStatus(MembershipStatus status) { this.status = status; }
    
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    
    public Integer getSessionsAttended() { return sessionsAttended; }
    public void setSessionsAttended(Integer sessionsAttended) { this.sessionsAttended = sessionsAttended; }
    
    public Integer getContributionsCount() { return contributionsCount; }
    public void setContributionsCount(Integer contributionsCount) { this.contributionsCount = contributionsCount; }
    
    public LocalDateTime getLastActivityDate() { return lastActivityDate; }
    public void setLastActivityDate(LocalDateTime lastActivityDate) { this.lastActivityDate = lastActivityDate; }
    
    public LocalDateTime getJoinedAt() { return joinedAt; }
    public void setJoinedAt(LocalDateTime joinedAt) { this.joinedAt = joinedAt; }
    
    public LocalDateTime getLeftAt() { return leftAt; }
    public void setLeftAt(LocalDateTime leftAt) { this.leftAt = leftAt; }
}
