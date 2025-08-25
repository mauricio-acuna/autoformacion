package com.pluto.learning.content;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {
    
    Optional<Skill> findByName(String name);
    
    List<Skill> findByCategory(SkillCategory category);
    
    List<Skill> findByLevel(SkillLevel level);
    
    @Query("SELECT s FROM Skill s WHERE s.category = :category AND s.level = :level")
    List<Skill> findByCategoryAndLevel(@Param("category") SkillCategory category, 
                                       @Param("level") SkillLevel level);
    
    @Query("SELECT s FROM Skill s ORDER BY s.category, s.level, s.name")
    List<Skill> findAllOrderedByCategoryAndLevel();
    
    /**
     * Obtener habilidades disponibles para un usuario (excluyendo las ya iniciadas)
     */
    @Query("SELECT s FROM Skill s WHERE s.id NOT IN :excludedIds AND s.active = true ORDER BY s.difficultyScore ASC")
    List<Skill> findAvailableSkillsForUser(@Param("excludedIds") List<Long> excludedIds);
}
