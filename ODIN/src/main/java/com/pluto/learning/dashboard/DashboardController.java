package com.pluto.learning.dashboard;

import com.pluto.learning.auth.User;
import com.pluto.learning.auth.UserService;
import com.pluto.learning.progress.ProgressTrackingService;
import com.pluto.learning.progress.UserSkillProgress;
import com.pluto.learning.progress.LearningActivity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/dashboard")
@Tag(name = "Dashboard", description = "API para dashboard de estudiante y analytics")
public class DashboardController {
    
    private final ProgressTrackingService progressService;
    private final UserService userService;
    private final DashboardAnalyticsService analyticsService;
    
    @Autowired
    public DashboardController(ProgressTrackingService progressService, 
                              UserService userService,
                              DashboardAnalyticsService analyticsService) {
        this.progressService = progressService;
        this.userService = userService;
        this.analyticsService = analyticsService;
    }
    
    @GetMapping("/student")
    @Operation(summary = "Obtener dashboard del estudiante")
    @ApiResponse(responseCode = "200", description = "Dashboard del estudiante")
    @PreAuthorize("hasRole('LEARNER') or hasRole('ADMIN')")
    public ResponseEntity<StudentDashboard> getStudentDashboard(Authentication authentication) {
        User user = userService.getCurrentUser(authentication);
        
        // Obtener estadísticas de progreso
        ProgressTrackingService.UserProgressStats stats = progressService.getUserProgressStats(user);
        
        // Obtener progreso de habilidades
        List<UserSkillProgress> skillProgress = progressService.getAllUserProgress(user);
        
        // Obtener actividades recientes
        List<LearningActivity> recentActivities = progressService.getRecentActivities(user, 10);
        
        // Obtener habilidades recomendadas
        List<RecommendedSkill> recommendations = analyticsService.getPersonalizedRecommendations(user);
        
        // Obtener métricas de aprendizaje
        LearningMetrics metrics = analyticsService.getLearningMetrics(user);
        
        StudentDashboard dashboard = new StudentDashboard(
            stats,
            skillProgress,
            recentActivities,
            recommendations,
            metrics
        );
        
        return ResponseEntity.ok(dashboard);
    }
    
    @GetMapping("/instructor")
    @Operation(summary = "Obtener dashboard del instructor")
    @ApiResponse(responseCode = "200", description = "Dashboard del instructor")
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<InstructorDashboard> getInstructorDashboard() {
        
        // Obtener estadísticas generales
        SystemOverview overview = analyticsService.getSystemOverview();
        
        // Obtener estudiantes con bajo rendimiento
        List<StudentAtRisk> studentsAtRisk = analyticsService.getStudentsAtRisk();
        
        // Obtener actividad reciente del sistema
        List<LearningActivity> systemActivity = analyticsService.getRecentSystemActivity(20);
        
        // Obtener métricas de contenido
        ContentMetrics contentMetrics = analyticsService.getContentMetrics();
        
        InstructorDashboard dashboard = new InstructorDashboard(
            overview,
            studentsAtRisk,
            systemActivity,
            contentMetrics
        );
        
        return ResponseEntity.ok(dashboard);
    }
    
    @GetMapping("/analytics/progress-trends")
    @Operation(summary = "Obtener tendencias de progreso")
    @ApiResponse(responseCode = "200", description = "Tendencias de progreso del usuario")
    @PreAuthorize("hasRole('LEARNER') or hasRole('ADMIN')")
    public ResponseEntity<ProgressTrends> getProgressTrends(
            Authentication authentication,
            @RequestParam(defaultValue = "30") int days) {
        
        User user = userService.getCurrentUser(authentication);
        ProgressTrends trends = analyticsService.getProgressTrends(user, days);
        
        return ResponseEntity.ok(trends);
    }
    
    @GetMapping("/analytics/skill-distribution")
    @Operation(summary = "Obtener distribución de habilidades")
    @ApiResponse(responseCode = "200", description = "Distribución de habilidades por categoría")
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<SkillDistribution> getSkillDistribution() {
        SkillDistribution distribution = analyticsService.getSkillDistribution();
        return ResponseEntity.ok(distribution);
    }
    
    @GetMapping("/analytics/performance-metrics")
    @Operation(summary = "Obtener métricas de rendimiento")
    @ApiResponse(responseCode = "200", description = "Métricas de rendimiento del sistema")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PerformanceMetrics> getPerformanceMetrics() {
        PerformanceMetrics metrics = analyticsService.getPerformanceMetrics();
        return ResponseEntity.ok(metrics);
    }
    
    // DTOs para el Dashboard
    public static class StudentDashboard {
        private final ProgressTrackingService.UserProgressStats stats;
        private final List<UserSkillProgress> skillProgress;
        private final List<LearningActivity> recentActivities;
        private final List<RecommendedSkill> recommendations;
        private final LearningMetrics metrics;
        
