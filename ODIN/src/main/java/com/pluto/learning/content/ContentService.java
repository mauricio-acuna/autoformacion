package com.pluto.learning.content;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ContentService {
    
    private final SkillRepository skillRepository;
    private final ModuleRepository moduleRepository;
    private final LessonRepository lessonRepository;
    
    @Autowired
    public ContentService(SkillRepository skillRepository, 
                         ModuleRepository moduleRepository,
                         LessonRepository lessonRepository) {
        this.skillRepository = skillRepository;
        this.moduleRepository = moduleRepository;
        this.lessonRepository = lessonRepository;
    }
    
    // Skill management
    public List<Skill> getAllSkills() {
        return skillRepository.findAllOrderedByCategoryAndLevel();
    }
    
    public List<Skill> getSkillsByCategory(SkillCategory category) {
        return skillRepository.findByCategory(category);
    }
    
    public Optional<Skill> getSkillById(Long id) {
        return skillRepository.findById(id);
    }
    
    public Skill createSkill(String name, String description, SkillCategory category, SkillLevel level) {
        if (skillRepository.findByName(name).isPresent()) {
            throw new IllegalArgumentException("Skill with name '" + name + "' already exists");
        }
        
        Skill skill = new Skill(name, description, category, level);
        return skillRepository.save(skill);
    }
    
    // Module management
    public List<Module> getModulesBySkill(Long skillId) {
        return moduleRepository.findBySkillIdOrderByOrderIndex(skillId);
    }
    
    public Optional<Module> getModuleById(Long id) {
        return moduleRepository.findById(id);
    }
    
    public Module createModule(String title, String description, Integer durationWeeks, 
                              Long skillId, List<Long> prerequisiteIds) {
        Skill skill = skillRepository.findById(skillId)
            .orElseThrow(() -> new IllegalArgumentException("Skill not found with id: " + skillId));
        
        // Calculate next order index
        List<Module> existingModules = moduleRepository.findBySkillIdOrderByOrderIndex(skillId);
        Integer nextOrderIndex = existingModules.isEmpty() ? 1 : 
            existingModules.get(existingModules.size() - 1).getOrderIndex() + 1;
        
        Module module = new Module(title, description, nextOrderIndex, durationWeeks, skill);
        
        // Set prerequisites
        if (prerequisiteIds != null && !prerequisiteIds.isEmpty()) {
            List<Module> prerequisites = moduleRepository.findAllById(prerequisiteIds);
            module.getPrerequisites().addAll(prerequisites);
        }
        
        return moduleRepository.save(module);
    }
    
    public List<Module> getAvailableModules() {
        return moduleRepository.findModulesWithoutPrerequisites();
    }
    
    // Lesson management
    public List<Lesson> getLessonsByModule(Long moduleId) {
        return lessonRepository.findByModuleIdOrderByOrderIndex(moduleId);
    }
    
    public Optional<Lesson> getLessonById(String id) {
        return lessonRepository.findById(id);
    }
    
    public Lesson createLesson(Long moduleId, String title, String content, 
                              Integer estimatedMinutes, List<String> objectives) {
        // Verify module exists
        if (!moduleRepository.existsById(moduleId)) {
            throw new IllegalArgumentException("Module not found with id: " + moduleId);
        }
        
        // Calculate next order index
        long lessonCount = lessonRepository.countByModuleId(moduleId);
        Integer nextOrderIndex = (int) (lessonCount + 1);
        
        Lesson lesson = new Lesson(moduleId, title, content, nextOrderIndex, estimatedMinutes);
        lesson.setObjectives(objectives);
        
        return lessonRepository.save(lesson);
    }
    
    public Lesson getNextLesson(Long moduleId, Integer currentOrderIndex) {
        return lessonRepository.findByModuleIdAndOrderIndex(moduleId, currentOrderIndex + 1);
    }
    
    public Lesson getPreviousLesson(Long moduleId, Integer currentOrderIndex) {
        return lessonRepository.findByModuleIdAndOrderIndex(moduleId, currentOrderIndex - 1);
    }
}
