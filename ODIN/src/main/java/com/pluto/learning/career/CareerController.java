package com.pluto.learning.career;

import com.pluto.learning.auth.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Controlador REST para la gestión del desarrollo profesional
 */
@RestController
@RequestMapping("/api/career")
@CrossOrigin(origins = "*")
public class CareerController {

    @Autowired
    private CareerDevelopmentService careerService;

    @Autowired
    private CareerProfileRepository careerProfileRepository;

    @Autowired
    private InterviewQuestionRepository questionRepository;

    @Autowired
    private InterviewResponseRepository responseRepository;

    /**
     * Obtener perfil profesional del usuario
     */
    @GetMapping("/profile")
    public ResponseEntity<CareerProfile> getCareerProfile(@AuthenticationPrincipal User user) {
        Optional<CareerProfile> profile = careerProfileRepository.findByUserId(user.getId());
        if (profile.isPresent()) {
            return ResponseEntity.ok(profile.get());
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Crear nuevo perfil profesional
     */
    @PostMapping("/profile")
    public ResponseEntity<CareerProfile> createCareerProfile(
            @AuthenticationPrincipal User user,
            @RequestBody CareerProfileRequest request) {
        
        CareerProfile profile = new CareerProfile(user, request.getCareerGoal(), request.getExperienceLevel());
        profile.setTargetRole(request.getTargetRole());
        profile.setTargetCompany(request.getTargetCompany());
        profile.setExpectedSalaryMin(request.getExpectedSalaryMin());
        profile.setExpectedSalaryMax(request.getExpectedSalaryMax());
        profile.setLocationPreference(request.getLocationPreference());
        profile.setRemotePreference(request.getRemotePreference());
        profile.setAvailabilityDate(request.getAvailabilityDate());
        
        CareerProfile saved = careerProfileRepository.save(profile);
        return ResponseEntity.ok(saved);
    }

    /**
     * Actualizar perfil profesional
     */
    @PutMapping("/profile")
    public ResponseEntity<CareerProfile> updateCareerProfile(
            @AuthenticationPrincipal User user,
            @RequestBody CareerProfileRequest request) {
        
        Optional<CareerProfile> existingProfile = careerProfileRepository.findByUserId(user.getId());
        if (!existingProfile.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        
        CareerProfile profile = existingProfile.get();
        profile.setCareerGoal(request.getCareerGoal());
        profile.setExperienceLevel(request.getExperienceLevel());
        profile.setTargetRole(request.getTargetRole());
        profile.setTargetCompany(request.getTargetCompany());
        profile.setExpectedSalaryMin(request.getExpectedSalaryMin());
        profile.setExpectedSalaryMax(request.getExpectedSalaryMax());
        profile.setLocationPreference(request.getLocationPreference());
        profile.setRemotePreference(request.getRemotePreference());
        profile.setAvailabilityDate(request.getAvailabilityDate());
        
        CareerProfile updated = careerProfileRepository.save(profile);
        return ResponseEntity.ok(updated);
    }

    /**
     * Generar roadmap personalizado
     */
    @PostMapping("/roadmap")
    public ResponseEntity<Map<String, Object>> generatePersonalizedRoadmap(
            @AuthenticationPrincipal User user) {
        
        CareerRoadmap roadmap = careerService.generatePersonalizedRoadmap(user);
        Map<String, Object> response = new HashMap<>();
        response.put("roadmap", roadmap);
        response.put("message", "Roadmap generated successfully");
        
        return ResponseEntity.ok(response);
    }

    /**
     * Obtener preguntas de entrevista por categoría y dificultad
     */
    @GetMapping("/interview/questions")
    public ResponseEntity<List<InterviewQuestion>> getInterviewQuestions(
            @RequestParam(required = false) InterviewQuestion.QuestionCategory category,
            @RequestParam(required = false) InterviewQuestion.DifficultyLevel difficulty,
            @RequestParam(defaultValue = "10") int limit) {
        
        List<InterviewQuestion> questions = questionRepository.findByActive(true);
        
        // Filtrar por categoría si se especifica
        if (category != null) {
            questions = questions.stream()
                .filter(q -> q.getCategory().equals(category))
                .collect(java.util.stream.Collectors.toList());
        }
        
        // Filtrar por dificultad si se especifica
        if (difficulty != null) {
            questions = questions.stream()
                .filter(q -> q.getDifficultyLevel().equals(difficulty))
                .collect(java.util.stream.Collectors.toList());
        }
        
        // Limitar resultados y mezclar aleatoriamente
        Collections.shuffle(questions);
        if (questions.size() > limit) {
            questions = questions.subList(0, limit);
        }
        
        return ResponseEntity.ok(questions);
    }

    /**
     * Enviar respuesta a pregunta de entrevista
     */
    @PostMapping("/interview/response")
    public ResponseEntity<Map<String, Object>> submitInterviewResponse(
            @AuthenticationPrincipal User user,
            @RequestBody InterviewResponseRequest request) {
        
        Optional<InterviewQuestion> question = questionRepository.findById(request.getQuestionId());
        if (!question.isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Question not found"));
        }
        
        InterviewResponse response = new InterviewResponse(user, question.get(), request.getResponseText());
        response.setResponseTimeSeconds(request.getResponseTimeSeconds());
        response.setPracticeSessionId(request.getSessionId());
        
        // Aquí se podría agregar evaluación con IA
        response.setAiScore(75); // Placeholder
        response.setAiFeedback("Good response. Consider providing more specific examples.");
        
        InterviewResponse saved = responseRepository.save(response);
        
        Map<String, Object> result = new HashMap<>();
        result.put("response", saved);
        result.put("score", saved.getAiScore());
        result.put("feedback", saved.getAiFeedback());
        result.put("performanceLevel", saved.getPerformanceLevel());
        
        return ResponseEntity.ok(result);
    }

    /**
     * Obtener historial de respuestas de entrevista del usuario
     */
    @GetMapping("/interview/responses")
    public ResponseEntity<List<InterviewResponse>> getInterviewResponses(
            @AuthenticationPrincipal User user) {
        
        List<InterviewResponse> responses = responseRepository.findByUserIdOrderByCreatedAtDesc(user.getId());
        return ResponseEntity.ok(responses);
    }

    /**
     * Obtener estadísticas de rendimiento en entrevistas
     */
    @GetMapping("/interview/analytics")
    public ResponseEntity<Map<String, Object>> getInterviewAnalytics(
            @AuthenticationPrincipal User user) {
        
        List<InterviewResponse> responses = responseRepository.findByUserId(user.getId());
        
        Map<String, Object> analytics = new HashMap<>();
        analytics.put("totalResponses", responses.size());
        
        if (!responses.isEmpty()) {
            double avgScore = responses.stream()
                .filter(r -> r.getAiScore() != null)
                .mapToInt(InterviewResponse::getAiScore)
                .average()
                .orElse(0.0);
            
            analytics.put("averageScore", Math.round(avgScore * 100.0) / 100.0);
            
            Map<String, Long> categoryStats = responses.stream()
                .collect(java.util.stream.Collectors.groupingBy(
                    r -> r.getQuestion().getCategory().name(),
                    java.util.stream.Collectors.counting()
                ));
            
            analytics.put("categoriesAttempted", categoryStats);
        } else {
            analytics.put("averageScore", 0);
            analytics.put("categoriesAttempted", new HashMap<>());
        }
        
        return ResponseEntity.ok(analytics);
    }

    // DTOs de request
    public static class CareerProfileRequest {
        private CareerProfile.CareerGoal careerGoal;
        private CareerProfile.ExperienceLevel experienceLevel;
        private String targetRole;
        private String targetCompany;
        private Integer expectedSalaryMin;
        private Integer expectedSalaryMax;
        private String locationPreference;
        private CareerProfile.RemotePreference remotePreference;
        private java.time.LocalDateTime availabilityDate;

        // Getters and setters
        public CareerProfile.CareerGoal getCareerGoal() { return careerGoal; }
        public void setCareerGoal(CareerProfile.CareerGoal careerGoal) { this.careerGoal = careerGoal; }
        
        public CareerProfile.ExperienceLevel getExperienceLevel() { return experienceLevel; }
        public void setExperienceLevel(CareerProfile.ExperienceLevel experienceLevel) { this.experienceLevel = experienceLevel; }
        
        public String getTargetRole() { return targetRole; }
        public void setTargetRole(String targetRole) { this.targetRole = targetRole; }
        
        public String getTargetCompany() { return targetCompany; }
        public void setTargetCompany(String targetCompany) { this.targetCompany = targetCompany; }
        
        public Integer getExpectedSalaryMin() { return expectedSalaryMin; }
        public void setExpectedSalaryMin(Integer expectedSalaryMin) { this.expectedSalaryMin = expectedSalaryMin; }
        
        public Integer getExpectedSalaryMax() { return expectedSalaryMax; }
        public void setExpectedSalaryMax(Integer expectedSalaryMax) { this.expectedSalaryMax = expectedSalaryMax; }
        
        public String getLocationPreference() { return locationPreference; }
        public void setLocationPreference(String locationPreference) { this.locationPreference = locationPreference; }
        
        public CareerProfile.RemotePreference getRemotePreference() { return remotePreference; }
        public void setRemotePreference(CareerProfile.RemotePreference remotePreference) { this.remotePreference = remotePreference; }
        
        public java.time.LocalDateTime getAvailabilityDate() { return availabilityDate; }
        public void setAvailabilityDate(java.time.LocalDateTime availabilityDate) { this.availabilityDate = availabilityDate; }
    }

    public static class InterviewResponseRequest {
        private String sessionId;
        private Long questionId;
        private String responseText;
        private Integer responseTimeSeconds;

        // Getters and setters
        public String getSessionId() { return sessionId; }
        public void setSessionId(String sessionId) { this.sessionId = sessionId; }
        
        public Long getQuestionId() { return questionId; }
        public void setQuestionId(Long questionId) { this.questionId = questionId; }
        
        public String getResponseText() { return responseText; }
        public void setResponseText(String responseText) { this.responseText = responseText; }
        
        public Integer getResponseTimeSeconds() { return responseTimeSeconds; }
        public void setResponseTimeSeconds(Integer responseTimeSeconds) { this.responseTimeSeconds = responseTimeSeconds; }
    }
}
