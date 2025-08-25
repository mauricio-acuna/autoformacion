package com.pluto.learning.community;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class StudyGroupService {
    @Autowired
    private StudyGroupRepository groupRepository;
    @Autowired
    private StudyGroupMembershipRepository membershipRepository;

    public List<StudyGroup> getAllActiveGroups() {
        return groupRepository.findByIsActiveTrue();
    }

    public Optional<StudyGroup> getGroupById(Long id) {
        return groupRepository.findById(id);
    }

    public StudyGroup createGroup(StudyGroup group) {
        return groupRepository.save(group);
    }

    public StudyGroup updateGroup(Long id, StudyGroup updated) {
        return groupRepository.findById(id)
            .map(group -> {
                group.setName(updated.getName());
                group.setDescription(updated.getDescription());
                group.setStudyFocus(updated.getStudyFocus());
                group.setDifficultyLevel(updated.getDifficultyLevel());
                group.setIsActive(updated.getIsActive());
                group.setMeetingSchedule(updated.getMeetingSchedule());
                group.setTags(updated.getTags());
                group.setCurrentTopic(updated.getCurrentTopic());
                group.setNextSessionDate(updated.getNextSessionDate());
                return groupRepository.save(group);
            })
            .orElseThrow();
    }

    public void deleteGroup(Long id) {
        groupRepository.deleteById(id);
    }

    public List<StudyGroupMembership> getMembershipsByGroup(Long groupId) {
        return membershipRepository.findByStudyGroupId(groupId);
    }

    public List<StudyGroupMembership> getMembershipsByUser(Long userId) {
        return membershipRepository.findByUserId(userId);
    }

    public StudyGroupMembership addMembership(StudyGroupMembership membership) {
        return membershipRepository.save(membership);
    }

    public void removeMembership(Long membershipId) {
        membershipRepository.deleteById(membershipId);
    }
}
