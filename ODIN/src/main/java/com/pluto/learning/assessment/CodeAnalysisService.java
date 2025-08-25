package com.pluto.learning.assessment;

import com.pluto.learning.submissions.LabSubmission;
import org.springframework.stereotype.Service;

/**
 * Servicio de análisis de código
 */
@Service
public class CodeAnalysisService {
    
    /**
     * Ejecuta tests automatizados en la submission
     */
    public TestExecutionResult runAutomatedTests(LabSubmission submission) {
        TestExecutionResult result = new TestExecutionResult();
        
        // Mock implementation - en producción integraría con Docker + test runners
        try {
            // Para LabSubmission, el código está en GitHub
            String githubUrl = submission.getGithubRepoUrl();
            String code = fetchCodeFromGitHub(githubUrl); // Mock method
            
            // Análisis básico del código
            int totalTests = countTestMethods(code);
            int passedTests = (int) (totalTests * 0.8); // 80% pass rate mock
            
            result.setTotalTests(totalTests);
            result.setPassedTests(passedTests);
            result.setCoverage(0.75); // 75% coverage mock
            result.setTotalEndpoints(countEndpoints(code));
            result.setWorkingEndpoints((int) (result.getTotalEndpoints() * 0.9));
            
        } catch (Exception e) {
            // Error en ejecución
            result.setTotalTests(0);
            result.setPassedTests(0);
            result.setCoverage(0.0);
        }
        
        return result;
    }
    
    /**
     * Analiza la calidad del código usando métricas estáticas
     */
    public CodeAnalysisReport analyzeCode(LabSubmission submission) {
        CodeAnalysisReport report = new CodeAnalysisReport();
        
        String code = fetchCodeFromGitHub(submission.getGithubRepoUrl());
        if (code == null || code.isEmpty()) {
            return report;
        }
        
        // Análisis de métricas básicas (mock)
        report.setTestCoverage(calculateTestCoverage(code));
        report.setCyclomaticComplexity(calculateComplexity(code));
        report.setCodeDuplication(calculateDuplication(code));
        report.setBugCount(detectBugs(code));
        report.setVulnerabilityCount(detectVulnerabilities(code));
        report.setCodeSmellCount(detectCodeSmells(code));
        
        return report;
    }
    
    /**
     * Analiza la arquitectura del código
     */
    public ArchitectureAnalysis analyzeArchitecture(LabSubmission submission) {
        ArchitectureAnalysis analysis = new ArchitectureAnalysis();
        
        String code = fetchCodeFromGitHub(submission.getGithubRepoUrl());
        
        // Detectar patrones de diseño (mock)
        analysis.setDesignPatterns(detectDesignPatterns(code));
        analysis.setProperLayering(hasLayeredArchitecture(code));
        analysis.setManagedDependencies(hasDependencyInjection(code));
        analysis.setApiDesignScore(evaluateApiDesign(code));
        
        return analysis;
    }
    
    /**
     * Analiza patrones de resiliencia
     */
    public ResilienceAnalysis analyzeResilience(LabSubmission submission) {
        ResilienceAnalysis analysis = new ResilienceAnalysis();
        
        String code = fetchCodeFromGitHub(submission.getGithubRepoUrl());
        
        // Detectar patrones de resiliencia
        analysis.setCircuitBreaker(hasCircuitBreakerPattern(code));
        analysis.setRetryLogic(hasRetryPattern(code));
        analysis.setTimeoutHandling(hasTimeoutHandling(code));
        analysis.setFallbackMechanisms(hasFallbackMechanisms(code));
        analysis.setErrorHandlingQuality(evaluateErrorHandling(code));
        
        return analysis;
    }
    
    /**
     * Analiza aspectos de operabilidad
     */
    public OperabilityAnalysis analyzeOperability(LabSubmission submission) {
        OperabilityAnalysis analysis = new OperabilityAnalysis();
        
        String code = fetchCodeFromGitHub(submission.getGithubRepoUrl());
        
        // Detectar aspectos operacionales
        analysis.setMetrics(hasMetricsExposed(code));
        analysis.setHealthChecks(hasHealthChecks(code));
        analysis.setLoggingQuality(evaluateLogging(code));
        analysis.setDocumentationScore(evaluateDocumentation(code));
        
        return analysis;
    }
    
    /**
     * Mock method para obtener código desde GitHub
     * En producción usaría GitHub API o git clone
     */
    private String fetchCodeFromGitHub(String githubUrl) {
        // Mock implementation
        return """
            @RestController
            public class UserController {
                @Autowired
                private UserService userService;
                
                @Test
                public void testUser() {
                    // test implementation
                }
                
                @GetMapping("/users")
                public ResponseEntity<List<User>> getUsers() {
                    return ResponseEntity.ok(userService.getAllUsers());
                }
            }
            """;
    }
    
