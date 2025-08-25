package com.pluto.learning.assessments;

import com.pluto.learning.content.Module;
import com.pluto.learning.content.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {
    
    /**
     * Obtener quizzes por módulo
     */
    List<Quiz> findByModuleAndActiveTrue(Module module);
    
    /**
     * Obtener quizzes activos ordenados por ID
     */
    List<Quiz> findByActiveTrueOrderById();
    
    /**
     * Buscar quizzes por título (búsqueda parcial)
     */
    @Query("SELECT q FROM Quiz q WHERE q.active = true AND LOWER(q.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    List<Quiz> findByTitleContainingIgnoreCaseAndActiveTrue(@Param("title") String title);
    
    /**
     * Contar quizzes por módulo
     */
    int countByModuleAndActiveTrue(Module module);
    
    /**
     * Contar quizzes por habilidad (a través del módulo)
     */
    @Query("SELECT COUNT(q) FROM Quiz q WHERE q.module.skill = :skill AND q.active = true")
    int countByModuleSkillAndActiveTrue(@Param("skill") Skill skill);
    
    /**
     * Sumar puntos totales de quizzes por habilidad
     */
    @Query("SELECT COALESCE(SUM(q.passingScore), 0) FROM Quiz q WHERE q.module.skill = :skill AND q.active = true")
    int sumPointsBySkill(@Param("skill") Skill skill);
    
    /**
     * Obtener quizzes por habilidad
     */
    @Query("SELECT q FROM Quiz q WHERE q.module.skill = :skill AND q.active = true ORDER BY q.module.moduleOrder")
    List<Quiz> findBySkillOrderByModuleOrder(@Param("skill") Skill skill);
}
