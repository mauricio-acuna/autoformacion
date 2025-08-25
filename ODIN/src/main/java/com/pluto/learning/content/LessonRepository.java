package com.pluto.learning.content;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonRepository extends MongoRepository<Lesson, String> {
    
    List<Lesson> findByModuleIdOrderByOrderIndex(Long moduleId);
    
    @Query("{ 'moduleId': ?0, 'orderIndex': ?1 }")
    Lesson findByModuleIdAndOrderIndex(Long moduleId, Integer orderIndex);
    
    @Query("{ 'moduleId': ?0, 'orderIndex': { $lt: ?1 } }")
    List<Lesson> findPreviousLessons(Long moduleId, Integer currentOrderIndex);
    
    @Query("{ 'moduleId': ?0, 'orderIndex': { $gt: ?1 } }")
    List<Lesson> findNextLessons(Long moduleId, Integer currentOrderIndex);
    
    long countByModuleId(Long moduleId);
}
