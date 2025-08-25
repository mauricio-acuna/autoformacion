package com.pluto.learning.lms;

import com.pluto.learning.auth.User;
import com.pluto.learning.content.Skill;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.ArrayList;

/**
 * Motor de recomendaciones basado en Machine Learning patterns
 */
@Component
public class SkillRecommendationEngine {
    
    public List<AdaptiveLearningService.LearningRecommendation> generateRecommendations(
            User user, Skill skill, SkillGapAnalysis gapAnalysis) {
        
        List<AdaptiveLearningService.LearningRecommendation> recommendations = new ArrayList<>();
        
        // Recomendaciones basadas en gaps detectados
        for (String missingArea : gapAnalysis.getMissingAreas()) {
            recommendations.add(new AdaptiveLearningService.LearningRecommendation(
                "lesson",
                "lesson-" + missingArea,
                "Fundamentals: " + missingArea,
                AdaptiveLearningService.DifficultyLevel.BEGINNER,
                0.85,
                "Gap detected in " + missingArea
            ));
        }
        
        // Agregar lab práctico
        recommendations.add(new AdaptiveLearningService.LearningRecommendation(
            "lab",
            "lab-" + skill.getName().toLowerCase(),
            "Hands-on Lab: " + skill.getName(),
            AdaptiveLearningService.DifficultyLevel.INTERMEDIATE,
            0.90,
            "Practical application for " + skill.getName()
        ));
        
        return recommendations;
    }
}

/**
 * Ruta de aprendizaje personalizada
 */
class PersonalizedLearningPath {
    private final User user;
    private final List<SkillPath> skillPaths;
    
    public PersonalizedLearningPath(User user) {
        this.user = user;
        this.skillPaths = new ArrayList<>();
    }
    
    public void addSkillPath(Skill skill, List<AdaptiveLearningService.LearningRecommendation> recommendations) {
        skillPaths.add(new SkillPath(skill, recommendations));
    }
    
    public User getUser() { return user; }
    public List<SkillPath> getSkillPaths() { return skillPaths; }
    
    public static class SkillPath {
        private final Skill skill;
        private final List<AdaptiveLearningService.LearningRecommendation> recommendations;
        
        public SkillPath(Skill skill, List<AdaptiveLearningService.LearningRecommendation> recommendations) {
            this.skill = skill;
            this.recommendations = recommendations;
        }
        
        public Skill getSkill() { return skill; }
        public List<AdaptiveLearningService.LearningRecommendation> getRecommendations() { return recommendations; }
    }
}
