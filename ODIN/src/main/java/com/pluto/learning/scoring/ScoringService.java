package com.pluto.learning.scoring;

import com.pluto.learning.auth.User;
import com.pluto.learning.content.Skill;
import com.pluto.learning.content.Module;
import com.pluto.learning.progress.LearningActivity;
import com.pluto.learning.progress.LearningActivityRepository;
import com.pluto.learning.scoring.dto.ProgressSummary;
import com.pluto.learning.scoring.dto.ActivityRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ScoringService {
    
    private final UserProgressRepository progressRepository;
    private final LearningActivityRepository activityRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    
    @Autowired
    public ScoringService(UserProgressRepository progressRepository, 
                         LearningActivityRepository activityRepository,
                         KafkaTemplate<String, Object> kafkaTemplate) {
        this.progressRepository = progressRepository;
        this.activityRepository = activityRepository;
        this.kafkaTemplate = kafkaTemplate;
    }
    
    /**
     * Registrar nueva actividad de aprendizaje y actualizar progreso
     */
    public void recordLearningActivity(ActivityRequest request, User user) {
        // Crear actividad
        LearningActivity activity = new LearningActivity(
            user, 
            request.getActivityType(),
            request.getResourceId(),
            request.getResourceType(),
            request.getPointsEarned()
        );
        activity.setTimeSpentMinutes(request.getTimeSpentMinutes());
        activity.setScoreAchieved(request.getScoreAchieved());
        activity.setMetadata(request.getMetadata());
        
        activityRepository.save(activity);
        
        // Actualizar progreso si se proporciona skill
        if (request.getSkillId() != null) {
            updateUserProgress(user, request.getSkillId(), request);
        }
        
        // Enviar evento para analytics
        sendProgressEvent(user, activity);
    }
    
    /**
     * Obtener progreso del usuario en una skill específica
     */
    @Transactional(readOnly = true)
    public Optional<UserProgress> getUserProgress(User user, Skill skill) {
        return progressRepository.findByUserAndSkill(user, skill);
    }
    
    /**
     * Obtener resumen completo de progreso del usuario
     */
    @Transactional(readOnly = true)
    public ProgressSummary getUserProgressSummary(User user) {
        List<UserProgress> userProgress = progressRepository.findByUserOrderByLastActivityAtDesc(user);
        
        int totalSkillsStarted = (int) userProgress.stream()
            .filter(p -> p.getStatus() != UserProgress.ProgressStatus.NOT_STARTED)
            .count();
        
        int totalSkillsCompleted = (int) userProgress.stream()
            .filter(UserProgress::isCompleted)
            .count();
        
        int totalSkillsMastered = (int) userProgress.stream()
            .filter(UserProgress::isMastered)
            .count();
        
        int totalPointsEarned = userProgress.stream()
            .mapToInt(UserProgress::getTotalPointsEarned)
            .sum();
        
        int totalTimeSpent = userProgress.stream()
            .mapToInt(UserProgress::getTotalTimeSpentHours)
            .sum();
        
        double averageCompletion = userProgress.stream()
            .filter(p -> p.getStatus() != UserProgress.ProgressStatus.NOT_STARTED)
            .mapToInt(UserProgress::getCompletionPercentage)
            .average()
            .orElse(0.0);
        
        return new ProgressSummary(
            totalSkillsStarted,
            totalSkillsCompleted,
            totalSkillsMastered,
            totalPointsEarned,
            totalTimeSpent,
            averageCompletion,
            userProgress
        );
    }
    
    /**
     * Obtener actividades recientes del usuario
     */
    @Transactional(readOnly = true)
    public List<LearningActivity> getRecentActivities(User user, int limit) {
        return activityRepository.findByUserOrderByCreatedAtDesc(user)
            .stream()
            .limit(limit)
            .toList();
    }
    
    /**
     * Calcular puntuación de streak (racha de actividad)
     */
    @Transactional(readOnly = true)
    public int calculateStreakDays(User user) {
        List<LocalDateTime> activityDates = activityRepository.findDistinctActivityDatesByUser(user);
        
        if (activityDates.isEmpty()) {
            return 0;
        }
        
        int streak = 1;
        LocalDateTime current = activityDates.get(0).toLocalDate().atStartOfDay();
        
        for (int i = 1; i < activityDates.size(); i++) {
            LocalDateTime previous = activityDates.get(i).toLocalDate().atStartOfDay();
            
            if (current.minusDays(1).equals(previous)) {
                streak++;
                current = previous;
            } else {
                break;
            }
        }
        
        return streak;
    }
    
    /**
     * Obtener leaderboard de usuarios por puntos
     */
    @Transactional(readOnly = true)
    public List<LeaderboardEntry> getLeaderboard(int limit) {
        return progressRepository.findTopUsersByPoints(limit);
    }
    
    /**
     * Recalcular progreso de un usuario en una skill
     */
    public void recalculateProgress(User user, Skill skill) {
        UserProgress progress = progressRepository.findByUserAndSkill(user, skill)
            .orElse(new UserProgress(user, skill));
        
        // Obtener todas las actividades relacionadas con esta skill
        List<LearningActivity> activities = activityRepository.findByUserAndSkillRelated(user, skill.getId());
        
        int totalPoints = activities.stream()
            .mapToInt(LearningActivity::getPointsEarned)
            .sum();
        
        int totalTimeMinutes = activities.stream()
            .mapToInt(a -> a.getTimeSpentMinutes() != null ? a.getTimeSpentMinutes() : 0)
            .sum();
        
        // Calcular porcentaje de completitud basado en módulos completados
        int completedModules = (int) activities.stream()
            .filter(a -> "MODULE_COMPLETED".equals(a.getActivityType()))
            .count();
        
        int totalModules = skill.getModules().size();
        int completionPercentage = totalModules > 0 ? (completedModules * 100) / totalModules : 0;
        
        // Calcular score de maestría basado en puntuaciones de evaluaciones
        double averageScore = activities.stream()
            .filter(a -> a.getScoreAchieved() != null)
            .mapToInt(LearningActivity::getScoreAchieved)
            .average()
            .orElse(0.0);
        
        progress.updateProgress(
            completionPercentage,
            (int) averageScore,
            totalPoints - progress.getTotalPointsEarned(),
            (totalTimeMinutes / 60) - progress.getTotalTimeSpentHours()
        );
        
        progressRepository.save(progress);
    }
    
    // Métodos privados
    private void updateUserProgress(User user, Long skillId, ActivityRequest request) {
        // Este método se implementaría con la lógica específica de actualización
        // basada en el tipo de actividad y los puntos obtenidos
        
        // Por ahora, simplificamos asumiendo que tenemos la skill
        // En una implementación completa, buscaríamos la skill por ID
    }
    
    private void sendProgressEvent(User user, LearningActivity activity) {
        ProgressEvent event = new ProgressEvent(
            user.getId(),
            activity.getActivityType().toString(),
            activity.getResourceId(),
            activity.getResourceType(),
            activity.getPointsEarned(),
            activity.getCreatedAt()
        );
        
        kafkaTemplate.send("user-progress", event);
    }
    
    // Clases auxiliares
    public static class ProgressEvent {
        private final Long userId;
        private final String activityType;
        private final Long resourceId;
        private final String resourceType;
        private final Integer pointsEarned;
        private final LocalDateTime timestamp;
        
        public ProgressEvent(Long userId, String activityType, Long resourceId, String resourceType, Integer pointsEarned, LocalDateTime timestamp) {
            this.userId = userId;
            this.activityType = activityType;
            this.resourceId = resourceId;
            this.resourceType = resourceType;
            this.pointsEarned = pointsEarned;
            this.timestamp = timestamp;
        }
        
        // Getters
        public Long getUserId() { return userId; }
        public String getActivityType() { return activityType; }
        public Long getResourceId() { return resourceId; }
        public String getResourceType() { return resourceType; }
        public Integer getPointsEarned() { return pointsEarned; }
        public LocalDateTime getTimestamp() { return timestamp; }
    }
    
    public static class LeaderboardEntry {
        private final Long userId;
        private final String userName;
        private final Integer totalPoints;
        private final Integer completedSkills;
        private final Integer position;
        
        public LeaderboardEntry(Long userId, String userName, Integer totalPoints, Integer completedSkills, Integer position) {
            this.userId = userId;
            this.userName = userName;
            this.totalPoints = totalPoints;
            this.completedSkills = completedSkills;
            this.position = position;
        }
        
        // Getters
        public Long getUserId() { return userId; }
        public String getUserName() { return userName; }
        public Integer getTotalPoints() { return totalPoints; }
        public Integer getCompletedSkills() { return completedSkills; }
        public Integer getPosition() { return position; }
    }
}
