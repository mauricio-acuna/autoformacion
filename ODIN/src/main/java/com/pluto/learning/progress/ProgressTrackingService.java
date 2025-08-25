package com.pluto.learning.progress;

import com.pluto.learning.auth.User;
import com.pluto.learning.content.Skill;
import com.pluto.learning.content.Module;
import com.pluto.learning.content.SkillRepository;
import com.pluto.learning.content.ModuleRepository;
import com.pluto.learning.assessments.LabRepository;
import com.pluto.learning.assessments.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProgressTrackingService {
    
    private final UserSkillProgressRepository progressRepository;
    private final LearningActivityRepository activityRepository;
    private final SkillRepository skillRepository;
    private final ModuleRepository moduleRepository;
    private final LabRepository labRepository;
    private final QuizRepository quizRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    
    @Autowired
    public ProgressTrackingService(UserSkillProgressRepository progressRepository,
                                 LearningActivityRepository activityRepository,
                                 SkillRepository skillRepository,
                                 ModuleRepository moduleRepository,
                                 LabRepository labRepository,
                                 QuizRepository quizRepository,
                                 KafkaTemplate<String, Object> kafkaTemplate) {
        this.progressRepository = progressRepository;
        this.activityRepository = activityRepository;
        this.skillRepository = skillRepository;
        this.moduleRepository = moduleRepository;
        this.labRepository = labRepository;
        this.quizRepository = quizRepository;
        this.kafkaTemplate = kafkaTemplate;
    }
    
    /**
     * Registrar actividad de aprendizaje
     */
    public void recordActivity(LearningActivity activity) {
        activityRepository.save(activity);
        
        // Actualizar progreso de la habilidad relacionada
        updateSkillProgress(activity.getUser(), activity);
        
        // Enviar evento para analytics
        kafkaTemplate.send("learning-activity", activity);
    }
    
    /**
     * Obtener progreso de usuario en una habilidad
     */
    @Transactional(readOnly = true)
    public UserSkillProgress getUserSkillProgress(User user, Skill skill) {
        Optional<UserSkillProgress> existing = progressRepository.findByUserAndSkill(user, skill);
        
        if (existing.isPresent()) {
            return existing.get();
        } else {
            // Crear nuevo progreso si no existe
            return initializeSkillProgress(user, skill);
        }
    }
    
    /**
     * Obtener progreso de todas las habilidades de un usuario
     */
    @Transactional(readOnly = true)
    public List<UserSkillProgress> getAllUserProgress(User user) {
        return progressRepository.findByUserOrderByUpdatedAtDesc(user);
    }
    
    /**
     * Obtener actividades recientes de un usuario
     */
    @Transactional(readOnly = true)
    public List<LearningActivity> getRecentActivities(User user, int limit) {
        return activityRepository.findByUserOrderByCreatedAtDesc(user)
                .stream()
                .limit(limit)
                .toList();
    }
    
    /**
     * Obtener estadísticas de progreso de un usuario
     */
    @Transactional(readOnly = true)
    public UserProgressStats getUserProgressStats(User user) {
        List<UserSkillProgress> allProgress = getAllUserProgress(user);
        
        int totalSkills = allProgress.size();
        long completedSkills = allProgress.stream()
                .mapToLong(p -> p.isCompleted() ? 1 : 0)
                .sum();
        long masteredSkills = allProgress.stream()
                .mapToLong(p -> p.isMastered() ? 1 : 0)
                .sum();
        
        int totalPoints = allProgress.stream()
                .mapToInt(UserSkillProgress::getTotalPointsEarned)
                .sum();
        
        double averageCompletion = allProgress.stream()
                .mapToInt(UserSkillProgress::getCompletionPercentage)
                .average()
                .orElse(0.0);
        
        int activeDays = activityRepository.countDistinctActivityDays(user);
        LocalDateTime lastActivity = activityRepository.findLastActivityDate(user);
        
        return new UserProgressStats(
                totalSkills,
                (int) completedSkills,
                (int) masteredSkills,
                totalPoints,
                averageCompletion,
                activeDays,
                lastActivity
        );
    }
    
    /**
     * Obtener habilidades recomendadas para un usuario
     */
    @Transactional(readOnly = true)
    public List<Skill> getRecommendedSkills(User user, int limit) {
        // Obtener habilidades ya iniciadas
        List<Long> startedSkillIds = progressRepository.findStartedSkillIdsByUser(user);
        
        // Encontrar habilidades no iniciadas con nivel apropiado
        List<Skill> availableSkills = skillRepository.findAvailableSkillsForUser(startedSkillIds);
        
        return availableSkills.stream().limit(limit).toList();
    }
    
    // Métodos privados
    private UserSkillProgress initializeSkillProgress(User user, Skill skill) {
        UserSkillProgress progress = new UserSkillProgress(user, skill);
        
        // Contar elementos totales de la habilidad
        int totalModules = moduleRepository.countBySkillAndActiveTrue(skill);
        int totalLabs = labRepository.countByModuleSkillAndActiveTrue(skill);
        int totalQuizzes = quizRepository.countByModuleSkillAndActiveTrue(skill);
        
        progress.setTotalModules(totalModules);
        progress.setTotalLabs(totalLabs);
        progress.setTotalQuizzes(totalQuizzes);
        
        // Calcular puntos totales posibles
        int totalPointsPossible = calculateTotalPointsPossible(skill);
        progress.setTotalPointsPossible(totalPointsPossible);
        
        return progressRepository.save(progress);
    }
    
    private void updateSkillProgress(User user, LearningActivity activity) {
        // Determinar qué habilidad actualizar basado en la actividad
        Skill skill = getSkillFromActivity(activity);
        if (skill == null) return;
        
        UserSkillProgress progress = getUserSkillProgress(user, skill);
        
        // Actualizar contadores basado en el tipo de actividad
        switch (activity.getActivityType()) {
            case LESSON_COMPLETED:
                progress.setCompletedLessons(progress.getCompletedLessons() + 1);
                break;
            case QUIZ_PASSED:
                progress.setPassedQuizzes(progress.getPassedQuizzes() + 1);
                if (activity.getPointsEarned() != null) {
                    progress.setTotalPointsEarned(progress.getTotalPointsEarned() + activity.getPointsEarned());
                }
                break;
            case LAB_PASSED:
                progress.setPassedLabs(progress.getPassedLabs() + 1);
                if (activity.getPointsEarned() != null) {
                    progress.setTotalPointsEarned(progress.getTotalPointsEarned() + activity.getPointsEarned());
                }
                break;
            case MODULE_COMPLETED:
                progress.setCompletedModules(progress.getCompletedModules() + 1);
                break;
        }
        
        // Actualizar estado y porcentaje
        progress.updateProgress();
        progressRepository.save(progress);
    }
    
    private Skill getSkillFromActivity(LearningActivity activity) {
        // Lógica para determinar la habilidad basada en la entidad
        switch (activity.getEntityType()) {
            case "SKILL":
                return skillRepository.findById(activity.getEntityId()).orElse(null);
            case "MODULE":
                Optional<Module> module = moduleRepository.findById(activity.getEntityId());
                return module.map(Module::getSkill).orElse(null);
            // Para LESSON, LAB, QUIZ necesitaríamos navegar a través del módulo
            default:
                return null;
        }
    }
    
    private int calculateTotalPointsPossible(Skill skill) {
        // Sumar puntos de todos los labs y quizzes de la habilidad
        return labRepository.sumPointsBySkill(skill) + quizRepository.sumPointsBySkill(skill);
    }
    
    // Clase para estadísticas de progreso
    public static class UserProgressStats {
        private final int totalSkills;
        private final int completedSkills;
        private final int masteredSkills;
        private final int totalPointsEarned;
        private final double averageCompletion;
        private final int activeDays;
        private final LocalDateTime lastActivity;
        
        public UserProgressStats(int totalSkills, int completedSkills, int masteredSkills, 
                               int totalPointsEarned, double averageCompletion, 
                               int activeDays, LocalDateTime lastActivity) {
            this.totalSkills = totalSkills;
            this.completedSkills = completedSkills;
            this.masteredSkills = masteredSkills;
            this.totalPointsEarned = totalPointsEarned;
            this.averageCompletion = averageCompletion;
            this.activeDays = activeDays;
            this.lastActivity = lastActivity;
        }
        
        // Getters
        public int getTotalSkills() { return totalSkills; }
        public int getCompletedSkills() { return completedSkills; }
        public int getMasteredSkills() { return masteredSkills; }
        public int getTotalPointsEarned() { return totalPointsEarned; }
        public double getAverageCompletion() { return averageCompletion; }
        public int getActiveDays() { return activeDays; }
        public LocalDateTime getLastActivity() { return lastActivity; }
        
        public double getCompletionRate() {
            return totalSkills > 0 ? (double) completedSkills / totalSkills * 100 : 0;
        }
        
        public double getMasteryRate() {
            return totalSkills > 0 ? (double) masteredSkills / totalSkills * 100 : 0;
        }
    }
}
