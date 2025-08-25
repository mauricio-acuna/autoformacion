package com.pluto.learning.dashboard;

import com.pluto.learning.auth.User;
import com.pluto.learning.auth.UserRepository;
import com.pluto.learning.content.SkillRepository;
import com.pluto.learning.content.ModuleRepository;
import com.pluto.learning.assessments.LabRepository;
import com.pluto.learning.assessments.QuizRepository;
import com.pluto.learning.progress.UserSkillProgressRepository;
import com.pluto.learning.progress.LearningActivityRepository;
import com.pluto.learning.progress.LearningActivity;
import com.pluto.learning.submissions.SubmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.ArrayList;

@Service
@Transactional(readOnly = true)
public class DashboardAnalyticsService {
    
    private final UserRepository userRepository;
    private final SkillRepository skillRepository;
    private final ModuleRepository moduleRepository;
    private final LabRepository labRepository;
    private final QuizRepository quizRepository;
    private final UserSkillProgressRepository progressRepository;
    private final LearningActivityRepository activityRepository;
    private final SubmissionRepository submissionRepository;
    
    @Autowired
    public DashboardAnalyticsService(UserRepository userRepository,
                                   SkillRepository skillRepository,
                                   ModuleRepository moduleRepository,
                                   LabRepository labRepository,
                                   QuizRepository quizRepository,
                                   UserSkillProgressRepository progressRepository,
                                   LearningActivityRepository activityRepository,
                                   SubmissionRepository submissionRepository) {
        this.userRepository = userRepository;
        this.skillRepository = skillRepository;
        this.moduleRepository = moduleRepository;
        this.labRepository = labRepository;
        this.quizRepository = quizRepository;
        this.progressRepository = progressRepository;
        this.activityRepository = activityRepository;
        this.submissionRepository = submissionRepository;
    }
    
    /**
     * Obtener recomendaciones personalizadas para un usuario
     */
    public List<DashboardController.RecommendedSkill> getPersonalizedRecommendations(User user) {
        List<DashboardController.RecommendedSkill> recommendations = new ArrayList<>();
        
        // Obtener habilidades no iniciadas
        List<Long> startedSkillIds = progressRepository.findStartedSkillIdsByUser(user);
        
        // Simular algoritmo de recomendación básico
        skillRepository.findAvailableSkillsForUser(startedSkillIds)
                .stream()
                .limit(5)
                .forEach(skill -> {
                    String reason = "Basado en tu progreso actual y nivel de habilidad";
                    double matchScore = 0.8; // Algoritmo de matching simplificado
                    
                    recommendations.add(new DashboardController.RecommendedSkill(
                        skill.getId(),
                        skill.getName(),
                        reason,
                        matchScore
                    ));
                });
        
        return recommendations;
    }
    
    /**
     * Obtener métricas de aprendizaje de un usuario
     */
    public DashboardController.LearningMetrics getLearningMetrics(User user) {
        int totalStudyTime = activityRepository.sumTimeSpentByUser(user);
        
        // Calcular progreso de meta semanal (simplificado)
        LocalDateTime weekStart = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS).minusDays(7);
        int weeklyActivities = activityRepository.countByUserAndActivityTypeSince(
            user, LearningActivity.ActivityType.LESSON_COMPLETED, weekStart
        );
        int weeklyGoalProgress = Math.min(100, (weeklyActivities * 100) / 10); // Meta: 10 lecciones por semana
        
        // Calcular racha de días (simplificado)
        int streakDays = activityRepository.countDistinctActivityDays(user);
        
        LocalDateTime lastStudySession = activityRepository.findLastActivityDate(user);
        