    // Métodos auxiliares de análisis (implementaciones mock)
    private int countTestMethods(String code) {
        if (code == null) return 0;
        return (int) code.lines()
            .filter(line -> line.contains("@Test") || line.contains("test"))
            .count();
    }
    
    private int countEndpoints(String code) {
        if (code == null) return 0;
        return (int) code.lines()
            .filter(line -> line.contains("@GetMapping") || 
                           line.contains("@PostMapping") ||
                           line.contains("@PutMapping") ||
                           line.contains("@DeleteMapping"))
            .count();
    }
    
    private double calculateTestCoverage(String code) {
        // Mock: analizar ratio de tests vs código
        long totalLines = code.lines().count();
        long testLines = code.lines()
            .filter(line -> line.contains("@Test") || line.contains("test"))
            .count();
        
        return totalLines > 0 ? Math.min(1.0, testLines * 10.0 / totalLines) : 0.0;
    }
    
    private int calculateComplexity(String code) {
        // Mock: contar if, for, while, switch
        return (int) code.lines()
            .filter(line -> line.contains("if ") || 
                           line.contains("for ") || 
                           line.contains("while ") ||
                           line.contains("switch"))
            .count();
    }
    
    private double calculateDuplication(String code) {
        // Mock: porcentaje de duplicación
        return Math.random() * 20; // 0-20% duplicación
    }
    
    private int detectBugs(String code) {
        // Mock: detectar bugs comunes
        int bugs = 0;
        if (code.contains("null.")) bugs++;
        if (code.contains("== null") && !code.contains("!= null")) bugs++;
        return bugs;
    }
    
    private int detectVulnerabilities(String code) {
        // Mock: detectar vulnerabilidades
        int vulns = 0;
        if (code.contains("SQL") && !code.contains("PreparedStatement")) vulns++;
        if (code.contains("eval(")) vulns++;
        return vulns;
    }
    
    private int detectCodeSmells(String code) {
        // Mock: detectar code smells
        return (int) (Math.random() * 10);
    }
    
    private java.util.List<String> detectDesignPatterns(String code) {
        java.util.List<String> patterns = new java.util.ArrayList<>();
        if (code.contains("@Service")) patterns.add("Service Layer");
        if (code.contains("@Repository")) patterns.add("Repository");
        if (code.contains("@Controller")) patterns.add("MVC");
        if (code.contains("Factory")) patterns.add("Factory");
        if (code.contains("Builder")) patterns.add("Builder");
        return patterns;
    }
    
    private boolean hasLayeredArchitecture(String code) {
        return code.contains("@Controller") && 
               code.contains("@Service") && 
               code.contains("@Repository");
    }
    
    private boolean hasDependencyInjection(String code) {
        return code.contains("@Autowired") || 
               code.contains("@Inject") || 
               code.contains("constructor injection");
    }
    
    private int evaluateApiDesign(String code) {
        int score = 0;
        if (code.contains("@RestController")) score += 25;
        if (code.contains("@RequestMapping")) score += 25;
        if (code.contains("ResponseEntity")) score += 25;
        if (code.contains("@Valid")) score += 25;
        return score;
    }
    
    private boolean hasCircuitBreakerPattern(String code) {
        return code.contains("CircuitBreaker") || 
               code.contains("@CircuitBreaker");
    }
    
    private boolean hasRetryPattern(String code) {
        return code.contains("@Retry") || 
               code.contains("retry");
    }
    
    private boolean hasTimeoutHandling(String code) {
        return code.contains("timeout") || 
               code.contains("@Timeout");
    }
    
    private boolean hasFallbackMechanisms(String code) {
        return code.contains("fallback") || 
               code.contains("@Fallback");
    }
    
    private int evaluateErrorHandling(String code) {
        int score = 0;
        if (code.contains("try-catch")) score += 25;
        if (code.contains("@ControllerAdvice")) score += 25;
        if (code.contains("@ExceptionHandler")) score += 25;
        if (code.contains("throw new")) score += 25;
        return score;
    }
    
    private boolean hasMetricsExposed(String code) {
        return code.contains("@Timed") || 
               code.contains("micrometer") ||
               code.contains("metrics");
    }
    
    private boolean hasHealthChecks(String code) {
        return code.contains("@HealthCheck") || 
               code.contains("actuator") ||
               code.contains("health");
    }
    
    private int evaluateLogging(String code) {
        int score = 0;
        if (code.contains("Logger")) score += 30;
        if (code.contains("log.info")) score += 20;
        if (code.contains("log.error")) score += 25;
        if (code.contains("log.debug")) score += 25;
        return Math.min(100, score);
    }
    
    private int evaluateDocumentation(String code) {
        int score = 0;
        if (code.contains("/**")) score += 40; // JavaDoc
        if (code.contains("@Api")) score += 30; // Swagger
        if (code.contains("README")) score += 30;
        return Math.min(100, score);
    }
}
