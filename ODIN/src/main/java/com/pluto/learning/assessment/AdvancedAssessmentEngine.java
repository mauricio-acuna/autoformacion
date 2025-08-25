package com.pluto.learning.assessment;

import com.pluto.learning.auth.User;
import com.pluto.learning.assessments.Quiz;
import com.pluto.learning.submissions.LabSubmission;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Assessment Engine Avanzado para evaluación automática multi-dimensional
 * 
 * Características:
 * - Auto-grading con múltiples criterios
 * - Detección de plagio
 * - Análisis de performance en tiempo real
 * - Generación de feedback inteligente
 */
@Service
@Transactional
public class AdvancedAssessmentEngine {
    
    private final CodeAnalysisService codeAnalysisService;
    private final PlagiarismDetectionService plagiarismService;
    private final PerformanceAnalyzer performanceAnalyzer;
    private final FeedbackGenerator feedbackGenerator;
    
    public AdvancedAssessmentEngine(CodeAnalysisService codeAnalysisService,
                                  PlagiarismDetectionService plagiarismService,
                                  PerformanceAnalyzer performanceAnalyzer,
                                  FeedbackGenerator feedbackGenerator) {
        this.codeAnalysisService = codeAnalysisService;
        this.plagiarismService = plagiarismService;
        this.performanceAnalyzer = performanceAnalyzer;
        this.feedbackGenerator = feedbackGenerator;
    }
    
    /**
     * Evaluación automática completa de una submission
     */
    public AssessmentResult evaluateSubmission(LabSubmission submission) {
        AssessmentResult result = new AssessmentResult(submission);
        
        // 1. Análisis funcional (40%)
        FunctionalityScore functionality = evaluateFunctionality(submission);
        result.setFunctionalityScore(functionality);
        
        // 2. Análisis de calidad de código (25%)
        CodeQualityScore codeQuality = evaluateCodeQuality(submission);
        result.setCodeQualityScore(codeQuality);
        
        // 3. Análisis de arquitectura (15%)
        ArchitectureScore architecture = evaluateArchitecture(submission);
        result.setArchitectureScore(architecture);
        
        // 4. Análisis de resiliencia (15%)
        ResilienceScore resilience = evaluateResilience(submission);
        result.setResilienceScore(resilience);
        
        // 5. Análisis de operabilidad (5%)
        OperabilityScore operability = evaluateOperability(submission);
        result.setOperabilityScore(operability);
        
        // 6. Detección de plagio
        PlagiarismAnalysis plagiarism = plagiarismService.analyzeSubmission(submission);
        result.setPlagiarismAnalysis(plagiarism);
        
        // 7. Cálculo de score final
        result.calculateFinalScore();
        
        // 8. Generación de feedback
        String feedback = feedbackGenerator.generateFeedback(result);
        result.setFeedback(feedback);
        
        return result;
    }
    
    /**
     * Evaluación de funcionalidad mediante tests automatizados
     */
    private FunctionalityScore evaluateFunctionality(LabSubmission submission) {
        // Ejecutar tests automatizados
        TestExecutionResult testResult = codeAnalysisService.runAutomatedTests(submission);
        
        FunctionalityScore score = new FunctionalityScore();
        score.setTestsPassed(testResult.getPassedTests());
        score.setTestsTotal(testResult.getTotalTests());
        score.setTestCoverage(testResult.getCoverage());
        score.setEndpointsWorking(testResult.getWorkingEndpoints());
        score.setEndpointsTotal(testResult.getTotalEndpoints());
        
        // Cálculo del score (0-100)
        double testScore = (double) testResult.getPassedTests() / testResult.getTotalTests() * 70;
        double coverageScore = testResult.getCoverage() * 20;
        double endpointScore = (double) testResult.getWorkingEndpoints() / testResult.getTotalEndpoints() * 10;
        
        score.setScore((int) (testScore + coverageScore + endpointScore));
        return score;
    }
    
    /**
     * Evaluación de calidad de código
     */
    private CodeQualityScore evaluateCodeQuality(LabSubmission submission) {
        CodeAnalysisReport analysis = codeAnalysisService.analyzeCode(submission);
        
        CodeQualityScore score = new CodeQualityScore();
        score.setCodeCoverage(analysis.getTestCoverage());
        score.setComplexity(analysis.getCyclomaticComplexity());
        score.setDuplication(analysis.getCodeDuplication());
        score.setBugs(analysis.getBugCount());
        score.setVulnerabilities(analysis.getVulnerabilityCount());
        score.setCodeSmells(analysis.getCodeSmellCount());
        
        // Algoritmo de scoring basado en métricas
        int qualityScore = 100;
        qualityScore -= Math.min(30, analysis.getBugCount() * 5);
        qualityScore -= Math.min(20, analysis.getVulnerabilityCount() * 10);
        qualityScore -= Math.min(25, analysis.getCodeSmellCount() * 2);
        qualityScore -= Math.min(15, Math.max(0, (int)(analysis.getCodeDuplication() - 5) * 3));
        
        score.setScore(Math.max(0, qualityScore));
        return score;
    }
    
