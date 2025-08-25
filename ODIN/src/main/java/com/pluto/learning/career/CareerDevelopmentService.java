package com.pluto.learning.career;

import com.pluto.learning.auth.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Servicio para gestión de desarrollo profesional y carrera
 */
@Service
@Transactional
public class CareerDevelopmentService {
    
    @Autowired
    private CareerProfileRepository careerProfileRepository;
    
    @Autowired
    private InterviewQuestionRepository interviewQuestionRepository;
    
    @Autowired
    private InterviewResponseRepository interviewResponseRepository;
    
    /**
     * Crea o actualiza el perfil profesional del usuario
     */
    public CareerProfile createOrUpdateProfile(User user, CareerProfileDTO profileDTO) {
        CareerProfile profile = careerProfileRepository.findByUser(user)
            .orElse(new CareerProfile(user, profileDTO.getCareerGoal(), profileDTO.getExperienceLevel()));
        
        // Actualizar datos del perfil
        profile.setCareerGoal(profileDTO.getCareerGoal());
        profile.setExperienceLevel(profileDTO.getExperienceLevel());
        profile.setTargetRole(profileDTO.getTargetRole());
        profile.setTargetCompany(profileDTO.getTargetCompany());
        profile.setExpectedSalaryMin(profileDTO.getExpectedSalaryMin());
        profile.setExpectedSalaryMax(profileDTO.getExpectedSalaryMax());
        profile.setLocationPreference(profileDTO.getLocationPreference());
        profile.setRemotePreference(profileDTO.getRemotePreference());
        profile.setAvailabilityDate(profileDTO.getAvailabilityDate());
        profile.setUpdatedAt(LocalDateTime.now());
        
        return careerProfileRepository.save(profile);
    }
    
    /**
     * Genera un roadmap personalizado basado en el perfil del usuario
     */
    public CareerRoadmap generatePersonalizedRoadmap(User user) {
        CareerProfile profile = careerProfileRepository.findByUser(user)
            .orElseThrow(() -> new IllegalArgumentException("User must have a career profile"));
        
        CareerRoadmap roadmap = new CareerRoadmap();
        roadmap.setCareerGoal(profile.getCareerGoal());
        roadmap.setCurrentLevel(profile.getExperienceLevel());
        
        // Generar milestones basados en el objetivo de carrera
        List<CareerMilestone> milestones = generateMilestones(profile);
        roadmap.setMilestones(milestones);
        
        // Calcular tiempo estimado
        roadmap.setEstimatedTimeMonths(calculateEstimatedTime(profile, milestones));
        
        // Generar recomendaciones de skills
        roadmap.setRecommendedSkills(generateSkillRecommendations(profile));
        
        return roadmap;
    }
    
    /**
     * Obtiene preguntas de entrevista personalizadas
     */
    public List<InterviewQuestion> getPersonalizedInterviewQuestions(User user, int count) {
        CareerProfile profile = careerProfileRepository.findByUser(user).orElse(null);
        
        if (profile == null) {
            // Sin perfil, devolver preguntas generales
            return interviewQuestionRepository.findByActiveTrueOrderByRandom(count);
        }
        
        // Filtrar por nivel de experiencia y objetivo
        InterviewQuestion.DifficultyLevel targetLevel = mapExperienceToInterviewLevel(profile.getExperienceLevel());
        
        List<InterviewQuestion> questions = new ArrayList<>();
        
        // Mix de categorías de preguntas
        questions.addAll(interviewQuestionRepository.findByCategoryAndDifficultyLevel(
            InterviewQuestion.QuestionCategory.TECHNICAL, targetLevel, count / 3));
        questions.addAll(interviewQuestionRepository.findByCategoryAndDifficultyLevel(
            InterviewQuestion.QuestionCategory.BEHAVIORAL, targetLevel, count / 3));
        questions.addAll(interviewQuestionRepository.findByCategoryAndDifficultyLevel(
            InterviewQuestion.QuestionCategory.SITUATIONAL, targetLevel, count / 3));
        
        // Rellenar con preguntas adicionales si es necesario
        if (questions.size() < count) {
            int remaining = count - questions.size();
            List<InterviewQuestion> additional = interviewQuestionRepository
                .findByDifficultyLevelAndActiveTrue(targetLevel, remaining);
            questions.addAll(additional);
        }
        
        Collections.shuffle(questions);
        return questions.stream().limit(count).collect(Collectors.toList());
    }
    
