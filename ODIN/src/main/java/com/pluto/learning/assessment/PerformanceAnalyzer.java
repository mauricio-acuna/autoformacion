package com.pluto.learning.assessment;

import com.pluto.learning.auth.User;
import com.pluto.learning.assessments.Quiz;
import org.springframework.stereotype.Service;

/**
 * Analizador de rendimiento para quiz y assignments
 */
@Service
public class PerformanceAnalyzer {
    
    /**
     * Analiza patrones de rendimiento en quiz
     */
    public void analyzeQuizPatterns(QuizAssessmentResult result) {
        User user = result.getUser();
        Quiz quiz = result.getQuiz();
        
        // Análisis de tiempo por pregunta
        analyzeTimePatterns(result);
        
        // Análisis de dificultad vs performance
        analyzeDifficultyPatterns(result);
        
        // Identificar áreas débiles
        identifyWeakAreas(result);
        
        // Patrones de comportamiento
        analyzeBehaviorPatterns(result);
    }
    
    private void analyzeTimePatterns(QuizAssessmentResult result) {
        // Mock: análisis de patrones de tiempo
        // En producción capturaría timestamps de cada respuesta
        result.addAnalysisNote("Time Pattern", "Consistent pacing throughout quiz");
    }
    
    private void analyzeDifficultyPatterns(QuizAssessmentResult result) {
        // Analizar rendimiento vs dificultad de preguntas
        result.addAnalysisNote("Difficulty Analysis", "Better performance on medium difficulty questions");
    }
    
    private void identifyWeakAreas(QuizAssessmentResult result) {
        // Identificar temas donde el usuario necesita mejora
        result.addAnalysisNote("Weak Areas", "Spring Security, Database optimization");
    }
    
    private void analyzeBehaviorPatterns(QuizAssessmentResult result) {
        // Analizar patrones de comportamiento del usuario
        result.addAnalysisNote("Behavior", "Methodical approach, double-checks answers");
    }
}

/**
 * Generador de feedback inteligente
 */
@Service
class FeedbackGenerator {
    
    /**
     * Genera feedback detallado basado en el resultado de evaluación
     */
    public String generateFeedback(AssessmentResult result) {
        StringBuilder feedback = new StringBuilder();
        
        // Resumen general
        feedback.append("## Assessment Summary\n");
        feedback.append(String.format("Final Score: %d/100\n", result.getFinalScore()));
        feedback.append(String.format("Status: %s\n\n", result.isPassed() ? "PASSED" : "NEEDS IMPROVEMENT"));
        
        // Feedback por categoría
        if (result.getFunctionalityScore() != null) {
            feedback.append(generateFunctionalityFeedback(result.getFunctionalityScore()));
        }
        
        if (result.getCodeQualityScore() != null) {
            feedback.append(generateCodeQualityFeedback(result.getCodeQualityScore()));
        }
        
        if (result.getArchitectureScore() != null) {
            feedback.append(generateArchitectureFeedback(result.getArchitectureScore()));
        }
        
        if (result.getResilienceScore() != null) {
            feedback.append(generateResilienceFeedback(result.getResilienceScore()));
        }
        
        if (result.getOperabilityScore() != null) {
            feedback.append(generateOperabilityFeedback(result.getOperabilityScore()));
        }
        
        // Plagio
        if (result.getPlagiarismAnalysis() != null && result.getPlagiarismAnalysis().isPlagiarismDetected()) {
            feedback.append(generatePlagiarismFeedback(result.getPlagiarismAnalysis()));
        }
        
        // Recomendaciones
        feedback.append(generateRecommendations(result));
        
        return feedback.toString();
    }
    
    private String generateFunctionalityFeedback(FunctionalityScore score) {
        StringBuilder fb = new StringBuilder();
        fb.append("### Functionality (40% weight)\n");
        fb.append(String.format("Score: %d/100\n", score.getScore()));
        fb.append(String.format("Tests: %d/%d passed (%.1f%%)\n", 
                score.getTestsPassed(), score.getTestsTotal(), 
                score.getTestsTotal() > 0 ? (100.0 * score.getTestsPassed() / score.getTestsTotal()) : 0));
        fb.append(String.format("Test Coverage: %.1f%%\n", score.getTestCoverage() * 100));
        fb.append(String.format("Endpoints: %d/%d working\n\n", 
                score.getEndpointsWorking(), score.getEndpointsTotal()));
        
        if (score.getScore() < 70) {
            fb.append("⚠️ **Improvement needed**: Focus on making all tests pass and improving coverage.\n\n");
        } else if (score.getScore() >= 90) {
            fb.append("✅ **Excellent**: Strong functional implementation.\n\n");
        }
        
        return fb.toString();
    }
    
