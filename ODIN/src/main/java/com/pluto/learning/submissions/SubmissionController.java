package com.pluto.learning.submissions;

import com.pluto.learning.assessments.Lab;
import com.pluto.learning.assessments.LabRepository;
import com.pluto.learning.auth.User;
import com.pluto.learning.auth.UserService;
import com.pluto.learning.submissions.dto.CreateSubmissionRequest;
import com.pluto.learning.submissions.dto.SubmissionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/submissions")
@Tag(name = "Submissions", description = "API para gestión de envíos de laboratorios")
public class SubmissionController {
    
    private final SubmissionService submissionService;
    private final LabRepository labRepository;
    private final UserService userService;
    
    @Autowired
    public SubmissionController(SubmissionService submissionService, LabRepository labRepository, UserService userService) {
        this.submissionService = submissionService;
        this.labRepository = labRepository;
        this.userService = userService;
    }
    
    @PostMapping("/labs/{labId}")
    @Operation(summary = "Crear nuevo envío de laboratorio")
    @ApiResponse(responseCode = "201", description = "Envío creado exitosamente")
    @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    @ApiResponse(responseCode = "404", description = "Laboratorio no encontrado")
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    public ResponseEntity<SubmissionResponse> createSubmission(
            @Parameter(description = "ID del laboratorio") @PathVariable Long labId,
            @Valid @RequestBody CreateSubmissionRequest request,
            Authentication authentication) {
        
        Lab lab = labRepository.findById(labId)
            .orElseThrow(() -> new IllegalArgumentException("Laboratorio no encontrado"));
        
        User user = userService.getCurrentUser(authentication);
        
        SubmissionResponse response = submissionService.createSubmission(request, lab, user);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    @GetMapping("/labs/{labId}/my-submissions")
    @Operation(summary = "Obtener mis envíos para un laboratorio")
    @ApiResponse(responseCode = "200", description = "Lista de envíos del usuario")
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    public ResponseEntity<List<SubmissionResponse>> getMySubmissions(
            @Parameter(description = "ID del laboratorio") @PathVariable Long labId,
            Authentication authentication) {
        
        Lab lab = labRepository.findById(labId)
            .orElseThrow(() -> new IllegalArgumentException("Laboratorio no encontrado"));
        
        User user = userService.getCurrentUser(authentication);
        
        List<SubmissionResponse> submissions = submissionService.getSubmissionsByLabAndUser(lab, user);
        return ResponseEntity.ok(submissions);
    }
    
    @GetMapping("/{submissionId}")
    @Operation(summary = "Obtener detalles de un envío específico")
    @ApiResponse(responseCode = "200", description = "Detalles del envío")
    @ApiResponse(responseCode = "404", description = "Envío no encontrado")
    public ResponseEntity<SubmissionResponse> getSubmission(
            @Parameter(description = "ID del envío") @PathVariable Long submissionId) {
        
        return submissionService.getSubmissionById(submissionId)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/pending-review")
    @Operation(summary = "Obtener envíos pendientes de revisión manual")
    @ApiResponse(responseCode = "200", description = "Lista de envíos pendientes de revisión")
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<List<SubmissionResponse>> getPendingReviewSubmissions() {
        List<SubmissionResponse> submissions = submissionService.getPendingReviewSubmissions();
        return ResponseEntity.ok(submissions);
    }
    
    @PutMapping("/{submissionId}/manual-score")
    @Operation(summary = "Actualizar puntuación manual de un envío")
    @ApiResponse(responseCode = "200", description = "Puntuación actualizada exitosamente")
    @ApiResponse(responseCode = "404", description = "Envío no encontrado")
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<SubmissionResponse> updateManualScore(
            @Parameter(description = "ID del envío") @PathVariable Long submissionId,
            @RequestBody ManualScoreRequest request) {
        
        SubmissionResponse response = submissionService.updateManualScore(
            submissionId, request.getScore(), request.getFeedback()
        );
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/labs/{labId}/statistics")
    @Operation(summary = "Obtener estadísticas de envíos para un laboratorio")
    @ApiResponse(responseCode = "200", description = "Estadísticas del laboratorio")
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<SubmissionService.SubmissionStatistics> getSubmissionStatistics(
            @Parameter(description = "ID del laboratorio") @PathVariable Long labId) {
        
        Lab lab = labRepository.findById(labId)
            .orElseThrow(() -> new IllegalArgumentException("Laboratorio no encontrado"));
        
        SubmissionService.SubmissionStatistics statistics = submissionService.getSubmissionStatistics(lab);
        return ResponseEntity.ok(statistics);
    }
    
    // Webhook para actualizar puntuación automática (llamado por sistema de evaluación)
    @PutMapping("/{submissionId}/automated-score")
    @Operation(summary = "Actualizar puntuación automática (webhook interno)")
    @ApiResponse(responseCode = "200", description = "Puntuación automática actualizada")
    @PreAuthorize("hasRole('SYSTEM') or hasRole('ADMIN')")
    public ResponseEntity<SubmissionResponse> updateAutomatedScore(
            @PathVariable Long submissionId,
            @RequestBody AutomatedScoreRequest request) {
        
        SubmissionResponse response = submissionService.updateAutomatedScore(
            submissionId, request.getScore(), request.getTestResults(), request.getLogs()
        );
        return ResponseEntity.ok(response);
    }
    
    // DTOs internos para requests
    public static class ManualScoreRequest {
        private Integer score;
        private String feedback;
        
        public Integer getScore() { return score; }
        public void setScore(Integer score) { this.score = score; }
        public String getFeedback() { return feedback; }
        public void setFeedback(String feedback) { this.feedback = feedback; }
    }
    
    public static class AutomatedScoreRequest {
        private Integer score;
        private String testResults;
        private String logs;
        
        public Integer getScore() { return score; }
        public void setScore(Integer score) { this.score = score; }
        public String getTestResults() { return testResults; }
        public void setTestResults(String testResults) { this.testResults = testResults; }
        public String getLogs() { return logs; }
        public void setLogs(String logs) { this.logs = logs; }
    }
}
