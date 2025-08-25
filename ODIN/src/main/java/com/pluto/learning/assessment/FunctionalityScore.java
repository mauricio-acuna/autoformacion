package com.pluto.learning.assessment;

/**
 * Score de funcionalidad basado en tests automatizados
 */
public class FunctionalityScore {
    private int score;
    private int testsPassed;
    private int testsTotal;
    private double testCoverage;
    private int endpointsWorking;
    private int endpointsTotal;
    
    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }
    
    public int getTestsPassed() { return testsPassed; }
    public void setTestsPassed(int testsPassed) { this.testsPassed = testsPassed; }
    
    public int getTestsTotal() { return testsTotal; }
    public void setTestsTotal(int testsTotal) { this.testsTotal = testsTotal; }
    
    public double getTestCoverage() { return testCoverage; }
    public void setTestCoverage(double testCoverage) { this.testCoverage = testCoverage; }
    
    public int getEndpointsWorking() { return endpointsWorking; }
    public void setEndpointsWorking(int endpointsWorking) { this.endpointsWorking = endpointsWorking; }
    
    public int getEndpointsTotal() { return endpointsTotal; }
    public void setEndpointsTotal(int endpointsTotal) { this.endpointsTotal = endpointsTotal; }
}

/**
 * Score de calidad de código
 */
class CodeQualityScore {
    private int score;
    private double codeCoverage;
    private int complexity;
    private double duplication;
    private int bugs;
    private int vulnerabilities;
    private int codeSmells;
    
    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }
    
    public double getCodeCoverage() { return codeCoverage; }
    public void setCodeCoverage(double codeCoverage) { this.codeCoverage = codeCoverage; }
    
    public int getComplexity() { return complexity; }
    public void setComplexity(int complexity) { this.complexity = complexity; }
    
    public double getDuplication() { return duplication; }
    public void setDuplication(double duplication) { this.duplication = duplication; }
    
    public int getBugs() { return bugs; }
    public void setBugs(int bugs) { this.bugs = bugs; }
    
    public int getVulnerabilities() { return vulnerabilities; }
    public void setVulnerabilities(int vulnerabilities) { this.vulnerabilities = vulnerabilities; }
    
    public int getCodeSmells() { return codeSmells; }
    public void setCodeSmells(int codeSmells) { this.codeSmells = codeSmells; }
}

/**
 * Score de arquitectura
 */
class ArchitectureScore {
    private int score;
    private java.util.List<String> designPatternsUsed;
    private boolean layerSeparation;
    private boolean dependencyManagement;
    private int apiDesign;
    
    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }
    
    public java.util.List<String> getDesignPatternsUsed() { return designPatternsUsed; }
    public void setDesignPatternsUsed(java.util.List<String> designPatternsUsed) { 
        this.designPatternsUsed = designPatternsUsed; 
    }
    
    public boolean isLayerSeparation() { return layerSeparation; }
    public void setLayerSeparation(boolean layerSeparation) { this.layerSeparation = layerSeparation; }
    
    public boolean isDependencyManagement() { return dependencyManagement; }
    public void setDependencyManagement(boolean dependencyManagement) { 
        this.dependencyManagement = dependencyManagement; 
    }
    
    public int getApiDesign() { return apiDesign; }
    public void setApiDesign(int apiDesign) { this.apiDesign = apiDesign; }
}

/**
 * Score de resiliencia
 */
class ResilienceScore {
    private int score;
    private boolean circuitBreakerImplemented;
    private boolean retryLogicImplemented;
    private boolean timeoutHandling;
    private int errorHandling;
    private boolean fallbackMechanisms;
    
    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }
    
    public boolean isCircuitBreakerImplemented() { return circuitBreakerImplemented; }
    public void setCircuitBreakerImplemented(boolean circuitBreakerImplemented) { 
        this.circuitBreakerImplemented = circuitBreakerImplemented; 
    }
    
    public boolean isRetryLogicImplemented() { return retryLogicImplemented; }
    public void setRetryLogicImplemented(boolean retryLogicImplemented) { 
        this.retryLogicImplemented = retryLogicImplemented; 
    }
    
    public boolean isTimeoutHandling() { return timeoutHandling; }
    public void setTimeoutHandling(boolean timeoutHandling) { this.timeoutHandling = timeoutHandling; }
    
    public int getErrorHandling() { return errorHandling; }
    public void setErrorHandling(int errorHandling) { this.errorHandling = errorHandling; }
    
    public boolean isFallbackMechanisms() { return fallbackMechanisms; }
    public void setFallbackMechanisms(boolean fallbackMechanisms) { 
        this.fallbackMechanisms = fallbackMechanisms; 
    }
}

/**
 * Score de operabilidad
 */
class OperabilityScore {
    private int score;
    private boolean metricsExposed;
    private boolean healthChecksImplemented;
    private int loggingQuality;
    private int documentationQuality;
    
    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }
    
    public boolean isMetricsExposed() { return metricsExposed; }
    public void setMetricsExposed(boolean metricsExposed) { this.metricsExposed = metricsExposed; }
    
    public boolean isHealthChecksImplemented() { return healthChecksImplemented; }
    public void setHealthChecksImplemented(boolean healthChecksImplemented) { 
        this.healthChecksImplemented = healthChecksImplemented; 
    }
    
    public int getLoggingQuality() { return loggingQuality; }
    public void setLoggingQuality(int loggingQuality) { this.loggingQuality = loggingQuality; }
    
    public int getDocumentationQuality() { return documentationQuality; }
    public void setDocumentationQuality(int documentationQuality) { 
        this.documentationQuality = documentationQuality; 
    }
}
