package com.pluto.learning.scoring;

import com.pluto.learning.auth.User;
import com.pluto.learning.auth.UserService;
import com.pluto.learning.content.Skill;
import com.pluto.learning.content.SkillRepository;
import com.pluto.learning.progress.LearningActivity;
import com.pluto.learning.progress.LearningActivityRepository;
import com.pluto.learning.scoring.dto.ActivityRequest;
import com.pluto.learning.scoring.dto.ProgressSummary;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/progress")
@Tag(name = "Progress & Scoring", description = "API para seguimiento de progreso y puntuación")
public class ProgressController {
    
    private final ScoringService scoringService;
    private final UserService userService;
    private final SkillRepository skillRepository;
    private final UserProgressRepository progressRepository;
    private final LearningActivityRepository activityRepository;
    
    @Autowired
    public ProgressController(ScoringService scoringService, UserService userService, 
                             SkillRepository skillRepository, UserProgressRepository progressRepository,
                             LearningActivityRepository activityRepository) {
        this.scoringService = scoringService;
        this.userService = userService;
        this.skillRepository = skillRepository;
        this.progressRepository = progressRepository;
        this.activityRepository = activityRepository;
    }
    
    @PostMapping("/activities")
    @Operation(summary = "Registrar nueva actividad de aprendizaje")
    @ApiResponse(responseCode = "200", description = "Actividad registrada exitosamente")
    @PreAuthorize("hasRole('LEARNER') or hasRole('ADMIN')")
    public ResponseEntity<String> recordActivity(
            @Valid @RequestBody ActivityRequest request,
            Authentication authentication) {
        
        User user = userService.getCurrentUser(authentication);
        scoringService.recordLearningActivity(request, user);
        
        return ResponseEntity.ok("Actividad registrada exitosamente");
    }
    
    @GetMapping("/summary")
    @Operation(summary = "Obtener resumen completo de progreso del usuario")
    @ApiResponse(responseCode = "200", description = "Resumen de progreso")
    public ResponseEntity<ProgressSummary> getProgressSummary(Authentication authentication) {
        User user = userService.getCurrentUser(authentication);
        ProgressSummary summary = scoringService.getUserProgressSummary(user);
        return ResponseEntity.ok(summary);
    }
    