    /**
     * Evaluación de arquitectura y patrones de diseño
     */
    private ArchitectureScore evaluateArchitecture(LabSubmission submission) {
        ArchitectureAnalysis analysis = codeAnalysisService.analyzeArchitecture(submission);
        
        ArchitectureScore score = new ArchitectureScore();
        score.setDesignPatternsUsed(analysis.getDesignPatterns());
        score.setLayerSeparation(analysis.hasProperLayering());
        score.setDependencyManagement(analysis.hasManagedDependencies());
        score.setApiDesign(analysis.getApiDesignScore());
        
        // Scoring basado en buenas prácticas arquitectónicas
        int archScore = 0;
        archScore += analysis.getDesignPatterns().size() * 15; // Max 60 points
        archScore += analysis.hasProperLayering() ? 20 : 0;
        archScore += analysis.hasManagedDependencies() ? 15 : 0;
        archScore += Math.min(20, analysis.getApiDesignScore());
        
        score.setScore(Math.min(100, archScore));
        return score;
    }
    
    /**
     * Evaluación de patrones de resiliencia
     */
    private ResilienceScore evaluateResilience(LabSubmission submission) {
        ResilienceAnalysis analysis = codeAnalysisService.analyzeResilience(submission);
        
        ResilienceScore score = new ResilienceScore();
        score.setCircuitBreakerImplemented(analysis.hasCircuitBreaker());
        score.setRetryLogicImplemented(analysis.hasRetryLogic());
        score.setTimeoutHandling(analysis.hasTimeoutHandling());
        score.setErrorHandling(analysis.getErrorHandlingQuality());
        score.setFallbackMechanisms(analysis.hasFallbackMechanisms());
        
        // Scoring de resiliencia
        int resilienceScore = 0;
        resilienceScore += analysis.hasCircuitBreaker() ? 25 : 0;
        resilienceScore += analysis.hasRetryLogic() ? 20 : 0;
        resilienceScore += analysis.hasTimeoutHandling() ? 20 : 0;
        resilienceScore += analysis.hasFallbackMechanisms() ? 15 : 0;
        resilienceScore += Math.min(20, analysis.getErrorHandlingQuality());
        
        score.setScore(resilienceScore);
        return score;
    }
    
    /**
     * Evaluación de operabilidad (métricas, health checks, logs)
     */
    private OperabilityScore evaluateOperability(LabSubmission submission) {
        OperabilityAnalysis analysis = codeAnalysisService.analyzeOperability(submission);
        
        OperabilityScore score = new OperabilityScore();
        score.setMetricsExposed(analysis.hasMetrics());
        score.setHealthChecksImplemented(analysis.hasHealthChecks());
        score.setLoggingQuality(analysis.getLoggingQuality());
        score.setDocumentationQuality(analysis.getDocumentationScore());
        
        // Scoring de operabilidad
        int operScore = 0;
        operScore += analysis.hasMetrics() ? 30 : 0;
        operScore += analysis.hasHealthChecks() ? 25 : 0;
        operScore += Math.min(25, analysis.getLoggingQuality());
        operScore += Math.min(20, analysis.getDocumentationScore());
        
        score.setScore(operScore);
        return score;
    }
    
    /**
     * Evaluación de quiz con análisis de patrones de respuesta
     */
    public QuizAssessmentResult evaluateQuiz(User user, Quiz quiz, Map<Long, String> answers) {
        QuizAssessmentResult result = new QuizAssessmentResult(user, quiz);
        
        // Análisis de respuestas
        int correctAnswers = 0;
        int totalQuestions = quiz.getQuestions().size();
        
        for (var question : quiz.getQuestions()) {
            String userAnswer = answers.get(question.getId());
            boolean isCorrect = question.isAnswerCorrect(userAnswer);
            
            result.addQuestionResult(question.getId(), userAnswer, isCorrect);
            if (isCorrect) correctAnswers++;
        }
        
        // Cálculo de score
        double scorePercentage = (double) correctAnswers / totalQuestions * 100;
        result.setScore((int) scorePercentage);
        result.setPassed(scorePercentage >= 75); // 75% para aprobar
        
        // Análisis de patrones (tiempo, dificultad, áreas débiles)
        performanceAnalyzer.analyzeQuizPatterns(result);
        
        return result;
    }
}
