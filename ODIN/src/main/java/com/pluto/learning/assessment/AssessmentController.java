package com.pluto.learning.assessment;

import com.pluto.learning.auth.User;
import com.pluto.learning.auth.UserService;
import com.pluto.learning.assessments.Quiz;
import com.pluto.learning.assessments.QuizRepository;
import com.pluto.learning.submissions.LabSubmission;
import com.pluto.learning.submissions.SubmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;
import java.util.Optional;

/**
 * Controlador REST para el Assessment Engine
 */
@RestController
@RequestMapping("/api/assessment")
@CrossOrigin(origins = "*")
public class AssessmentController {
    
    @Autowired
    private AdvancedAssessmentEngine assessmentEngine;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private SubmissionRepository submissionRepository;
    
    @Autowired
    private QuizRepository quizRepository;
    
    /**
     * Evalúa una submission de código
     */
    @PostMapping("/evaluate-submission/{submissionId}")
    public ResponseEntity<?> evaluateSubmission(@PathVariable Long submissionId, Principal principal) {
        try {
            // Verificar que la submission existe y pertenece al usuario
            Optional<LabSubmission> submissionOpt = submissionRepository.findById(submissionId);
            if (submissionOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            
            LabSubmission submission = submissionOpt.get();
            User currentUser = userService.getCurrentUser(
                org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication()
            );
            
            if (!submission.getUser().getId().equals(currentUser.getId())) {
                return ResponseEntity.status(403).build();
            }
            
            // Evaluar la submission
            AssessmentResult result = assessmentEngine.evaluateSubmission(submission);
            
            // Actualizar la submission con los resultados
            submission.setAutomatedScore(result.getFinalScore());
            submission.setFinalScore(result.getFinalScore());
            submission.setInstructorFeedback(result.getFeedback());
            submission.setEvaluatedAt(result.getEvaluationDate());
            submission.setStatus(result.isPassed() ? 
                LabSubmission.SubmissionStatus.EVALUATED : 
                LabSubmission.SubmissionStatus.REVIEW_PENDING);
            
            submissionRepository.save(submission);
            
            return ResponseEntity.ok(new AssessmentResponse(
                result.getFinalScore(),
                result.isPassed(),
                result.getFeedback(),
                createDetailedBreakdown(result)
            ));
            
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Assessment failed: " + e.getMessage()));
        }
    }
    
    /**
     * Evalúa un quiz completado
     */
    @PostMapping("/evaluate-quiz/{quizId}")
    public ResponseEntity<?> evaluateQuiz(
            @PathVariable Long quizId,
            @RequestBody Map<Long, String> answers,
            Principal principal) {
        
        try {
            // Verificar que el quiz existe
            Optional<Quiz> quizOpt = quizRepository.findById(quizId);
            if (quizOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            
            Quiz quiz = quizOpt.get();
            User currentUser = userService.getCurrentUser(
                org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication()
            );
            
            // Evaluar el quiz
            QuizAssessmentResult result = assessmentEngine.evaluateQuiz(currentUser, quiz, answers);
            
            return ResponseEntity.ok(new QuizAssessmentResponse(
                result.getScore(),
                result.isPassed(),
                result.getQuestionResults().size(),
                (int) result.getQuestionResults().values().stream()
                    .filter(qr -> qr.isCorrect())
                    .count(),
                result.getAnalysisNotes()
            ));
            
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Quiz evaluation failed: " + e.getMessage()));
        }
    }
    
    /**
     * Obtiene el historial de evaluaciones de un usuario
     */
    @GetMapping("/history")
    public ResponseEntity<?> getAssessmentHistory(Principal principal) {
        try {
            User currentUser = userService.getCurrentUser(
                org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication()
            );
            
            // Simplificado: retornar mensaje temporal
            return ResponseEntity.ok(Map.of(
                "message", "Assessment history functionality coming soon",
                "userId", currentUser.getId()
            ));
            
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Failed to fetch history: " + e.getMessage()));
        }
    }
    
    /**
     * Obtiene estadísticas detalladas de evaluación
     */
    @GetMapping("/stats")
    public ResponseEntity<?> getAssessmentStats(Principal principal) {
        try {
            User currentUser = userService.getCurrentUser(
                org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication()
            );
            
            // Mock stats para demostración
            return ResponseEntity.ok(Map.of(
                "totalSubmissions", 5,
                "gradedSubmissions", 4,
                "passedSubmissions", 3,
                "passRate", 75.0,
                "averageScore", 82.5,
                "userId", currentUser.getId()
            ));
            
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Failed to fetch stats: " + e.getMessage()));
        }
    }
    
    private Map<String, Object> createDetailedBreakdown(AssessmentResult result) {
        Map<String, Object> breakdown = new java.util.HashMap<>();
        
        if (result.getFunctionalityScore() != null) {
            breakdown.put("functionality", Map.of(
                "score", result.getFunctionalityScore().getScore(),
                "weight", "40%",
                "testsPassed", result.getFunctionalityScore().getTestsPassed(),
                "testsTotal", result.getFunctionalityScore().getTestsTotal()
            ));
        }
        
        if (result.getCodeQualityScore() != null) {
            breakdown.put("codeQuality", Map.of(
                "score", result.getCodeQualityScore().getScore(),
                "weight", "25%",
                "bugs", result.getCodeQualityScore().getBugs(),
                "vulnerabilities", result.getCodeQualityScore().getVulnerabilities()
            ));
        }
        
        if (result.getArchitectureScore() != null) {
            breakdown.put("architecture", Map.of(
                "score", result.getArchitectureScore().getScore(),
                "weight", "15%",
                "patternsUsed", result.getArchitectureScore().getDesignPatternsUsed()
            ));
        }
        
        if (result.getResilienceScore() != null) {
            breakdown.put("resilience", Map.of(
                "score", result.getResilienceScore().getScore(),
                "weight", "15%"
            ));
        }
        
        if (result.getOperabilityScore() != null) {
            breakdown.put("operability", Map.of(
                "score", result.getOperabilityScore().getScore(),
                "weight", "5%"
            ));
        }
        
        return breakdown;
    }
}

/**
 * Response DTOs
 */
class AssessmentResponse {
    private final int finalScore;
    private final boolean passed;
    private final String feedback;
    private final Map<String, Object> breakdown;
    
    public AssessmentResponse(int finalScore, boolean passed, String feedback, Map<String, Object> breakdown) {
        this.finalScore = finalScore;
        this.passed = passed;
        this.feedback = feedback;
        this.breakdown = breakdown;
    }
    
    public int getFinalScore() { return finalScore; }
    public boolean isPassed() { return passed; }
    public String getFeedback() { return feedback; }
    public Map<String, Object> getBreakdown() { return breakdown; }
}

class QuizAssessmentResponse {
    private final int score;
    private final boolean passed;
    private final int totalQuestions;
    private final int correctAnswers;
    private final Map<String, String> analysis;
    
    public QuizAssessmentResponse(int score, boolean passed, int totalQuestions, 
                                 int correctAnswers, Map<String, String> analysis) {
        this.score = score;
        this.passed = passed;
        this.totalQuestions = totalQuestions;
        this.correctAnswers = correctAnswers;
        this.analysis = analysis;
    }
    
    public int getScore() { return score; }
    public boolean isPassed() { return passed; }
    public int getTotalQuestions() { return totalQuestions; }
    public int getCorrectAnswers() { return correctAnswers; }
    public Map<String, String> getAnalysis() { return analysis; }
}
