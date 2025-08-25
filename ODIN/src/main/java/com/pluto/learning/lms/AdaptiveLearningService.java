package com.pluto.learning.lms;

import com.pluto.learning.auth.User;
import com.pluto.learning.content.Skill;
import com.pluto.learning.progress.LearningActivity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Adaptive Learning Service - Ajusta la dificultad y contenido según el rendimiento del usuario
 * 
 * Características:
 * - Algoritmos de adaptación de dificultad
 * - Recomendación de contenido personalizado
 * - Análisis de patrones de aprendizaje
 * - Identificación de gaps de conocimiento
 */
@Service
@Transactional
public class AdaptiveLearningService {
    
    private final LearningAnalyticsService analyticsService;
    private final SkillRecommendationEngine recommendationEngine;
    
    public AdaptiveLearningService(LearningAnalyticsService analyticsService, 
                                 SkillRecommendationEngine recommendationEngine) {
        this.analyticsService = analyticsService;
        this.recommendationEngine = recommendationEngine;
    }
    
    /**
     * Calcula el nivel de dificultad adaptativo para un usuario y skill
     */
    public DifficultyLevel calculateAdaptiveDifficulty(User user, Skill skill) {
        // Obtener historial de performance
        LearningPerformance performance = analyticsService.calculateUserPerformance(user, skill);
        
        // Algoritmo de adaptación basado en performance
        if (performance.getSuccessRate() >= 0.85) {
            return DifficultyLevel.ADVANCED;
        } else if (performance.getSuccessRate() >= 0.70) {
            return DifficultyLevel.INTERMEDIATE;
        } else if (performance.getSuccessRate() >= 0.50) {
            return DifficultyLevel.BEGINNER;
        } else {
            return DifficultyLevel.FOUNDATION;
        }
    }
    
    /**
     * Genera ruta de aprendizaje personalizada
     */
    public PersonalizedLearningPath generateLearningPath(User user, List<Skill> targetSkills) {
        PersonalizedLearningPath path = new PersonalizedLearningPath(user);
        
        for (Skill skill : targetSkills) {
            // Analizar conocimiento actual
            SkillGapAnalysis gapAnalysis = analyticsService.analyzeSkillGaps(user, skill);
            
            // Generar recomendaciones
            List<LearningRecommendation> recommendations = recommendationEngine
                .generateRecommendations(user, skill, gapAnalysis);
            
            path.addSkillPath(skill, recommendations);
        }
        
        return path;
    }
    
    /**
     * Adapta el contenido en tiempo real basado en respuestas
     */
    public ContentAdaptation adaptContentRealTime(User user, Skill skill, 
                                                 List<LearningActivity> recentActivities) {
        // Analizar patrones recientes
        LearningPatterns patterns = analyticsService.analyzeRecentPatterns(recentActivities);
        
        ContentAdaptation adaptation = new ContentAdaptation();
        
        // Si el usuario está struggling, simplificar
        if (patterns.getConsecutiveFailures() >= 3) {
            adaptation.setDifficultyReduction(true);
            adaptation.addSupportResource("fundamentals-review");
            adaptation.setSuggestedBreak(true);
        }
        
        // Si el usuario está dominando, incrementar dificultad
        if (patterns.getConsecutiveSuccesses() >= 5) {
            adaptation.setDifficultyIncrease(true);
            adaptation.addChallengeResource("advanced-topics");
        }
        
        // Recomendar contenido complementario
        if (patterns.getWeakAreas().size() > 0) {
            patterns.getWeakAreas().forEach(area -> 
                adaptation.addRecommendedContent(area, "reinforcement"));
        }
        
        return adaptation;
    }
    
    /**
     * Niveles de dificultad adaptativos
     */
    public enum DifficultyLevel {
        FOUNDATION(1, "Conceptos básicos y fundamentos"),
        BEGINNER(2, "Conocimiento introductorio"),
        INTERMEDIATE(3, "Aplicación práctica"),
        ADVANCED(4, "Dominio avanzado"),
        EXPERT(5, "Maestría y optimización");
        
        private final int level;
        private final String description;
        
        DifficultyLevel(int level, String description) {
            this.level = level;
            this.description = description;
        }
        
        public int getLevel() { return level; }
        public String getDescription() { return description; }
    }
    
    /**
     * Clase interna para análisis de performance
     */
    public static class LearningPerformance {
        private final double successRate;
        private final double averageTime;
        private final int totalAttempts;
        private final Map<String, Double> skillBreakdown;
        
        public LearningPerformance(double successRate, double averageTime, 
                                 int totalAttempts, Map<String, Double> skillBreakdown) {
            this.successRate = successRate;
            this.averageTime = averageTime;
            this.totalAttempts = totalAttempts;
            this.skillBreakdown = skillBreakdown;
        }
        
        public double getSuccessRate() { return successRate; }
        public double getAverageTime() { return averageTime; }
        public int getTotalAttempts() { return totalAttempts; }
        public Map<String, Double> getSkillBreakdown() { return skillBreakdown; }
    }
    
    /**
     * Clase para recomendaciones de contenido
     */
    public static class LearningRecommendation {
        private final String contentType;
        private final String contentId;
        private final String title;
        private final DifficultyLevel difficulty;
        private final double relevanceScore;
        private final String reasoning;
        
        public LearningRecommendation(String contentType, String contentId, String title,
                                    DifficultyLevel difficulty, double relevanceScore, String reasoning) {
            this.contentType = contentType;
            this.contentId = contentId;
            this.title = title;
            this.difficulty = difficulty;
            this.relevanceScore = relevanceScore;
            this.reasoning = reasoning;
        }
        
        // Getters
        public String getContentType() { return contentType; }
        public String getContentId() { return contentId; }
        public String getTitle() { return title; }
        public DifficultyLevel getDifficulty() { return difficulty; }
        public double getRelevanceScore() { return relevanceScore; }
        public String getReasoning() { return reasoning; }
    }
    
    /**
     * Clase para adaptación de contenido en tiempo real
     */
    public static class ContentAdaptation {
        private boolean difficultyIncrease = false;
        private boolean difficultyReduction = false;
        private boolean suggestedBreak = false;
        private List<String> supportResources = new java.util.ArrayList<>();
        private List<String> challengeResources = new java.util.ArrayList<>();
        private Map<String, String> recommendedContent = new java.util.HashMap<>();
        
        // Getters and setters
        public boolean isDifficultyIncrease() { return difficultyIncrease; }
        public void setDifficultyIncrease(boolean difficultyIncrease) { this.difficultyIncrease = difficultyIncrease; }
        
        public boolean isDifficultyReduction() { return difficultyReduction; }
        public void setDifficultyReduction(boolean difficultyReduction) { this.difficultyReduction = difficultyReduction; }
        
        public boolean isSuggestedBreak() { return suggestedBreak; }
        public void setSuggestedBreak(boolean suggestedBreak) { this.suggestedBreak = suggestedBreak; }
        
        public void addSupportResource(String resource) { supportResources.add(resource); }
        public void addChallengeResource(String resource) { challengeResources.add(resource); }
        public void addRecommendedContent(String area, String contentType) { 
            recommendedContent.put(area, contentType); 
        }
        
        public List<String> getSupportResources() { return supportResources; }
        public List<String> getChallengeResources() { return challengeResources; }
        public Map<String, String> getRecommendedContent() { return recommendedContent; }
    }
}
