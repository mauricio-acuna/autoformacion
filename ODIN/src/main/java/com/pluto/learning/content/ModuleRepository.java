package com.pluto.learning.content;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModuleRepository extends JpaRepository<Module, Long> {
    
    List<Module> findBySkillIdOrderByOrderIndex(Long skillId);
    
    @Query("SELECT m FROM Module m WHERE m.skill.id = :skillId AND m.orderIndex = :orderIndex")
    Module findBySkillIdAndOrderIndex(@Param("skillId") Long skillId, 
                                      @Param("orderIndex") Integer orderIndex);
    
    @Query("SELECT m FROM Module m WHERE SIZE(m.prerequisites) = 0 ORDER BY m.orderIndex")
    List<Module> findModulesWithoutPrerequisites();
    
    @Query("SELECT m FROM Module m JOIN m.prerequisites p WHERE p.id = :prerequisiteId")
    List<Module> findModulesByPrerequisite(@Param("prerequisiteId") Long prerequisiteId);
    
    /**
     * Contar módulos por habilidad y estado activo
     */
    int countBySkillAndActiveTrue(Skill skill);
}
