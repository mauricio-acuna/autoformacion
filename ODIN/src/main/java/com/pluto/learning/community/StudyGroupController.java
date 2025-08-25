package com.pluto.learning.community;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/community/groups")
@CrossOrigin(origins = "*")
public class StudyGroupController {
    @Autowired
    private StudyGroupService groupService;

    @GetMapping
    public ResponseEntity<List<StudyGroup>> getAllActiveGroups() {
        return ResponseEntity.ok(groupService.getAllActiveGroups());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudyGroup> getGroupById(@PathVariable Long id) {
        return groupService.getGroupById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<StudyGroup> createGroup(@RequestBody StudyGroup group) {
        return ResponseEntity.ok(groupService.createGroup(group));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudyGroup> updateGroup(@PathVariable Long id, @RequestBody StudyGroup group) {
        return ResponseEntity.ok(groupService.updateGroup(id, group));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGroup(@PathVariable Long id) {
        groupService.deleteGroup(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/memberships")
    public ResponseEntity<List<StudyGroupMembership>> getMembershipsByGroup(@PathVariable Long id) {
        return ResponseEntity.ok(groupService.getMembershipsByGroup(id));
    }

    @PostMapping("/{id}/memberships")
    public ResponseEntity<StudyGroupMembership> addMembership(@PathVariable Long id, @RequestBody StudyGroupMembership membership) {
        // For simplicity, assume membership.studyGroup is set
        return ResponseEntity.ok(groupService.addMembership(membership));
    }

    @DeleteMapping("/memberships/{membershipId}")
    public ResponseEntity<Void> removeMembership(@PathVariable Long membershipId) {
        groupService.removeMembership(membershipId);
        return ResponseEntity.noContent().build();
    }
}
