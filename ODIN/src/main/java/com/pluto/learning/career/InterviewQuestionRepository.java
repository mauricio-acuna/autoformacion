package com.pluto.learning.career;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository para gestión de preguntas de entrevista
 */
@Repository
public interface InterviewQuestionRepository extends JpaRepository<InterviewQuestion, Long> {
    
    /**
     * Encuentra preguntas activas ordenadas aleatoriamente
     */
    @Query(value = "SELECT * FROM interview_questions WHERE active = true ORDER BY RANDOM() LIMIT :count", 
           nativeQuery = true)
    List<InterviewQuestion> findByActiveTrueOrderByRandom(@Param("count") int count);
    
    /**
     * Encuentra preguntas activas
     */
    List<InterviewQuestion> findByActive(Boolean active);
    
    /**
     * Encuentra preguntas por categoría y nivel de dificultad
     */
    @Query("SELECT iq FROM InterviewQuestion iq WHERE iq.category = :category AND iq.difficultyLevel = :level AND iq.active = true ORDER BY FUNCTION('RANDOM')")
    List<InterviewQuestion> findByCategoryAndDifficultyLevel(
        @Param("category") InterviewQuestion.QuestionCategory category,
        @Param("level") InterviewQuestion.DifficultyLevel level,
        @Param("limit") int limit
    );
    
    /**
     * Encuentra preguntas por nivel de dificultad
     */
    @Query("SELECT iq FROM InterviewQuestion iq WHERE iq.difficultyLevel = :level AND iq.active = true ORDER BY FUNCTION('RANDOM')")
    List<InterviewQuestion> findByDifficultyLevelAndActiveTrue(
        @Param("level") InterviewQuestion.DifficultyLevel level,
        @Param("limit") int limit
    );
    
    /**
     * Encuentra preguntas por IDs
     */
    List<InterviewQuestion> findByIdIn(List<Long> ids);
    
    /**
     * Encuentra preguntas por categoría
     */
    List<InterviewQuestion> findByCategoryAndActiveTrue(InterviewQuestion.QuestionCategory category);
    
    /**
     * Encuentra preguntas por tags
     */
    @Query("SELECT iq FROM InterviewQuestion iq WHERE iq.active = true AND iq.tags LIKE %:tag%")
    List<InterviewQuestion> findByTagsContaining(@Param("tag") String tag);
    
    /**
     * Cuenta preguntas por categoría
     */
    long countByCategoryAndActiveTrue(InterviewQuestion.QuestionCategory category);
    
    /**
     * Cuenta preguntas por nivel de dificultad
     */
    long countByDifficultyLevelAndActiveTrue(InterviewQuestion.DifficultyLevel level);
}