    /**
     * Inicia una sesión de práctica de entrevista
     */
    public InterviewPracticeSession startPracticeSession(User user, List<Long> questionIds) {
        String sessionId = UUID.randomUUID().toString();
        
        List<InterviewQuestion> questions = interviewQuestionRepository.findByIdIn(questionIds);
        
        InterviewPracticeSession session = new InterviewPracticeSession();
        session.setSessionId(sessionId);
        session.setUser(user);
        session.setQuestions(questions);
        session.setStartTime(LocalDateTime.now());
        session.setStatus(InterviewPracticeSession.SessionStatus.IN_PROGRESS);
        
        return session;
    }
    
    /**
     * Procesa la respuesta a una pregunta de entrevista
     */
    public InterviewResponse processInterviewResponse(User user, Long questionId, String responseText, 
                                                    Integer responseTimeSeconds, String sessionId) {
        InterviewQuestion question = interviewQuestionRepository.findById(questionId)
            .orElseThrow(() -> new IllegalArgumentException("Question not found"));
        
        InterviewResponse response = new InterviewResponse(user, question, responseText);
        response.setResponseTimeSeconds(responseTimeSeconds);
        response.setPracticeSessionId(sessionId);
        
        // Generar feedback con IA (mock implementation)
        AIFeedback feedback = generateAIFeedback(question, responseText, responseTimeSeconds);
        response.setAiFeedback(feedback.getFeedbackText());
        response.setAiScore(feedback.getScore());
        
        return interviewResponseRepository.save(response);
    }
    
    /**
     * Obtiene el análisis de performance de entrevistas del usuario
     */
    public InterviewPerformanceAnalysis getPerformanceAnalysis(User user) {
        List<InterviewResponse> responses = interviewResponseRepository.findByUserOrderByCreatedAtDesc(user);
        
        InterviewPerformanceAnalysis analysis = new InterviewPerformanceAnalysis();
        analysis.setTotalResponses(responses.size());
        
        if (responses.isEmpty()) {
            return analysis;
        }
        
        // Calcular estadísticas
        OptionalDouble avgScore = responses.stream()
            .filter(r -> r.getAiScore() != null)
            .mapToInt(InterviewResponse::getAiScore)
            .average();
        
        analysis.setAverageScore(avgScore.orElse(0.0));
        
        // Análisis por categoría
        Map<InterviewQuestion.QuestionCategory, Double> categoryScores = responses.stream()
            .filter(r -> r.getAiScore() != null)
            .collect(Collectors.groupingBy(
                r -> r.getQuestion().getCategory(),
                Collectors.averagingInt(InterviewResponse::getAiScore)
            ));
        
        analysis.setCategoryPerformance(categoryScores);
        
        // Áreas de mejora
        analysis.setImprovementAreas(identifyImprovementAreas(categoryScores));
        
        // Progreso en el tiempo
        analysis.setProgressOverTime(calculateProgressOverTime(responses));
        
        return analysis;
    }
    
    // Métodos auxiliares privados
    private List<CareerMilestone> generateMilestones(CareerProfile profile) {
        List<CareerMilestone> milestones = new ArrayList<>();
        
        switch (profile.getCareerGoal()) {
            case JUNIOR_DEVELOPER:
                milestones.add(new CareerMilestone("Master Programming Fundamentals", 
                    "Complete core programming courses and build 3 projects", 3));
                milestones.add(new CareerMilestone("Build Portfolio", 
                    "Create 5 diverse projects showcasing different skills", 2));
                milestones.add(new CareerMilestone("Prepare for Interviews", 
                    "Practice coding challenges and behavioral questions", 1));
                break;
                
            case MID_LEVEL_DEVELOPER:
                milestones.add(new CareerMilestone("Advanced Architecture Skills", 
                    "Learn design patterns and system architecture", 4));
                milestones.add(new CareerMilestone("Leadership Experience", 
                    "Lead small projects and mentor junior developers", 6));
                milestones.add(new CareerMilestone("Specialization", 
                    "Develop expertise in specific technology stack", 3));
                break;
                
            case SENIOR_DEVELOPER:
                milestones.add(new CareerMilestone("System Design Mastery", 
                    "Master large-scale system design and architecture", 6));
                milestones.add(new CareerMilestone("Technical Leadership", 
                    "Lead cross-functional teams and technical initiatives", 8));
                milestones.add(new CareerMilestone("Strategic Thinking", 
                    "Contribute to product strategy and technical roadmap", 4));
                break;
                
            default:
                milestones.add(new CareerMilestone("Skill Assessment", 
                    "Complete comprehensive skill evaluation", 1));
                milestones.add(new CareerMilestone("Personalized Learning Path", 
                    "Follow customized development plan", 6));
        }
        
        return milestones;
    }
    
