package com.pluto.learning.assessment;

import com.pluto.learning.submissions.LabSubmission;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.HashMap;

/**
 * Resultado completo de evaluación de una submission
 */
public class AssessmentResult {
    
    private final LabSubmission submission;
    private final LocalDateTime evaluationDate;
    private final Map<String, Object> metrics;
    
    // Scores por categoría
    private FunctionalityScore functionalityScore;
    private CodeQualityScore codeQualityScore;
    private ArchitectureScore architectureScore;
    private ResilienceScore resilienceScore;
    private OperabilityScore operabilityScore;
    
    // Análisis adicionales
    private PlagiarismAnalysis plagiarismAnalysis;
    private String feedback;
    
    // Score final
    private int finalScore;
    private boolean passed;
    
    public AssessmentResult(LabSubmission submission) {
        this.submission = submission;
        this.evaluationDate = LocalDateTime.now();
        this.metrics = new HashMap<>();
    }
    
    /**
     * Calcula el score final basado en los weights de cada categoría
     */
    public void calculateFinalScore() {
        int total = 0;
        
        if (functionalityScore != null) {
            total += functionalityScore.getScore() * 0.40; // 40%
        }
        if (codeQualityScore != null) {
            total += codeQualityScore.getScore() * 0.25; // 25%
        }
        if (architectureScore != null) {
            total += architectureScore.getScore() * 0.15; // 15%
        }
        if (resilienceScore != null) {
            total += resilienceScore.getScore() * 0.15; // 15%
        }
        if (operabilityScore != null) {
            total += operabilityScore.getScore() * 0.05; // 5%
        }
        
        this.finalScore = total;
        this.passed = total >= 75; // 75% para aprobar
        
        // Penalty por plagio
        if (plagiarismAnalysis != null && plagiarismAnalysis.isPlagiarismDetected()) {
            this.finalScore = Math.max(0, this.finalScore - 50);
            this.passed = false;
        }
    }
    
    // Getters and Setters
    public LabSubmission getSubmission() { return submission; }
    public LocalDateTime getEvaluationDate() { return evaluationDate; }
    
    public FunctionalityScore getFunctionalityScore() { return functionalityScore; }
    public void setFunctionalityScore(FunctionalityScore functionalityScore) { 
        this.functionalityScore = functionalityScore; 
    }
    
    public CodeQualityScore getCodeQualityScore() { return codeQualityScore; }
    public void setCodeQualityScore(CodeQualityScore codeQualityScore) { 
        this.codeQualityScore = codeQualityScore; 
    }
    
    public ArchitectureScore getArchitectureScore() { return architectureScore; }
    public void setArchitectureScore(ArchitectureScore architectureScore) { 
        this.architectureScore = architectureScore; 
    }
    
    public ResilienceScore getResilienceScore() { return resilienceScore; }
    public void setResilienceScore(ResilienceScore resilienceScore) { 
        this.resilienceScore = resilienceScore; 
    }
    
    public OperabilityScore getOperabilityScore() { return operabilityScore; }
    public void setOperabilityScore(OperabilityScore operabilityScore) { 
        this.operabilityScore = operabilityScore; 
    }
    
    public PlagiarismAnalysis getPlagiarismAnalysis() { return plagiarismAnalysis; }
    public void setPlagiarismAnalysis(PlagiarismAnalysis plagiarismAnalysis) { 
        this.plagiarismAnalysis = plagiarismAnalysis; 
    }
    
    public String getFeedback() { return feedback; }
    public void setFeedback(String feedback) { this.feedback = feedback; }
    
    public int getFinalScore() { return finalScore; }
    public boolean isPassed() { return passed; }
    
    public Map<String, Object> getMetrics() { return metrics; }
    public void addMetric(String key, Object value) { this.metrics.put(key, value); }
}
