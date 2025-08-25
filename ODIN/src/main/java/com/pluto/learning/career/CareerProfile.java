package com.pluto.learning.career;

import com.pluto.learning.auth.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Entidad para gestión de perfiles profesionales y roadmaps de carrera
 */
@Entity
@Table(name = "career_profiles")
public class CareerProfile {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "career_goal", nullable = false)
    private CareerGoal careerGoal;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "experience_level", nullable = false)
    private ExperienceLevel experienceLevel;
    
    @Column(name = "target_role", length = 200)
    private String targetRole;
    
    @Column(name = "target_company", length = 200)
    private String targetCompany;
    
    @Column(name = "expected_salary_min")
    private Integer expectedSalaryMin;
    
    @Column(name = "expected_salary_max")
    private Integer expectedSalaryMax;
    
    @Column(name = "location_preference", length = 100)
    private String locationPreference;
    
    @Column(name = "remote_preference")
    @Enumerated(EnumType.STRING)
    private RemotePreference remotePreference;
    
    @Column(name = "availability_date")
    private LocalDateTime availabilityDate;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    // Skills técnicas priorizadas (relación many-to-many)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "career_profile_skills",
        joinColumns = @JoinColumn(name = "career_profile_id"),
        inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    private List<com.pluto.learning.content.Skill> prioritySkills;
    
    public enum CareerGoal {
        JUNIOR_DEVELOPER,
        MID_LEVEL_DEVELOPER,
        SENIOR_DEVELOPER,
        TECH_LEAD,
        ARCHITECT,
        ENGINEERING_MANAGER,
        PRODUCT_MANAGER,
        DEVOPS_ENGINEER,
        DATA_SCIENTIST,
        CAREER_CHANGE
    }
    
    public enum ExperienceLevel {
        BEGINNER,        // 0-1 años
        INTERMEDIATE,    // 1-3 años
        ADVANCED,        // 3-5 años
        EXPERT          // 5+ años
    }
    
    public enum RemotePreference {
        ONSITE_ONLY,
        REMOTE_ONLY,
        HYBRID,
        NO_PREFERENCE
    }
    
    // Constructors
    public CareerProfile() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public CareerProfile(User user, CareerGoal careerGoal, ExperienceLevel experienceLevel) {
        this();
        this.user = user;
        this.careerGoal = careerGoal;
        this.experienceLevel = experienceLevel;
    }
    
    // Business methods
    public boolean isReadyForJob() {
        return availabilityDate != null && 
               !availabilityDate.isAfter(LocalDateTime.now().plusMonths(3));
    }
    
    public String getSalaryRange() {
        if (expectedSalaryMin != null && expectedSalaryMax != null) {
            return String.format("$%,d - $%,d", expectedSalaryMin, expectedSalaryMax);
        }
        return "Not specified";
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    
    public CareerGoal getCareerGoal() { return careerGoal; }
    public void setCareerGoal(CareerGoal careerGoal) { this.careerGoal = careerGoal; }
    
    public ExperienceLevel getExperienceLevel() { return experienceLevel; }
    public void setExperienceLevel(ExperienceLevel experienceLevel) { this.experienceLevel = experienceLevel; }
    
    public String getTargetRole() { return targetRole; }
    public void setTargetRole(String targetRole) { this.targetRole = targetRole; }
    
    public String getTargetCompany() { return targetCompany; }
    public void setTargetCompany(String targetCompany) { this.targetCompany = targetCompany; }
    
    public Integer getExpectedSalaryMin() { return expectedSalaryMin; }
    public void setExpectedSalaryMin(Integer expectedSalaryMin) { this.expectedSalaryMin = expectedSalaryMin; }
    
    public Integer getExpectedSalaryMax() { return expectedSalaryMax; }
    public void setExpectedSalaryMax(Integer expectedSalaryMax) { this.expectedSalaryMax = expectedSalaryMax; }
    
    public String getLocationPreference() { return locationPreference; }
    public void setLocationPreference(String locationPreference) { this.locationPreference = locationPreference; }
    
    public RemotePreference getRemotePreference() { return remotePreference; }
    public void setRemotePreference(RemotePreference remotePreference) { this.remotePreference = remotePreference; }
    
    public LocalDateTime getAvailabilityDate() { return availabilityDate; }
    public void setAvailabilityDate(LocalDateTime availabilityDate) { this.availabilityDate = availabilityDate; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public List<com.pluto.learning.content.Skill> getPrioritySkills() { return prioritySkills; }
    public void setPrioritySkills(List<com.pluto.learning.content.Skill> prioritySkills) { this.prioritySkills = prioritySkills; }
}