    @GetMapping("/skills/{skillId}")
    @Operation(summary = "Obtener progreso en una skill específica")
    @ApiResponse(responseCode = "200", description = "Progreso de la skill")
    @ApiResponse(responseCode = "404", description = "Skill no encontrada")
    public ResponseEntity<UserProgress> getSkillProgress(
            @Parameter(description = "ID de la skill") @PathVariable Long skillId,
            Authentication authentication) {
        
        User user = userService.getCurrentUser(authentication);
        Skill skill = skillRepository.findById(skillId)
            .orElseThrow(() -> new IllegalArgumentException("Skill no encontrada"));
        
        return scoringService.getUserProgress(user, skill)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/activities/recent")
    @Operation(summary = "Obtener actividades recientes del usuario")
    @ApiResponse(responseCode = "200", description = "Lista de actividades recientes")
    public ResponseEntity<List<LearningActivity>> getRecentActivities(
            @RequestParam(defaultValue = "10") int limit,
            Authentication authentication) {
        
        User user = userService.getCurrentUser(authentication);
        List<LearningActivity> activities = scoringService.getRecentActivities(user, limit);
        return ResponseEntity.ok(activities);
    }
    
    @GetMapping("/streak")
    @Operation(summary = "Obtener racha de días de actividad")
    @ApiResponse(responseCode = "200", description = "Número de días consecutivos de actividad")
    public ResponseEntity<Map<String, Integer>> getStreakDays(Authentication authentication) {
        User user = userService.getCurrentUser(authentication);
        int streak = scoringService.calculateStreakDays(user);
        return ResponseEntity.ok(Map.of("streakDays", streak));
    }
    
    @GetMapping("/leaderboard")
    @Operation(summary = "Obtener tabla de líderes por puntos")
    @ApiResponse(responseCode = "200", description = "Lista de usuarios ordenados por puntos")
    public ResponseEntity<List<ScoringService.LeaderboardEntry>> getLeaderboard(
            @RequestParam(defaultValue = "10") int limit) {
        
        List<ScoringService.LeaderboardEntry> leaderboard = scoringService.getLeaderboard(limit);
        return ResponseEntity.ok(leaderboard);
    }
    
    @PostMapping("/skills/{skillId}/recalculate")
    @Operation(summary = "Recalcular progreso de una skill")
    @ApiResponse(responseCode = "200", description = "Progreso recalculado exitosamente")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> recalculateProgress(
            @Parameter(description = "ID de la skill") @PathVariable Long skillId,
            Authentication authentication) {
        
        User user = userService.getCurrentUser(authentication);
        Skill skill = skillRepository.findById(skillId)
            .orElseThrow(() -> new IllegalArgumentException("Skill no encontrada"));
        
        scoringService.recalculateProgress(user, skill);
        return ResponseEntity.ok("Progreso recalculado exitosamente");
    }
    
    @GetMapping("/statistics")
    @Operation(summary = "Obtener estadísticas globales de progreso")
    @ApiResponse(responseCode = "200", description = "Estadísticas de progreso")
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getGlobalStatistics() {
        Map<String, Object> statistics = progressRepository.getGlobalProgressStatistics();
        return ResponseEntity.ok(statistics);
    }
    
    @GetMapping("/activities/today")
    @Operation(summary = "Obtener actividades del día de hoy")
    @ApiResponse(responseCode = "200", description = "Actividades del día actual")
    public ResponseEntity<List<LearningActivity>> getTodayActivities(Authentication authentication) {
        User user = userService.getCurrentUser(authentication);
        List<LearningActivity> activities = activityRepository.findTodayActivitiesByUser(user);
        return ResponseEntity.ok(activities);
    }
    
    @GetMapping("/activities/stats")
    @Operation(summary = "Obtener estadísticas de actividad por tipo")
    @ApiResponse(responseCode = "200", description = "Estadísticas de actividades del usuario")
    public ResponseEntity<List<Map<String, Object>>> getActivityStatistics(Authentication authentication) {
        User user = userService.getCurrentUser(authentication);
        List<Map<String, Object>> stats = activityRepository.getActivityStatisticsByUser(user);
        return ResponseEntity.ok(stats);
    }
    
    @GetMapping("/dashboard")
    @Operation(summary = "Obtener datos completos para dashboard")
    @ApiResponse(responseCode = "200", description = "Datos consolidados para dashboard")
    public ResponseEntity<DashboardData> getDashboardData(Authentication authentication) {
        User user = userService.getCurrentUser(authentication);
        
        ProgressSummary progressSummary = scoringService.getUserProgressSummary(user);
        List<LearningActivity> recentActivities = scoringService.getRecentActivities(user, 5);
        int streakDays = scoringService.calculateStreakDays(user);
        List<LearningActivity> todayActivities = activityRepository.findTodayActivitiesByUser(user);
        List<Map<String, Object>> activityStats = activityRepository.getActivityStatisticsByUser(user);
        
        DashboardData dashboard = new DashboardData(
            progressSummary,
            recentActivities,
            streakDays,
            todayActivities.size(),
            activityStats
        );
        
        return ResponseEntity.ok(dashboard);
    }
    
    // Clase para respuesta del dashboard
    public static class DashboardData {
        private final ProgressSummary progressSummary;
        private final List<LearningActivity> recentActivities;
        private final int streakDays;
        private final int todayActivitiesCount;
        private final List<Map<String, Object>> activityStatistics;
        
        public DashboardData(ProgressSummary progressSummary, List<LearningActivity> recentActivities,
                           int streakDays, int todayActivitiesCount, List<Map<String, Object>> activityStatistics) {
            this.progressSummary = progressSummary;
            this.recentActivities = recentActivities;
            this.streakDays = streakDays;
            this.todayActivitiesCount = todayActivitiesCount;
            this.activityStatistics = activityStatistics;
        }
        
        // Getters
        public ProgressSummary getProgressSummary() { return progressSummary; }
        public List<LearningActivity> getRecentActivities() { return recentActivities; }
        public int getStreakDays() { return streakDays; }
        public int getTodayActivitiesCount() { return todayActivitiesCount; }
        public List<Map<String, Object>> getActivityStatistics() { return activityStatistics; }
    }
}
