package com.pluto.learning.assessment;

import com.pluto.learning.submissions.LabSubmission;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.ArrayList;

/**
 * Servicio de detección de plagio
 */
@Service
public class PlagiarismDetectionService {
    
    /**
     * Analiza una submission en busca de plagio
     */
    public PlagiarismAnalysis analyzeSubmission(LabSubmission submission) {
        PlagiarismAnalysis analysis = new PlagiarismAnalysis();
        
        String code = fetchCodeFromGitHub(submission.getGithubRepoUrl());
        if (code == null || code.isEmpty()) {
            analysis.setPlagiarismDetected(false);
            analysis.setSimilarityPercentage(0.0);
            analysis.setAnalysisMethod("NO_CODE");
            return analysis;
        }
        
        // Análisis de similitud estructural
        double structuralSimilarity = analyzeStructuralSimilarity(code);
        
        // Análisis de patrones de código
        double patternSimilarity = analyzeCodePatterns(code);
        
        // Análisis de comentarios y variables
        double semanticSimilarity = analyzeSemanticSimilarity(code);
        
        // Cálculo de similitud total
        double totalSimilarity = (structuralSimilarity * 0.5) + 
                                (patternSimilarity * 0.3) + 
                                (semanticSimilarity * 0.2);
        
        analysis.setSimilarityPercentage(totalSimilarity);
        analysis.setPlagiarismDetected(totalSimilarity > 0.75); // 75% threshold
        analysis.setAnalysisMethod("MULTI_FACTOR_ANALYSIS");
        
        if (analysis.isPlagiarismDetected()) {
            analysis.setMatches(findSimilarMatches(code, totalSimilarity));
        }
        
        return analysis;
    }
    
    /**
     * Mock method para obtener código desde GitHub
     */
    private String fetchCodeFromGitHub(String githubUrl) {
        // Mock implementation - en producción usaría GitHub API
        return """
            @RestController
            public class TestController {
                @Autowired
                private TestService service;
                
                @GetMapping("/test")
                public String test() {
                    return "Hello World";
                }
            }
            """;
    }
    
    /**
     * Analiza similitud estructural del código
     */
    private double analyzeStructuralSimilarity(String code) {
        // Mock: analizar estructura de clases, métodos, etc.
        
        // Extraer estructura básica
        long classCount = countOccurrences(code, "class ");
        long methodCount = countOccurrences(code, "public ") + 
                          countOccurrences(code, "private ") + 
                          countOccurrences(code, "protected ");
        long importCount = countOccurrences(code, "import ");
        
        // Comparar con patrones conocidos (mock)
        // En producción, esto compararía con una base de datos de submissions
        
        // Calcular hash estructural simple
        String structuralHash = generateStructuralHash(code);
        
        // Mock: simular comparación con base de datos
        return Math.random() * 0.4; // Max 40% por estructura
    }
    
    /**
     * Analiza patrones de código específicos
     */
    private double analyzeCodePatterns(String code) {
        // Detectar patrones comunes que podrían ser copiados
        
        double suspiciousPatterns = 0.0;
        
        // Patrones sospechosos
        if (hasUnusualVariableNames(code)) suspiciousPatterns += 0.1;
        if (hasIdenticalComments(code)) suspiciousPatterns += 0.2;
        if (hasUnusualFormattingPatterns(code)) suspiciousPatterns += 0.1;
        if (hasIdenticalAlgorithmicStructure(code)) suspiciousPatterns += 0.3;
        
        return Math.min(1.0, suspiciousPatterns);
    }
    
    /**
     * Analiza similitud semántica
     */
    private double analyzeSemanticSimilarity(String code) {
        // Analizar nombres de variables, comentarios, strings
        
        double semanticScore = 0.0;
        
        // Extraer elementos semánticos
        List<String> variableNames = extractVariableNames(code);
        List<String> comments = extractComments(code);
        List<String> stringLiterals = extractStringLiterals(code);
        
        // Mock: comparar con base de datos semántica
        if (hasCommonVariablePatterns(variableNames)) semanticScore += 0.2;
        if (hasIdenticalComments(comments)) semanticScore += 0.3;
        if (hasIdenticalStrings(stringLiterals)) semanticScore += 0.1;
        
        return Math.min(1.0, semanticScore);
    }
    