        public StudentDashboard(ProgressTrackingService.UserProgressStats stats,
                               List<UserSkillProgress> skillProgress,
                               List<LearningActivity> recentActivities,
                               List<RecommendedSkill> recommendations,
                               LearningMetrics metrics) {
            this.stats = stats;
            this.skillProgress = skillProgress;
            this.recentActivities = recentActivities;
            this.recommendations = recommendations;
            this.metrics = metrics;
        }
        
        // Getters
        public ProgressTrackingService.UserProgressStats getStats() { return stats; }
        public List<UserSkillProgress> getSkillProgress() { return skillProgress; }
        public List<LearningActivity> getRecentActivities() { return recentActivities; }
        public List<RecommendedSkill> getRecommendations() { return recommendations; }
        public LearningMetrics getMetrics() { return metrics; }
    }
    
    public static class InstructorDashboard {
        private final SystemOverview overview;
        private final List<StudentAtRisk> studentsAtRisk;
        private final List<LearningActivity> systemActivity;
        private final ContentMetrics contentMetrics;
        
        public InstructorDashboard(SystemOverview overview,
                                  List<StudentAtRisk> studentsAtRisk,
                                  List<LearningActivity> systemActivity,
                                  ContentMetrics contentMetrics) {
            this.overview = overview;
            this.studentsAtRisk = studentsAtRisk;
            this.systemActivity = systemActivity;
            this.contentMetrics = contentMetrics;
        }
        
        // Getters
        public SystemOverview getOverview() { return overview; }
        public List<StudentAtRisk> getStudentsAtRisk() { return studentsAtRisk; }
        public List<LearningActivity> getSystemActivity() { return systemActivity; }
        public ContentMetrics getContentMetrics() { return contentMetrics; }
    }
    
    // Clases auxiliares para métricas
    public static class RecommendedSkill {
        private final Long skillId;
        private final String skillName;
        private final String reason;
        private final double matchScore;
        
        public RecommendedSkill(Long skillId, String skillName, String reason, double matchScore) {
            this.skillId = skillId;
            this.skillName = skillName;
            this.reason = reason;
            this.matchScore = matchScore;
        }
        
        // Getters
        public Long getSkillId() { return skillId; }
        public String getSkillName() { return skillName; }
        public String getReason() { return reason; }
        public double getMatchScore() { return matchScore; }
    }
    
    public static class LearningMetrics {
        private final int totalStudyTime;
        private final int weeklyGoalProgress;
        private final int streakDays;
        private final LocalDateTime lastStudySession;
        
        public LearningMetrics(int totalStudyTime, int weeklyGoalProgress, int streakDays, LocalDateTime lastStudySession) {
            this.totalStudyTime = totalStudyTime;
            this.weeklyGoalProgress = weeklyGoalProgress;
            this.streakDays = streakDays;
            this.lastStudySession = lastStudySession;
        }
        
        // Getters
        public int getTotalStudyTime() { return totalStudyTime; }
        public int getWeeklyGoalProgress() { return weeklyGoalProgress; }
        public int getStreakDays() { return streakDays; }
        public LocalDateTime getLastStudySession() { return lastStudySession; }
    }
    
    public static class SystemOverview {
        private final int totalStudents;
        private final int activeStudents;
        private final int completedSkills;
        private final double averageProgress;
        
        public SystemOverview(int totalStudents, int activeStudents, int completedSkills, double averageProgress) {
            this.totalStudents = totalStudents;
            this.activeStudents = activeStudents;
            this.completedSkills = completedSkills;
            this.averageProgress = averageProgress;
        }
        
        // Getters
        public int getTotalStudents() { return totalStudents; }
        public int getActiveStudents() { return activeStudents; }
        public int getCompletedSkills() { return completedSkills; }
        public double getAverageProgress() { return averageProgress; }
    }
    
    public static class StudentAtRisk {
        private final Long userId;
        private final String userName;
        private final double riskScore;
        private final String riskFactors;
        private final LocalDateTime lastActivity;
        
        public StudentAtRisk(Long userId, String userName, double riskScore, String riskFactors, LocalDateTime lastActivity) {
            this.userId = userId;
            this.userName = userName;
            this.riskScore = riskScore;
            this.riskFactors = riskFactors;
            this.lastActivity = lastActivity;
        }
        
        // Getters
        public Long getUserId() { return userId; }
        public String getUserName() { return userName; }
        public double getRiskScore() { return riskScore; }
        public String getRiskFactors() { return riskFactors; }
        public LocalDateTime getLastActivity() { return lastActivity; }
    }
    
    public static class ContentMetrics {
        private final int totalSkills;
        private final int totalModules;
        private final int totalLabs;
        private final int totalQuizzes;
        private final double averageLabScore;
        private final double averageQuizScore;
        
