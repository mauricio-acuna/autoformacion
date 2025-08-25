package com.pluto.learning.content;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/content")
@Tag(name = "Content Management", description = "APIs para gestión de contenidos educativos")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ContentController {
    
    private final ContentService contentService;
    
    @Autowired
    public ContentController(ContentService contentService) {
        this.contentService = contentService;
    }
    
    // Skills endpoints
    @GetMapping("/skills")
    @Operation(summary = "Obtener todas las habilidades", 
               description = "Retorna lista de habilidades ordenadas por categoría y nivel")
    public ResponseEntity<List<Skill>> getAllSkills() {
        List<Skill> skills = contentService.getAllSkills();
        return ResponseEntity.ok(skills);
    }
    
    @GetMapping("/skills/category/{category}")
    @Operation(summary = "Obtener habilidades por categoría")
    public ResponseEntity<List<Skill>> getSkillsByCategory(
            @Parameter(description = "Categoría de la habilidad") 
            @PathVariable SkillCategory category) {
        List<Skill> skills = contentService.getSkillsByCategory(category);
        return ResponseEntity.ok(skills);
    }
    
    @GetMapping("/skills/{id}")
    @Operation(summary = "Obtener habilidad por ID")
    public ResponseEntity<Skill> getSkillById(
            @Parameter(description = "ID de la habilidad") 
            @PathVariable Long id) {
        return contentService.getSkillById(id)
                .map(skill -> ResponseEntity.ok(skill))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping("/skills")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INSTRUCTOR')")
    @Operation(summary = "Crear nueva habilidad", 
               description = "Solo disponible para administradores e instructores")
    public ResponseEntity<Skill> createSkill(@Valid @RequestBody CreateSkillRequest request) {
        try {
            Skill skill = contentService.createSkill(
                request.getName(), 
                request.getDescription(), 
                request.getCategory(), 
                request.getLevel()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(skill);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // Modules endpoints
    @GetMapping("/skills/{skillId}/modules")
    @Operation(summary = "Obtener módulos de una habilidad")
    public ResponseEntity<List<Module>> getModulesBySkill(
            @Parameter(description = "ID de la habilidad") 
            @PathVariable Long skillId) {
        List<Module> modules = contentService.getModulesBySkill(skillId);
        return ResponseEntity.ok(modules);
    }
    
    @GetMapping("/modules/{id}")
    @Operation(summary = "Obtener módulo por ID")
    public ResponseEntity<Module> getModuleById(
            @Parameter(description = "ID del módulo") 
            @PathVariable Long id) {
        return contentService.getModuleById(id)
                .map(module -> ResponseEntity.ok(module))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/modules/available")
    @Operation(summary = "Obtener módulos disponibles", 
               description = "Módulos sin prerequisitos que pueden ser iniciados")
    public ResponseEntity<List<Module>> getAvailableModules() {
        List<Module> modules = contentService.getAvailableModules();
        return ResponseEntity.ok(modules);
    }
    
    @PostMapping("/modules")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INSTRUCTOR')")
    @Operation(summary = "Crear nuevo módulo")
    public ResponseEntity<Module> createModule(@Valid @RequestBody CreateModuleRequest request) {
        try {
            Module module = contentService.createModule(
                request.getTitle(),
                request.getDescription(),
                request.getDurationWeeks(),
                request.getSkillId(),
                request.getPrerequisiteIds()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(module);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // Lessons endpoints
    @GetMapping("/modules/{moduleId}/lessons")
    @Operation(summary = "Obtener lecciones de un módulo")
    public ResponseEntity<List<Lesson>> getLessonsByModule(
            @Parameter(description = "ID del módulo") 
            @PathVariable Long moduleId) {
        List<Lesson> lessons = contentService.getLessonsByModule(moduleId);
        return ResponseEntity.ok(lessons);
    }
    
    @GetMapping("/lessons/{id}")
    @Operation(summary = "Obtener lección por ID")
    public ResponseEntity<Lesson> getLessonById(
            @Parameter(description = "ID de la lección") 
            @PathVariable String id) {
        return contentService.getLessonById(id)
                .map(lesson -> ResponseEntity.ok(lesson))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping("/lessons")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INSTRUCTOR')")
    @Operation(summary = "Crear nueva lección")
    public ResponseEntity<Lesson> createLesson(@Valid @RequestBody CreateLessonRequest request) {
        try {
            Lesson lesson = contentService.createLesson(
                request.getModuleId(),
                request.getTitle(),
                request.getContent(),
                request.getEstimatedMinutes(),
                request.getObjectives()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(lesson);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/modules/{moduleId}/lessons/{orderIndex}/next")
    @Operation(summary = "Obtener siguiente lección")
    public ResponseEntity<Lesson> getNextLesson(
            @PathVariable Long moduleId, 
            @PathVariable Integer orderIndex) {
        Lesson nextLesson = contentService.getNextLesson(moduleId, orderIndex);
        return nextLesson != null ? ResponseEntity.ok(nextLesson) : ResponseEntity.notFound().build();
    }
    
    @GetMapping("/modules/{moduleId}/lessons/{orderIndex}/previous")
    @Operation(summary = "Obtener lección anterior")
    public ResponseEntity<Lesson> getPreviousLesson(
            @PathVariable Long moduleId, 
            @PathVariable Integer orderIndex) {
        Lesson previousLesson = contentService.getPreviousLesson(moduleId, orderIndex);
        return previousLesson != null ? ResponseEntity.ok(previousLesson) : ResponseEntity.notFound().build();
    }
}
