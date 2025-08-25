package com.pluto.learning.assessments;

import com.pluto.learning.content.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LabRepository extends JpaRepository<Lab, Long> {
    
    /**
     * Obtener laboratorios por módulo
     */
    List<Lab> findByModuleAndActiveTrue(Module module);
    
    /**
     * Obtener laboratorios activos ordenados por ID
     */
    List<Lab> findByActiveTrueOrderById();
    
    /**
     * Buscar laboratorios por título (búsqueda parcial)
     */
    @Query("SELECT l FROM Lab l WHERE l.active = true AND LOWER(l.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    List<Lab> findByTitleContainingIgnoreCaseAndActiveTrue(@Param("title") String title);
    
    /**
     * Obtener laboratorios por tipo de evaluación
     */
    List<Lab> findByEvaluationTypeAndActiveTrue(Lab.EvaluationType evaluationType);
    
    /**
     * Contar laboratorios por módulo
     */
    int countByModuleAndActiveTrue(Module module);
    
    /**
     * Obtener laboratorios con evaluación automática
     */
    @Query("SELECT l FROM Lab l WHERE l.active = true AND l.evaluationType IN ('AUTOMATED', 'HYBRID')")
    List<Lab> findLabsWithAutomatedEvaluation();
    
    /**
     * Contar laboratorios por habilidad (a través del módulo)
     */
    @Query("SELECT COUNT(l) FROM Lab l WHERE l.module.skill = :skill AND l.active = true")
    int countByModuleSkillAndActiveTrue(@Param("skill") com.pluto.learning.content.Skill skill);
    
    /**
     * Sumar puntos totales de laboratorios por habilidad
     */
    @Query("SELECT COALESCE(SUM(l.points), 0) FROM Lab l WHERE l.module.skill = :skill AND l.active = true")
    int sumPointsBySkill(@Param("skill") com.pluto.learning.content.Skill skill);
}
