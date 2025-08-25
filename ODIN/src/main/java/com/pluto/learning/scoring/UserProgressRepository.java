package com.pluto.learning.scoring;

import com.pluto.learning.auth.User;
import com.pluto.learning.content.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserProgressRepository extends JpaRepository<UserProgress, Long> {
    
    /**
     * Obtener progreso del usuario en una skill específica
     */
    Optional<UserProgress> findByUserAndSkill(User user, Skill skill);
    
    /**
     * Obtener todo el progreso del usuario ordenado por última actividad
     */
    List<UserProgress> findByUserOrderByLastActivityAtDesc(User user);
    
    /**
     * Obtener progreso por usuario y estado
     */
    List<UserProgress> findByUserAndStatus(User user, UserProgress.ProgressStatus status);
    
    /**
     * Obtener usuarios que han comenzado una skill
     */
    List<UserProgress> findBySkillAndStatusNot(Skill skill, UserProgress.ProgressStatus status);
    
    /**
     * Contar usuarios que han completado una skill
     */
    @Query("SELECT COUNT(p) FROM UserProgress p WHERE p.skill = :skill AND p.status IN ('COMPLETED', 'MASTERED')")
    long countCompletedBySkill(@Param("skill") Skill skill);
    
    /**
     * Obtener promedio de completitud por skill
     */
    @Query("SELECT AVG(p.completionPercentage) FROM UserProgress p WHERE p.skill = :skill AND p.status != 'NOT_STARTED'")
    Double findAverageCompletionBySkill(@Param("skill") Skill skill);
    
    /**
     * Obtener top usuarios por puntos (leaderboard)
     */
    @Query("""
        SELECT new com.pluto.learning.scoring.ScoringService$LeaderboardEntry(
            u.id, u.name, SUM(p.totalPointsEarned), 
            COUNT(CASE WHEN p.status IN ('COMPLETED', 'MASTERED') THEN 1 END),
            ROW_NUMBER() OVER (ORDER BY SUM(p.totalPointsEarned) DESC)
        )
        FROM UserProgress p JOIN p.user u 
        WHERE p.status != 'NOT_STARTED'
        GROUP BY u.id, u.name 
        ORDER BY SUM(p.totalPointsEarned) DESC
        LIMIT :limit
    """)
    List<ScoringService.LeaderboardEntry> findTopUsersByPoints(@Param("limit") int limit);
    
    /**
     * Obtener usuarios más activos (por tiempo total)
     */
    @Query("""
        SELECT p FROM UserProgress p 
        WHERE p.status != 'NOT_STARTED' 
        ORDER BY p.totalTimeSpentHours DESC, p.lastActivityAt DESC
    """)
    List<UserProgress> findMostActiveUsers();
    
    /**
     * Obtener progreso de skill más reciente por usuario
     */
    @Query("SELECT p FROM UserProgress p WHERE p.user = :user ORDER BY p.lastActivityAt DESC LIMIT 1")
    Optional<UserProgress> findMostRecentProgressByUser(@Param("user") User user);
    
    /**
     * Obtener estadísticas globales de progreso
     */
    @Query("""
        SELECT new map(
            COUNT(p) as totalProgress,
            COUNT(CASE WHEN p.status = 'COMPLETED' THEN 1 END) as completed,
            COUNT(CASE WHEN p.status = 'MASTERED' THEN 1 END) as mastered,
            AVG(p.completionPercentage) as avgCompletion,
            AVG(p.masteryScore) as avgMastery
        )
        FROM UserProgress p WHERE p.status != 'NOT_STARTED'
    """)
    java.util.Map<String, Object> getGlobalProgressStatistics();
}
