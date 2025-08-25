package com.pluto.learning.career;

import com.pluto.learning.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository para gestión de respuestas de entrevista
 */
@Repository
public interface InterviewResponseRepository extends JpaRepository<InterviewResponse, Long> {
    
    /**
     * Encuentra respuestas por usuario ordenadas por fecha descendente
     */
    List<InterviewResponse> findByUserOrderByCreatedAtDesc(User user);
    
    /**
     * Encuentra respuestas por ID de usuario
     */
    List<InterviewResponse> findByUserId(Long userId);
    
    /**
     * Encuentra respuestas por ID de usuario ordenadas por fecha
     */
    List<InterviewResponse> findByUserIdOrderByCreatedAtDesc(Long userId);
    
    /**
     * Encuentra respuestas por sesión de práctica
     */
    List<InterviewResponse> findByPracticeSessionIdOrderByCreatedAt(String practiceSessionId);
    
    /**
     * Encuentra respuestas por usuario y pregunta
     */
    List<InterviewResponse> findByUserAndQuestion(User user, InterviewQuestion question);
    
    /**
     * Encuentra respuestas por usuario en un rango de fechas
     */
    List<InterviewResponse> findByUserAndCreatedAtBetween(User user, LocalDateTime start, LocalDateTime end);
    
    /**
     * Calcula el score promedio por usuario
     */
    @Query("SELECT AVG(ir.aiScore) FROM InterviewResponse ir WHERE ir.user = :user AND ir.aiScore IS NOT NULL")
    Double findAverageScoreByUser(@Param("user") User user);
    
    /**
     * Cuenta respuestas por usuario
     */
    long countByUser(User user);
    
    /**
     * Encuentra las últimas N respuestas de un usuario
     */
    @Query("SELECT ir FROM InterviewResponse ir WHERE ir.user = :user ORDER BY ir.createdAt DESC")
    List<InterviewResponse> findTopByUserOrderByCreatedAtDesc(@Param("user") User user, @Param("limit") int limit);
    
    /**
     * Estadísticas por categoría de pregunta
     */
    @Query("SELECT ir.question.category, AVG(ir.aiScore) FROM InterviewResponse ir WHERE ir.user = :user AND ir.aiScore IS NOT NULL GROUP BY ir.question.category")
    List<Object[]> findCategoryAveragesByUser(@Param("user") User user);
}
