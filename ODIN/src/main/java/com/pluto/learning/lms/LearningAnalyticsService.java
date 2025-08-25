package com.pluto.learning.lms;

import com.pluto.learning.auth.User;
import com.pluto.learning.content.Skill;
import com.pluto.learning.progress.LearningActivity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Servicio de Analytics para análisis de patrones de aprendizaje
 */
@Service
public class LearningAnalyticsService {
    
    public AdaptiveLearningService.LearningPerformance calculateUserPerformance(User user, Skill skill) {
        // Mock implementation - en producción esto consultaría la base de datos
        return new AdaptiveLearningService.LearningPerformance(
            0.75, // 75% success rate
            15.5, // 15.5 minutes average
            10,   // 10 attempts
            Map.of("theory", 0.8, "practice", 0.7, "labs", 0.6)
        );
    }
    
    public SkillGapAnalysis analyzeSkillGaps(User user, Skill skill) {
        // Mock implementation
        return new SkillGapAnalysis(skill.getName(), 0.6, List.of("advanced-patterns", "performance-tuning"));
    }
    
    public LearningPatterns analyzeRecentPatterns(List<LearningActivity> recentActivities) {
        // Análisis de patrones en actividades recientes
        int consecutiveFailures = 0;
        int consecutiveSuccesses = 0;
        
        // Simulación básica de análisis
        if (recentActivities.size() > 3) {
            consecutiveSuccesses = 2;
        }
        
        return new LearningPatterns(consecutiveFailures, consecutiveSuccesses, List.of("pattern-matching"));
    }
}

/**
 * Análisis de gaps en skills
 */
class SkillGapAnalysis {
    private final String skillName;
    private final double currentLevel;
    private final List<String> missingAreas;
    
    public SkillGapAnalysis(String skillName, double currentLevel, List<String> missingAreas) {
        this.skillName = skillName;
        this.currentLevel = currentLevel;
        this.missingAreas = missingAreas;
    }
    
    public String getSkillName() { return skillName; }
    public double getCurrentLevel() { return currentLevel; }
    public List<String> getMissingAreas() { return missingAreas; }
}

/**
 * Patrones de aprendizaje detectados
 */
class LearningPatterns {
    private final int consecutiveFailures;
    private final int consecutiveSuccesses;
    private final List<String> weakAreas;
    
    public LearningPatterns(int consecutiveFailures, int consecutiveSuccesses, List<String> weakAreas) {
        this.consecutiveFailures = consecutiveFailures;
        this.consecutiveSuccesses = consecutiveSuccesses;
        this.weakAreas = weakAreas;
    }
    
    public int getConsecutiveFailures() { return consecutiveFailures; }
    public int getConsecutiveSuccesses() { return consecutiveSuccesses; }
    public List<String> getWeakAreas() { return weakAreas; }
}
