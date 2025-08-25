package com.pluto.learning.submissions;

import com.pluto.learning.assessments.Lab;
import com.pluto.learning.auth.User;
import com.pluto.learning.submissions.dto.CreateSubmissionRequest;
import com.pluto.learning.submissions.dto.SubmissionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SubmissionService {
    
    private final SubmissionRepository submissionRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    
    @Autowired
    public SubmissionService(SubmissionRepository submissionRepository, KafkaTemplate<String, Object> kafkaTemplate) {
        this.submissionRepository = submissionRepository;
        this.kafkaTemplate = kafkaTemplate;
    }
    
    /**
     * Crear un nuevo envío de laboratorio
     */
    public SubmissionResponse createSubmission(CreateSubmissionRequest request, Lab lab, User user) {
        // Validar número de intentos
        int attemptCount = submissionRepository.countByLabAndUser(lab, user);
        if (attemptCount >= lab.getMaxAttempts()) {
            throw new IllegalStateException("Se ha superado el número máximo de intentos para este laboratorio");
        }
        
        // Crear nueva submission
        LabSubmission submission = new LabSubmission(lab, user, request.getGithubRepoUrl());
        submission.setCommitHash(request.getCommitHash());
        submission.setBranchName(request.getBranchName());
        submission.setAttemptNumber(attemptCount + 1);
        
        submission = submissionRepository.save(submission);
        
        // Enviar evento para evaluación automática
        sendEvaluationEvent(submission);
        
        return mapToResponse(submission);
    }
    
    /**
     * Obtener envíos por laboratorio y usuario
     */
    @Transactional(readOnly = true)
    public List<SubmissionResponse> getSubmissionsByLabAndUser(Lab lab, User user) {
        List<LabSubmission> submissions = submissionRepository.findByLabAndUserOrderByAttemptNumberDesc(lab, user);
        return submissions.stream().map(this::mapToResponse).toList();
    }
    
    /**
     * Obtener envío por ID
     */
    @Transactional(readOnly = true)
    public Optional<SubmissionResponse> getSubmissionById(Long id) {
        return submissionRepository.findById(id).map(this::mapToResponse);
    }
    
    /**
     * Obtener envíos pendientes de revisión manual
     */
    @Transactional(readOnly = true)
    public List<SubmissionResponse> getPendingReviewSubmissions() {
        List<LabSubmission> submissions = submissionRepository.findByStatusOrderBySubmittedAtAsc(
            LabSubmission.SubmissionStatus.REVIEW_PENDING
        );
        return submissions.stream().map(this::mapToResponse).toList();
    }
    
    /**
     * Actualizar puntuación automática
     */
    public SubmissionResponse updateAutomatedScore(Long submissionId, Integer score, String testResults, String logs) {
        LabSubmission submission = submissionRepository.findById(submissionId)
            .orElseThrow(() -> new IllegalArgumentException("Submission no encontrada"));
        
        submission.setAutomatedScore(score);
        submission.setTestResults(testResults);
        submission.setExecutionLogs(logs);
        submission.setStatus(LabSubmission.SubmissionStatus.EVALUATED);
        submission.setEvaluatedAt(LocalDateTime.now());
        
        // Calcular puntuación final según tipo de evaluación
        calculateFinalScore(submission);
        
        submission = submissionRepository.save(submission);
        return mapToResponse(submission);
    }
    
    /**
     * Actualizar puntuación manual por instructor
     */
    public SubmissionResponse updateManualScore(Long submissionId, Integer score, String feedback) {
        LabSubmission submission = submissionRepository.findById(submissionId)
            .orElseThrow(() -> new IllegalArgumentException("Submission no encontrada"));
        
        submission.setManualScore(score);
        submission.setInstructorFeedback(feedback);
        submission.setStatus(LabSubmission.SubmissionStatus.REVIEWED);
        submission.setEvaluatedAt(LocalDateTime.now());
        
        // Calcular puntuación final
        calculateFinalScore(submission);
        
        // Actualizar estado final
        if (submission.isPassed()) {
            submission.setStatus(LabSubmission.SubmissionStatus.APPROVED);
        } else {
            submission.setStatus(LabSubmission.SubmissionStatus.REJECTED);
        }
        
        submission = submissionRepository.save(submission);
        return mapToResponse(submission);
    }
    
    /**
     * Obtener estadísticas de envíos por laboratorio
     */
    @Transactional(readOnly = true)
    public SubmissionStatistics getSubmissionStatistics(Lab lab) {
        int totalSubmissions = submissionRepository.countByLab(lab);
        int approvedSubmissions = submissionRepository.countByLabAndStatus(lab, LabSubmission.SubmissionStatus.APPROVED);
        int pendingReview = submissionRepository.countByLabAndStatus(lab, LabSubmission.SubmissionStatus.REVIEW_PENDING);
        
        Double averageScore = submissionRepository.findAverageScoreByLab(lab);
        
        return new SubmissionStatistics(totalSubmissions, approvedSubmissions, pendingReview, averageScore);
    }
    
    // Métodos privados
    private void sendEvaluationEvent(LabSubmission submission) {
        SubmissionEvaluationEvent event = new SubmissionEvaluationEvent(
            submission.getId(),
            submission.getGithubRepoUrl(),
            submission.getCommitHash(),
            submission.getBranchName(),
            submission.getLab().getEvaluationType()
        );
        
        kafkaTemplate.send("submission-evaluation", event);
    }
    
    private void calculateFinalScore(LabSubmission submission) {
        Lab.EvaluationType evaluationType = submission.getLab().getEvaluationType();
        
        switch (evaluationType) {
            case AUTOMATED:
                submission.setFinalScore(submission.getAutomatedScore());
                break;
            case MANUAL:
                submission.setFinalScore(submission.getManualScore());
                break;
            case HYBRID:
                // Promedio ponderado: 60% automático, 40% manual
                Integer autoScore = submission.getAutomatedScore() != null ? submission.getAutomatedScore() : 0;
                Integer manualScore = submission.getManualScore() != null ? submission.getManualScore() : 0;
                submission.setFinalScore((int) (autoScore * 0.6 + manualScore * 0.4));
                break;
        }
    }
    
    private SubmissionResponse mapToResponse(LabSubmission submission) {
        return new SubmissionResponse(
            submission.getId(),
            submission.getLab().getId(),
            submission.getUser().getId(),
            submission.getGithubRepoUrl(),
            submission.getCommitHash(),
            submission.getBranchName(),
            submission.getStatus(),
            submission.getAutomatedScore(),
            submission.getManualScore(),
            submission.getFinalScore(),
            submission.getTestResults(),
            submission.getInstructorFeedback(),
            submission.getAttemptNumber(),
            submission.getSubmittedAt(),
            submission.getEvaluatedAt(),
            submission.isPassed()
        );
    }
    
    // Clases auxiliares
    public static class SubmissionStatistics {
        private final int totalSubmissions;
        private final int approvedSubmissions;
        private final int pendingReview;
        private final Double averageScore;
        
        public SubmissionStatistics(int totalSubmissions, int approvedSubmissions, int pendingReview, Double averageScore) {
            this.totalSubmissions = totalSubmissions;
            this.approvedSubmissions = approvedSubmissions;
            this.pendingReview = pendingReview;
            this.averageScore = averageScore;
        }
        
        // Getters
        public int getTotalSubmissions() { return totalSubmissions; }
        public int getApprovedSubmissions() { return approvedSubmissions; }
        public int getPendingReview() { return pendingReview; }
        public Double getAverageScore() { return averageScore; }
        public double getApprovalRate() { 
            return totalSubmissions > 0 ? (double) approvedSubmissions / totalSubmissions * 100 : 0;
        }
    }
    
    public static class SubmissionEvaluationEvent {
        private final Long submissionId;
        private final String githubRepoUrl;
        private final String commitHash;
        private final String branchName;
        private final Lab.EvaluationType evaluationType;
        
        public SubmissionEvaluationEvent(Long submissionId, String githubRepoUrl, String commitHash, String branchName, Lab.EvaluationType evaluationType) {
            this.submissionId = submissionId;
            this.githubRepoUrl = githubRepoUrl;
            this.commitHash = commitHash;
            this.branchName = branchName;
            this.evaluationType = evaluationType;
        }
        
        // Getters
        public Long getSubmissionId() { return submissionId; }
        public String getGithubRepoUrl() { return githubRepoUrl; }
        public String getCommitHash() { return commitHash; }
        public String getBranchName() { return branchName; }
        public Lab.EvaluationType getEvaluationType() { return evaluationType; }
    }
}