    private int calculateEstimatedTime(CareerProfile profile, List<CareerMilestone> milestones) {
        return milestones.stream()
            .mapToInt(CareerMilestone::getEstimatedMonths)
            .sum();
    }
    
    private List<String> generateSkillRecommendations(CareerProfile profile) {
        List<String> skills = new ArrayList<>();
        
        switch (profile.getCareerGoal()) {
            case JUNIOR_DEVELOPER:
                skills.addAll(Arrays.asList("Java", "Spring Boot", "JavaScript", "React", "SQL", "Git"));
                break;
            case MID_LEVEL_DEVELOPER:
                skills.addAll(Arrays.asList("Microservices", "Docker", "Kubernetes", "Redis", "MongoDB", "AWS"));
                break;
            case SENIOR_DEVELOPER:
                skills.addAll(Arrays.asList("System Design", "Leadership", "Architecture", "DevOps", "Performance Optimization"));
                break;
            case TECH_LEAD:
                skills.addAll(Arrays.asList("Team Management", "Agile/Scrum", "Technical Strategy", "Mentoring"));
                break;
            default:
                skills.addAll(Arrays.asList("Problem Solving", "Communication", "Continuous Learning"));
        }
        
        return skills;
    }
    
    private InterviewQuestion.DifficultyLevel mapExperienceToInterviewLevel(CareerProfile.ExperienceLevel experience) {
        switch (experience) {
            case BEGINNER: return InterviewQuestion.DifficultyLevel.JUNIOR;
            case INTERMEDIATE: return InterviewQuestion.DifficultyLevel.MID;
            case ADVANCED: return InterviewQuestion.DifficultyLevel.SENIOR;
            case EXPERT: return InterviewQuestion.DifficultyLevel.PRINCIPAL;
            default: return InterviewQuestion.DifficultyLevel.MID;
        }
    }
    
    private AIFeedback generateAIFeedback(InterviewQuestion question, String response, Integer timeSeconds) {
        // Mock AI feedback - en producción integraría con LLM
        AIFeedback feedback = new AIFeedback();
        
        int score = 75; // Base score
        StringBuilder feedbackText = new StringBuilder();
        
        // Evaluar longitud de respuesta
        if (response.length() < 50) {
            score -= 20;
            feedbackText.append("Response too brief. Consider providing more detail. ");
        } else if (response.length() > 1000) {
            score -= 10;
            feedbackText.append("Response very lengthy. Try to be more concise. ");
        }
        
        // Evaluar tiempo de respuesta
        if (question.getTimeLimitSeconds() != null && timeSeconds != null) {
            if (timeSeconds > question.getTimeLimitSeconds()) {
                score -= 15;
                feedbackText.append("Response exceeded time limit. Practice being more concise. ");
            } else if (timeSeconds < question.getTimeLimitSeconds() * 0.3) {
                score -= 5;
                feedbackText.append("Very quick response. Ensure you're thinking through the answer. ");
            }
        }
        
        // Evaluar contenido (mock)
        if (response.toLowerCase().contains("example") || response.toLowerCase().contains("experience")) {
            score += 10;
            feedbackText.append("Good use of examples to support your answer. ");
        }
        
        if (question.getCategory() == InterviewQuestion.QuestionCategory.BEHAVIORAL) {
            if (response.toLowerCase().contains("situation") && response.toLowerCase().contains("result")) {
                score += 15;
                feedbackText.append("Excellent STAR method structure. ");
            }
        }
        
        feedback.setScore(Math.max(0, Math.min(100, score)));
        feedback.setFeedbackText(feedbackText.toString().trim());
        
        return feedback;
    }
    
    private List<String> identifyImprovementAreas(Map<InterviewQuestion.QuestionCategory, Double> categoryScores) {
        return categoryScores.entrySet().stream()
            .filter(entry -> entry.getValue() < 70)
            .map(entry -> entry.getKey().toString())
            .collect(Collectors.toList());
    }
    
    private Map<String, Double> calculateProgressOverTime(List<InterviewResponse> responses) {
        // Simplificado: últimas 5 respuestas vs primeras 5
        Map<String, Double> progress = new HashMap<>();
        
        if (responses.size() >= 10) {
            List<InterviewResponse> recent = responses.subList(0, 5);
            List<InterviewResponse> initial = responses.subList(responses.size() - 5, responses.size());
            
            double recentAvg = recent.stream()
                .filter(r -> r.getAiScore() != null)
                .mapToInt(InterviewResponse::getAiScore)
                .average().orElse(0);
                
            double initialAvg = initial.stream()
                .filter(r -> r.getAiScore() != null)
                .mapToInt(InterviewResponse::getAiScore)
                .average().orElse(0);
            
            progress.put("improvement", recentAvg - initialAvg);
            progress.put("recent_average", recentAvg);
            progress.put("initial_average", initialAvg);
        }
        
        return progress;
    }
}