        public ContentMetrics(int totalSkills, int totalModules, int totalLabs, int totalQuizzes, double averageLabScore, double averageQuizScore) {
            this.totalSkills = totalSkills;
            this.totalModules = totalModules;
            this.totalLabs = totalLabs;
            this.totalQuizzes = totalQuizzes;
            this.averageLabScore = averageLabScore;
            this.averageQuizScore = averageQuizScore;
        }
        
        // Getters
        public int getTotalSkills() { return totalSkills; }
        public int getTotalModules() { return totalModules; }
        public int getTotalLabs() { return totalLabs; }
        public int getTotalQuizzes() { return totalQuizzes; }
        public double getAverageLabScore() { return averageLabScore; }
        public double getAverageQuizScore() { return averageQuizScore; }
    }
    
    public static class ProgressTrends {
        private final List<DailyProgress> dailyProgress;
        private final List<SkillProgress> skillTrends;
        
        public ProgressTrends(List<DailyProgress> dailyProgress, List<SkillProgress> skillTrends) {
            this.dailyProgress = dailyProgress;
            this.skillTrends = skillTrends;
        }
        
        // Getters
        public List<DailyProgress> getDailyProgress() { return dailyProgress; }
        public List<SkillProgress> getSkillTrends() { return skillTrends; }
    }
    
    public static class DailyProgress {
        private final LocalDateTime date;
        private final int activitiesCompleted;
        private final int pointsEarned;
        private final int timeSpent;
        
        public DailyProgress(LocalDateTime date, int activitiesCompleted, int pointsEarned, int timeSpent) {
            this.date = date;
            this.activitiesCompleted = activitiesCompleted;
            this.pointsEarned = pointsEarned;
            this.timeSpent = timeSpent;
        }
        
        // Getters
        public LocalDateTime getDate() { return date; }
        public int getActivitiesCompleted() { return activitiesCompleted; }
        public int getPointsEarned() { return pointsEarned; }
        public int getTimeSpent() { return timeSpent; }
    }
    
    public static class SkillProgress {
        private final String skillName;
        private final int currentProgress;
        private final int previousProgress;
        private final double trend;
        
        public SkillProgress(String skillName, int currentProgress, int previousProgress, double trend) {
            this.skillName = skillName;
            this.currentProgress = currentProgress;
            this.previousProgress = previousProgress;
            this.trend = trend;
        }
        
        // Getters
        public String getSkillName() { return skillName; }
        public int getCurrentProgress() { return currentProgress; }
        public int getPreviousProgress() { return previousProgress; }
        public double getTrend() { return trend; }
    }
    
    public static class SkillDistribution {
        private final List<CategoryStats> categoryStats;
        private final List<LevelStats> levelStats;
        
        public SkillDistribution(List<CategoryStats> categoryStats, List<LevelStats> levelStats) {
            this.categoryStats = categoryStats;
            this.levelStats = levelStats;
        }
        
        // Getters
        public List<CategoryStats> getCategoryStats() { return categoryStats; }
        public List<LevelStats> getLevelStats() { return levelStats; }
    }
    
    public static class CategoryStats {
        private final String category;
        private final int totalSkills;
        private final int completedSkills;
        private final double completionRate;
        
        public CategoryStats(String category, int totalSkills, int completedSkills, double completionRate) {
            this.category = category;
            this.totalSkills = totalSkills;
            this.completedSkills = completedSkills;
            this.completionRate = completionRate;
        }
        
        // Getters
        public String getCategory() { return category; }
        public int getTotalSkills() { return totalSkills; }
        public int getCompletedSkills() { return completedSkills; }
        public double getCompletionRate() { return completionRate; }
    }
    
    public static class LevelStats {
        private final String level;
        private final int studentCount;
        private final double averageScore;
        
        public LevelStats(String level, int studentCount, double averageScore) {
            this.level = level;
            this.studentCount = studentCount;
            this.averageScore = averageScore;
        }
        
        // Getters
        public String getLevel() { return level; }
        public int getStudentCount() { return studentCount; }
        public double getAverageScore() { return averageScore; }
    }
    
    public static class PerformanceMetrics {
        private final double systemUptime;
        private final int totalRequests;
        private final double averageResponseTime;
        private final int activeUsers;
        
        public PerformanceMetrics(double systemUptime, int totalRequests, double averageResponseTime, int activeUsers) {
            this.systemUptime = systemUptime;
            this.totalRequests = totalRequests;
            this.averageResponseTime = averageResponseTime;
            this.activeUsers = activeUsers;
        }
        
        // Getters
        public double getSystemUptime() { return systemUptime; }
        public int getTotalRequests() { return totalRequests; }
        public double getAverageResponseTime() { return averageResponseTime; }
        public int getActiveUsers() { return activeUsers; }
    }
}
