package com.pluto.learning.community;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface StudyGroupRepository extends JpaRepository<StudyGroup, Long> {
    List<StudyGroup> findByIsActiveTrue();
    List<StudyGroup> findByCreatorId(Long creatorId);
    List<StudyGroup> findByStudyFocus(StudyGroup.StudyFocus studyFocus);
    List<StudyGroup> findByDifficultyLevel(StudyGroup.DifficultyLevel difficultyLevel);
}