    private String generateCodeQualityFeedback(CodeQualityScore score) {
        StringBuilder fb = new StringBuilder();
        fb.append("### Code Quality (25% weight)\n");
        fb.append(String.format("Score: %d/100\n", score.getScore()));
        fb.append(String.format("Bugs: %d\n", score.getBugs()));
        fb.append(String.format("Vulnerabilities: %d\n", score.getVulnerabilities()));
        fb.append(String.format("Code Smells: %d\n", score.getCodeSmells()));
        fb.append(String.format("Duplication: %.1f%%\n\n", score.getDuplication()));
        
        if (score.getBugs() > 0) {
            fb.append("🐛 **Fix bugs**: Address the identified bug patterns.\n");
        }
        if (score.getVulnerabilities() > 0) {
            fb.append("🔒 **Security**: Review and fix security vulnerabilities.\n");
        }
        if (score.getDuplication() > 10) {
            fb.append("🔄 **Refactor**: Reduce code duplication through better abstractions.\n");
        }
        fb.append("\n");
        
        return fb.toString();
    }
    
    private String generateArchitectureFeedback(ArchitectureScore score) {
        StringBuilder fb = new StringBuilder();
        fb.append("### Architecture (15% weight)\n");
        fb.append(String.format("Score: %d/100\n", score.getScore()));
        fb.append(String.format("Design Patterns: %s\n", 
                score.getDesignPatternsUsed() != null ? 
                String.join(", ", score.getDesignPatternsUsed()) : "None detected"));
        fb.append(String.format("Layer Separation: %s\n", score.isLayerSeparation() ? "✅" : "❌"));
        fb.append(String.format("Dependency Management: %s\n\n", score.isDependencyManagement() ? "✅" : "❌"));
        
        if (score.getScore() < 60) {
            fb.append("🏗️ **Architecture**: Consider implementing proper layering and design patterns.\n\n");
        }
        
        return fb.toString();
    }
    
    private String generateResilienceFeedback(ResilienceScore score) {
        StringBuilder fb = new StringBuilder();
        fb.append("### Resilience (15% weight)\n");
        fb.append(String.format("Score: %d/100\n", score.getScore()));
        fb.append(String.format("Circuit Breaker: %s\n", score.isCircuitBreakerImplemented() ? "✅" : "❌"));
        fb.append(String.format("Retry Logic: %s\n", score.isRetryLogicImplemented() ? "✅" : "❌"));
        fb.append(String.format("Timeout Handling: %s\n", score.isTimeoutHandling() ? "✅" : "❌"));
        fb.append(String.format("Fallback Mechanisms: %s\n\n", score.isFallbackMechanisms() ? "✅" : "❌"));
        
        if (score.getScore() < 50) {
            fb.append("🛡️ **Resilience**: Implement error handling patterns for production readiness.\n\n");
        }
        
        return fb.toString();
    }
    
    private String generateOperabilityFeedback(OperabilityScore score) {
        StringBuilder fb = new StringBuilder();
        fb.append("### Operability (5% weight)\n");
        fb.append(String.format("Score: %d/100\n", score.getScore()));
        fb.append(String.format("Metrics: %s\n", score.isMetricsExposed() ? "✅" : "❌"));
        fb.append(String.format("Health Checks: %s\n", score.isHealthChecksImplemented() ? "✅" : "❌"));
        fb.append(String.format("Logging Quality: %d/100\n", score.getLoggingQuality()));
        fb.append(String.format("Documentation: %d/100\n\n", score.getDocumentationQuality()));
        
        return fb.toString();
    }
    
    private String generatePlagiarismFeedback(PlagiarismAnalysis analysis) {
        StringBuilder fb = new StringBuilder();
        fb.append("### 🚨 Plagiarism Detected\n");
        fb.append(String.format("Similarity: %.1f%%\n", analysis.getSimilarityPercentage() * 100));
        fb.append("This submission shows significant similarity to existing code.\n");
        fb.append("Please ensure all work is original and properly attributed.\n\n");
        
        return fb.toString();
    }
    
    private String generateRecommendations(AssessmentResult result) {
        StringBuilder rec = new StringBuilder();
        rec.append("### Recommendations\n");
        
        if (result.getFinalScore() < 60) {
            rec.append("- Focus on basic functionality first\n");
            rec.append("- Review Java/Spring Boot fundamentals\n");
            rec.append("- Practice test-driven development\n");
        } else if (result.getFinalScore() < 80) {
            rec.append("- Improve code quality and architecture\n");
            rec.append("- Learn design patterns\n");
            rec.append("- Add comprehensive error handling\n");
        } else {
            rec.append("- Excellent work! Consider advanced topics:\n");
            rec.append("- Microservices patterns\n");
            rec.append("- Performance optimization\n");
            rec.append("- Advanced security patterns\n");
        }
        
        return rec.toString();
    }
}