    /**
     * Encuentra matches específicos de similitud
     */
    private List<PlagiarismMatch> findSimilarMatches(String code, double similarity) {
        List<PlagiarismMatch> matches = new ArrayList<>();
        
        // Mock: crear matches ficticios
        if (similarity > 0.75) {
            PlagiarismMatch match = new PlagiarismMatch();
            match.setSourceFile("previous_submission_123.java");
            match.setStartLine(15);
            match.setEndLine(45);
            match.setSimilarity(similarity);
            matches.add(match);
        }
        
        return matches;
    }
    
    // Métodos auxiliares
    private long countOccurrences(String text, String pattern) {
        return text.lines()
            .mapToLong(line -> {
                int count = 0;
                int index = 0;
                while ((index = line.indexOf(pattern, index)) != -1) {
                    count++;
                    index += pattern.length();
                }
                return count;
            })
            .sum();
    }
    
    private String generateStructuralHash(String code) {
        // Generar hash basado en estructura del código
        StringBuilder structure = new StringBuilder();
        
        code.lines().forEach(line -> {
            String trimmed = line.trim();
            if (trimmed.startsWith("class ")) structure.append("CLASS;");
            else if (trimmed.startsWith("public ")) structure.append("PUBLIC;");
            else if (trimmed.startsWith("private ")) structure.append("PRIVATE;");
            else if (trimmed.contains("if (")) structure.append("IF;");
            else if (trimmed.contains("for (")) structure.append("FOR;");
            else if (trimmed.contains("while (")) structure.append("WHILE;");
        });
        
        return Integer.toString(structure.toString().hashCode());
    }
    
    private boolean hasUnusualVariableNames(String code) {
        // Detectar nombres de variables muy específicos o inusuales
        return code.contains("temp123") || 
               code.contains("myVar") || 
               code.contains("variable1");
    }
    
    private boolean hasIdenticalComments(String code) {
        // Detectar comentarios idénticos
        List<String> comments = extractComments(code);
        return comments.stream()
            .anyMatch(comment -> comment.length() > 20 && 
                     comment.contains("TODO") || 
                     comment.contains("FIXME"));
    }
    
    private boolean hasUnusualFormattingPatterns(String code) {
        // Detectar patrones de formato inusuales
        return code.contains("    ") && !code.contains("\t"); // Espacios vs tabs
    }
    
    private boolean hasIdenticalAlgorithmicStructure(String code) {
        // Detectar estructuras algorítmicas idénticas
        return code.contains("for (int i = 0; i < arr.length; i++)") ||
               code.contains("while (condition == true)");
    }
    
    private List<String> extractVariableNames(String code) {
        List<String> variables = new ArrayList<>();
        
        // Extraer declaraciones de variables (regex simple)
        code.lines().forEach(line -> {
            if (line.contains(" = ") && !line.trim().startsWith("//")) {
                String[] parts = line.split(" = ");
                if (parts.length > 0) {
                    String declaration = parts[0].trim();
                    String[] declarationParts = declaration.split(" ");
                    if (declarationParts.length >= 2) {
                        variables.add(declarationParts[declarationParts.length - 1]);
                    }
                }
            }
        });
        
        return variables;
    }
    
    private List<String> extractComments(String code) {
        List<String> comments = new ArrayList<>();
        
        code.lines().forEach(line -> {
            String trimmed = line.trim();
            if (trimmed.startsWith("//")) {
                comments.add(trimmed.substring(2).trim());
            } else if (trimmed.startsWith("/*")) {
                comments.add(trimmed);
            }
        });
        
        return comments;
    }
    
    private List<String> extractStringLiterals(String code) {
        List<String> strings = new ArrayList<>();
        
        // Extraer strings entre comillas
        int start = 0;
        while ((start = code.indexOf("\"", start)) != -1) {
            int end = code.indexOf("\"", start + 1);
            if (end != -1) {
                strings.add(code.substring(start + 1, end));
                start = end + 1;
            } else {
                break;
            }
        }
        
        return strings;
    }
    
    private boolean hasCommonVariablePatterns(List<String> variables) {
        return variables.stream()
            .anyMatch(var -> var.equals("temp") || 
                           var.equals("result") || 
                           var.equals("data"));
    }
    
    private boolean hasIdenticalComments(List<String> comments) {
        return comments.stream()
            .anyMatch(comment -> comment.contains("copied from") || 
                               comment.contains("source:"));
    }
    
    private boolean hasIdenticalStrings(List<String> strings) {
        return strings.stream()
            .anyMatch(str -> str.contains("Hello World") || 
                           str.contains("Test message"));
    }
}
