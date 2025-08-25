package com.pluto.learning.community;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface StudyGroupMembershipRepository extends JpaRepository<StudyGroupMembership, Long> {
    List<StudyGroupMembership> findByUserId(Long userId);
    List<StudyGroupMembership> findByStudyGroupId(Long studyGroupId);
    boolean existsByUserIdAndStudyGroupId(Long userId, Long studyGroupId);
}
