package com.pluto.learning.community;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/community/forums")
@CrossOrigin(origins = "*")
public class DiscussionForumController {
    @Autowired
    private DiscussionForumService forumService;

    @GetMapping
    public ResponseEntity<List<DiscussionForum>> getAllActiveForums() {
        return ResponseEntity.ok(forumService.getAllActiveForums());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DiscussionForum> getForumById(@PathVariable Long id) {
        return forumService.getForumById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<DiscussionForum> createForum(@RequestBody DiscussionForum forum) {
        return ResponseEntity.ok(forumService.createForum(forum));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DiscussionForum> updateForum(@PathVariable Long id, @RequestBody DiscussionForum forum) {
        return ResponseEntity.ok(forumService.updateForum(id, forum));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteForum(@PathVariable Long id) {
        forumService.deleteForum(id);
        return ResponseEntity.noContent().build();
    }
}