        return new DashboardController.LearningMetrics(
            totalStudyTime,
            weeklyGoalProgress,
            streakDays,
            lastStudySession
        );
    }
    
    /**
     * Obtener resumen del sistema
     */
    public DashboardController.SystemOverview getSystemOverview() {
        int totalStudents = (int) userRepository.count();
        
        // Estudiantes activos (con actividad en los últimos 7 días)
        LocalDateTime weekAgo = LocalDateTime.now().minusDays(7);
        int activeStudents = totalStudents; // Simplificado
        
        // Habilidades completadas en total
        int completedSkills = (int) progressRepository.count();
        
        // Progreso promedio del sistema
        double averageProgress = 75.0; // Simplificado
        
        return new DashboardController.SystemOverview(
            totalStudents,
            activeStudents,
            completedSkills,
            averageProgress
        );
    }
    
    /**
     * Obtener estudiantes en riesgo
     */
    public List<DashboardController.StudentAtRisk> getStudentsAtRisk() {
        List<DashboardController.StudentAtRisk> studentsAtRisk = new ArrayList<>();
        
        // Algoritmo simplificado: usuarios sin actividad reciente
        LocalDateTime twoWeeksAgo = LocalDateTime.now().minusDays(14);
        
        userRepository.findAll().forEach(user -> {
            LocalDateTime lastActivity = activityRepository.findLastActivityDate(user);
            
            if (lastActivity != null && lastActivity.isBefore(twoWeeksAgo)) {
                studentsAtRisk.add(new DashboardController.StudentAtRisk(
                    user.getId(),
                    user.getName(),
                    0.8, // Score de riesgo alto
                    "Sin actividad en las últimas 2 semanas",
                    lastActivity
                ));
            }
        });
        
        return studentsAtRisk;
    }
    
    /**
     * Obtener actividad reciente del sistema
     */
    public List<LearningActivity> getRecentSystemActivity(int limit) {
        return activityRepository.findRecentSystemActivities()
                .stream()
                .limit(limit)
                .toList();
    }
    
    /**
     * Obtener métricas de contenido
     */
    public DashboardController.ContentMetrics getContentMetrics() {
        int totalSkills = (int) skillRepository.count();
        int totalModules = (int) moduleRepository.count();
        int totalLabs = (int) labRepository.count();
        int totalQuizzes = (int) quizRepository.count();
        
        // Puntuaciones promedio (simplificadas)
        double averageLabScore = 78.5;
        double averageQuizScore = 82.3;
        
        return new DashboardController.ContentMetrics(
            totalSkills,
            totalModules,
            totalLabs,
            totalQuizzes,
            averageLabScore,
            averageQuizScore
        );
    }
    
    /**
     * Obtener tendencias de progreso
     */
    public DashboardController.ProgressTrends getProgressTrends(User user, int days) {
        List<DashboardController.DailyProgress> dailyProgress = new ArrayList<>();
        List<DashboardController.SkillProgress> skillTrends = new ArrayList<>();
        
        // Generar datos de progreso diario (simplificado)
        LocalDateTime startDate = LocalDateTime.now().minusDays(days);
        
        for (int i = 0; i < days; i++) {
            LocalDateTime date = startDate.plusDays(i);
            
            // Datos simulados para demo
            dailyProgress.add(new DashboardController.DailyProgress(
                date,
                (int) (Math.random() * 5), // Actividades completadas
                (int) (Math.random() * 100), // Puntos ganados
                (int) (Math.random() * 120) // Tiempo invertido
            ));
        }
        
        // Generar tendencias de habilidades (simplificado)
        progressRepository.findByUserOrderByUpdatedAtDesc(user)
                .stream()
                .limit(5)
                .forEach(progress -> {
                    skillTrends.add(new DashboardController.SkillProgress(
                        progress.getSkill().getName(),
                        progress.getCompletionPercentage(),
                        Math.max(0, progress.getCompletionPercentage() - 10), // Progreso anterior
                        5.0 // Tendencia positiva
                    ));
                });
        
        return new DashboardController.ProgressTrends(dailyProgress, skillTrends);
    }
    
    /**
     * Obtener distribución de habilidades
     */
    public DashboardController.SkillDistribution getSkillDistribution() {
        List<DashboardController.CategoryStats> categoryStats = new ArrayList<>();
        List<DashboardController.LevelStats> levelStats = new ArrayList<>();
        
        // Datos simplificados para categorías
        categoryStats.add(new DashboardController.CategoryStats("Java Fundamentals", 8, 5, 62.5));
        categoryStats.add(new DashboardController.CategoryStats("Spring Framework", 6, 3, 50.0));
        categoryStats.add(new DashboardController.CategoryStats("Database", 4, 2, 50.0));
        
        // Datos simplificados para niveles
        levelStats.add(new DashboardController.LevelStats("Beginner", 15, 78.5));
        levelStats.add(new DashboardController.LevelStats("Intermediate", 12, 72.3));
        levelStats.add(new DashboardController.LevelStats("Advanced", 8, 69.1));
        
        return new DashboardController.SkillDistribution(categoryStats, levelStats);
    }
    
    /**
     * Obtener métricas de rendimiento del sistema
     */
    public DashboardController.PerformanceMetrics getPerformanceMetrics() {
        // Métricas simuladas para demo
        return new DashboardController.PerformanceMetrics(
            99.8, // System uptime
            10250, // Total requests
            145.6, // Average response time (ms)
            23 // Active users
        );
    }
}
