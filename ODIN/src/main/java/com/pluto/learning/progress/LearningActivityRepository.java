package com.pluto.learning.progress;

import com.pluto.learning.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Repository
public interface LearningActivityRepository extends JpaRepository<LearningActivity, Long> {
    
    /**
     * Obtener actividades de un usuario ordenadas por fecha descendente
     */
    List<LearningActivity> findByUserOrderByCreatedAtDesc(User user);
    
    /**
     * Obtener actividades por tipo
     */
    List<LearningActivity> findByUserAndActivityTypeOrderByCreatedAtDesc(User user, LearningActivity.ActivityType activityType);
    
    /**
     * Obtener actividades en un rango de fechas
     */
    @Query("SELECT a FROM LearningActivity a WHERE a.user = :user AND a.createdAt BETWEEN :startDate AND :endDate ORDER BY a.createdAt DESC")
    List<LearningActivity> findByUserAndDateRange(@Param("user") User user, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    /**
     * Contar días únicos de actividad de un usuario
     */
    @Query("SELECT COUNT(DISTINCT DATE(a.createdAt)) FROM LearningActivity a WHERE a.user = :user")
    int countDistinctActivityDays(@Param("user") User user);
    
    /**
     * Obtener fecha de última actividad
     */
    @Query("SELECT MAX(a.createdAt) FROM LearningActivity a WHERE a.user = :user")
    LocalDateTime findLastActivityDate(@Param("user") User user);
    
    /**
     * Obtener actividades recientes del sistema
     */
    @Query("SELECT a FROM LearningActivity a ORDER BY a.createdAt DESC")
    List<LearningActivity> findRecentSystemActivities();
    
    /**
     * Contar actividades por tipo en un período
     */
    @Query("SELECT COUNT(a) FROM LearningActivity a WHERE a.user = :user AND a.activityType = :type AND a.createdAt >= :since")
    int countByUserAndActivityTypeSince(@Param("user") User user, @Param("type") LearningActivity.ActivityType type, @Param("since") LocalDateTime since);
    
    /**
     * Obtener total de puntos ganados por usuario
     */
    @Query("SELECT COALESCE(SUM(a.pointsEarned), 0) FROM LearningActivity a WHERE a.user = :user AND a.pointsEarned IS NOT NULL")
    int sumPointsEarnedByUser(@Param("user") User user);
    
    /**
     * Obtener total de tiempo invertido por usuario
     */
    @Query("SELECT COALESCE(SUM(a.timeSpentMinutes), 0) FROM LearningActivity a WHERE a.user = :user AND a.timeSpentMinutes IS NOT NULL")
    int sumTimeSpentByUser(@Param("user") User user);
    
    /**
     * Obtener actividades por entidad específica
     */
    List<LearningActivity> findByEntityIdAndEntityTypeOrderByCreatedAtDesc(Long entityId, String entityType);
    
    /**
     * Obtener estadísticas de actividad por día
     */
    @Query("SELECT DATE(a.createdAt), COUNT(a) FROM LearningActivity a WHERE a.user = :user GROUP BY DATE(a.createdAt) ORDER BY DATE(a.createdAt) DESC")
    List<Object[]> findDailyActivityStats(@Param("user") User user);
    
    /**
     * Obtener actividades de hoy por usuario
     */
    @Query("SELECT a FROM LearningActivity a WHERE a.user = :user AND DATE(a.createdAt) = CURRENT_DATE ORDER BY a.createdAt DESC")
    List<LearningActivity> findTodayActivitiesByUser(@Param("user") User user);
    
    /**
     * Obtener estadísticas de actividades por usuario
     */
    @Query("SELECT new map(a.activityType as type, COUNT(a) as count, COALESCE(SUM(a.pointsEarned), 0) as totalPoints) " +
           "FROM LearningActivity a WHERE a.user = :user GROUP BY a.activityType")
    List<Map<String, Object>> getActivityStatisticsByUser(@Param("user") User user);
    
    /**
     * Obtener fechas distintas de actividades por usuario
     */
    @Query("SELECT DISTINCT DATE(a.createdAt) FROM LearningActivity a WHERE a.user = :user ORDER BY DATE(a.createdAt) DESC")
    List<LocalDateTime> findDistinctActivityDatesByUser(@Param("user") User user);
    
    /**
     * Obtener actividades relacionadas a una skill específica
     */
    @Query("SELECT a FROM LearningActivity a WHERE a.user = :user AND (a.entityId = :skillId AND a.entityType = 'SKILL') ORDER BY a.createdAt DESC")
    List<LearningActivity> findByUserAndSkillRelated(@Param("user") User user, @Param("skillId") Long skillId);
}