// DTOs y clases auxiliares
class CareerProfileDTO {
    private CareerProfile.CareerGoal careerGoal;
    private CareerProfile.ExperienceLevel experienceLevel;
    private String targetRole;
    private String targetCompany;
    private Integer expectedSalaryMin;
    private Integer expectedSalaryMax;
    private String locationPreference;
    private CareerProfile.RemotePreference remotePreference;
    private LocalDateTime availabilityDate;
    
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
    
    public LocalDateTime getAvailabilityDate() { return availabilityDate; }
    public void setAvailabilityDate(LocalDateTime availabilityDate) { this.availabilityDate = availabilityDate; }
}

class CareerRoadmap {
    private CareerProfile.CareerGoal careerGoal;
    private CareerProfile.ExperienceLevel currentLevel;
    private List<CareerMilestone> milestones;
    private int estimatedTimeMonths;
    private List<String> recommendedSkills;
    
    // Getters and setters
    public CareerProfile.CareerGoal getCareerGoal() { return careerGoal; }
    public void setCareerGoal(CareerProfile.CareerGoal careerGoal) { this.careerGoal = careerGoal; }
    
    public CareerProfile.ExperienceLevel getCurrentLevel() { return currentLevel; }
    public void setCurrentLevel(CareerProfile.ExperienceLevel currentLevel) { this.currentLevel = currentLevel; }
    
    public List<CareerMilestone> getMilestones() { return milestones; }
    public void setMilestones(List<CareerMilestone> milestones) { this.milestones = milestones; }
    
    public int getEstimatedTimeMonths() { return estimatedTimeMonths; }
    public void setEstimatedTimeMonths(int estimatedTimeMonths) { this.estimatedTimeMonths = estimatedTimeMonths; }
    
    public List<String> getRecommendedSkills() { return recommendedSkills; }
    public void setRecommendedSkills(List<String> recommendedSkills) { this.recommendedSkills = recommendedSkills; }
}

class CareerMilestone {
    private String title;
    private String description;
    private int estimatedMonths;
    
    public CareerMilestone(String title, String description, int estimatedMonths) {
        this.title = title;
        this.description = description;
        this.estimatedMonths = estimatedMonths;
    }
    
    // Getters and setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public int getEstimatedMonths() { return estimatedMonths; }
    public void setEstimatedMonths(int estimatedMonths) { this.estimatedMonths = estimatedMonths; }
}

class InterviewPracticeSession {
    private String sessionId;
    private User user;
    private List<InterviewQuestion> questions;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private SessionStatus status;
    
    public enum SessionStatus {
        IN_PROGRESS,
        COMPLETED,
        ABANDONED
    }
    
    // Getters and setters
    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }
    
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    
    public List<InterviewQuestion> getQuestions() { return questions; }
    public void setQuestions(List<InterviewQuestion> questions) { this.questions = questions; }
    
    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    
    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
    
    public SessionStatus getStatus() { return status; }
    public void setStatus(SessionStatus status) { this.status = status; }
}

class AIFeedback {
    private String feedbackText;
    private int score;
    
    // Getters and setters
    public String getFeedbackText() { return feedbackText; }
    public void setFeedbackText(String feedbackText) { this.feedbackText = feedbackText; }
    
    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }
}

class InterviewPerformanceAnalysis {
    private int totalResponses;
    private double averageScore;
    private Map<InterviewQuestion.QuestionCategory, Double> categoryPerformance;
    private List<String> improvementAreas;
    private Map<String, Double> progressOverTime;
    
    // Getters and setters
    public int getTotalResponses() { return totalResponses; }
    public void setTotalResponses(int totalResponses) { this.totalResponses = totalResponses; }
    
    public double getAverageScore() { return averageScore; }
    public void setAverageScore(double averageScore) { this.averageScore = averageScore; }
    
    public Map<InterviewQuestion.QuestionCategory, Double> getCategoryPerformance() { return categoryPerformance; }
    public void setCategoryPerformance(Map<InterviewQuestion.QuestionCategory, Double> categoryPerformance) { 
        this.categoryPerformance = categoryPerformance; 
    }
    
    public List<String> getImprovementAreas() { return improvementAreas; }
    public void setImprovementAreas(List<String> improvementAreas) { this.improvementAreas = improvementAreas; }
    
    public Map<String, Double> getProgressOverTime() { return progressOverTime; }
    public void setProgressOverTime(Map<String, Double> progressOverTime) { this.progressOverTime = progressOverTime; }
}
