package com.pluto.learning.assessment;

import java.util.List;
import java.util.Map;



class PlagiarismMatch {
    private String sourceFile;
    private int startLine;
    private int endLine;
    private double similarity;
    
    public String getSourceFile() { return sourceFile; }
    public void setSourceFile(String sourceFile) { this.sourceFile = sourceFile; }
    
    public int getStartLine() { return startLine; }
    public void setStartLine(int startLine) { this.startLine = startLine; }
    
    public int getEndLine() { return endLine; }
    public void setEndLine(int endLine) { this.endLine = endLine; }
    
    public double getSimilarity() { return similarity; }
    public void setSimilarity(double similarity) { this.similarity = similarity; }
}

/**
 * Resultado de ejecución de tests
 */
class TestExecutionResult {
    private int passedTests;
    private int totalTests;
    private double coverage;
    private int workingEndpoints;
    private int totalEndpoints;
    private List<String> failedTests;
    private Map<String, Object> testDetails;
    
    public int getPassedTests() { return passedTests; }
    public void setPassedTests(int passedTests) { this.passedTests = passedTests; }
    
    public int getTotalTests() { return totalTests; }
    public void setTotalTests(int totalTests) { this.totalTests = totalTests; }
    
    public double getCoverage() { return coverage; }
    public void setCoverage(double coverage) { this.coverage = coverage; }
    
    public int getWorkingEndpoints() { return workingEndpoints; }
    public void setWorkingEndpoints(int workingEndpoints) { this.workingEndpoints = workingEndpoints; }
    
    public int getTotalEndpoints() { return totalEndpoints; }
    public void setTotalEndpoints(int totalEndpoints) { this.totalEndpoints = totalEndpoints; }
    
    public List<String> getFailedTests() { return failedTests; }
    public void setFailedTests(List<String> failedTests) { this.failedTests = failedTests; }
    
    public Map<String, Object> getTestDetails() { return testDetails; }
    public void setTestDetails(Map<String, Object> testDetails) { this.testDetails = testDetails; }
}

/**
 * Análisis de calidad de código
 */
class CodeAnalysisReport {
    private double testCoverage;
    private int cyclomaticComplexity;
    private double codeDuplication;
    private int bugCount;
    private int vulnerabilityCount;
    private int codeSmellCount;
    private Map<String, Object> details;
    
    public double getTestCoverage() { return testCoverage; }
    public void setTestCoverage(double testCoverage) { this.testCoverage = testCoverage; }
    
    public int getCyclomaticComplexity() { return cyclomaticComplexity; }
    public void setCyclomaticComplexity(int cyclomaticComplexity) { 
        this.cyclomaticComplexity = cyclomaticComplexity; 
    }
    
    public double getCodeDuplication() { return codeDuplication; }
    public void setCodeDuplication(double codeDuplication) { this.codeDuplication = codeDuplication; }
    
    public int getBugCount() { return bugCount; }
    public void setBugCount(int bugCount) { this.bugCount = bugCount; }
    
    public int getVulnerabilityCount() { return vulnerabilityCount; }
    public void setVulnerabilityCount(int vulnerabilityCount) { 
        this.vulnerabilityCount = vulnerabilityCount; 
    }
    
    public int getCodeSmellCount() { return codeSmellCount; }
    public void setCodeSmellCount(int codeSmellCount) { this.codeSmellCount = codeSmellCount; }
    
    public Map<String, Object> getDetails() { return details; }
    public void setDetails(Map<String, Object> details) { this.details = details; }
}

/**
 * Análisis de arquitectura
 */
class ArchitectureAnalysis {
    private List<String> designPatterns;
    private boolean properLayering;
    private boolean managedDependencies;
    private int apiDesignScore;
    
    public List<String> getDesignPatterns() { return designPatterns; }
    public void setDesignPatterns(List<String> designPatterns) { this.designPatterns = designPatterns; }
    
    public boolean hasProperLayering() { return properLayering; }
    public void setProperLayering(boolean properLayering) { this.properLayering = properLayering; }
    
    public boolean hasManagedDependencies() { return managedDependencies; }
    public void setManagedDependencies(boolean managedDependencies) { 
        this.managedDependencies = managedDependencies; 
    }
    
    public int getApiDesignScore() { return apiDesignScore; }
    public void setApiDesignScore(int apiDesignScore) { this.apiDesignScore = apiDesignScore; }
}

/**
 * Análisis de resiliencia
 */
class ResilienceAnalysis {
    private boolean circuitBreaker;
    private boolean retryLogic;
    private boolean timeoutHandling;
    private int errorHandlingQuality;
    private boolean fallbackMechanisms;
    
    public boolean hasCircuitBreaker() { return circuitBreaker; }
    public void setCircuitBreaker(boolean circuitBreaker) { this.circuitBreaker = circuitBreaker; }
    
    public boolean hasRetryLogic() { return retryLogic; }
    public void setRetryLogic(boolean retryLogic) { this.retryLogic = retryLogic; }
    
    public boolean hasTimeoutHandling() { return timeoutHandling; }
    public void setTimeoutHandling(boolean timeoutHandling) { this.timeoutHandling = timeoutHandling; }
    
    public int getErrorHandlingQuality() { return errorHandlingQuality; }
    public void setErrorHandlingQuality(int errorHandlingQuality) { 
        this.errorHandlingQuality = errorHandlingQuality; 
    }
    
    public boolean hasFallbackMechanisms() { return fallbackMechanisms; }
    public void setFallbackMechanisms(boolean fallbackMechanisms) { 
        this.fallbackMechanisms = fallbackMechanisms; 
    }
}

/**
 * Análisis de operabilidad
 */
class OperabilityAnalysis {
    private boolean metrics;
    private boolean healthChecks;
    private int loggingQuality;
    private int documentationScore;
    
    public boolean hasMetrics() { return metrics; }
    public void setMetrics(boolean metrics) { this.metrics = metrics; }
    
    public boolean hasHealthChecks() { return healthChecks; }
    public void setHealthChecks(boolean healthChecks) { this.healthChecks = healthChecks; }
    
    public int getLoggingQuality() { return loggingQuality; }
    public void setLoggingQuality(int loggingQuality) { this.loggingQuality = loggingQuality; }
    
    public int getDocumentationScore() { return documentationScore; }
    public void setDocumentationScore(int documentationScore) { 
        this.documentationScore = documentationScore; 
    }
}
