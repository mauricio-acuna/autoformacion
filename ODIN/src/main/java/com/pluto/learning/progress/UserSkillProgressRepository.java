package com.pluto.learning.progress;

import com.pluto.learning.auth.User;
import com.pluto.learning.content.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserSkillProgressRepository extends JpaRepository<UserSkillProgress, Long> {
    
    /**
     * Buscar progreso por usuario y habilidad
     */
    Optional<UserSkillProgress> findByUserAndSkill(User user, Skill skill);
    
    /**
     * Obtener progreso de todas las habilidades de un usuario
     */
    List<UserSkillProgress> findByUserOrderByUpdatedAtDesc(User user);
    
    /**
     * Obtener progreso por estado
     */
    List<UserSkillProgress> findByUserAndStatus(User user, UserSkillProgress.ProgressStatus status);
    
    /**
     * Obtener habilidades iniciadas por usuario
     */
    @Query("SELECT p.skill.id FROM UserSkillProgress p WHERE p.user = :user AND p.status != 'NOT_STARTED'")
    List<Long> findStartedSkillIdsByUser(@Param("user") User user);
    
    /**
     * Obtener progreso de habilidades completadas
     */
    @Query("SELECT p FROM UserSkillProgress p WHERE p.user = :user AND p.status IN ('COMPLETED', 'MASTERED') ORDER BY p.completedAt DESC")
    List<UserSkillProgress> findCompletedSkillsByUser(@Param("user") User user);
    
    /**
     * Contar habilidades por estado
     */
    int countByUserAndStatus(User user, UserSkillProgress.ProgressStatus status);
    
    /**
     * Obtener top usuarios por puntos
     */
    @Query("SELECT p.user, SUM(p.totalPointsEarned) as totalPoints FROM UserSkillProgress p GROUP BY p.user ORDER BY totalPoints DESC")
    List<Object[]> findTopUsersByPoints();
    
    /**
     * Obtener progreso reciente (últimas actualizaciones)
     */
    @Query("SELECT p FROM UserSkillProgress p WHERE p.lastActivityAt IS NOT NULL ORDER BY p.lastActivityAt DESC")
    List<UserSkillProgress> findRecentProgressUpdates();
    
    /**
     * Verificar si usuario ha completado una habilidad
     */
    @Query("SELECT COUNT(p) > 0 FROM UserSkillProgress p WHERE p.user = :user AND p.skill = :skill AND p.status IN ('COMPLETED', 'MASTERED')")
    boolean hasUserCompletedSkill(@Param("user") User user, @Param("skill") Skill skill);
}
