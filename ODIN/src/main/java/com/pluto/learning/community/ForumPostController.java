package com.pluto.learning.community;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/community/posts")
@CrossOrigin(origins = "*")
public class ForumPostController {
    @Autowired
    private ForumPostService postService;

    @GetMapping("/forum/{forumId}")
    public ResponseEntity<List<ForumPost>> getPostsByForum(@PathVariable Long forumId) {
        return ResponseEntity.ok(postService.getPostsByForum(forumId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ForumPost> getPostById(@PathVariable Long id) {
        return postService.getPostById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ForumPost> createPost(@RequestBody ForumPost post) {
        return ResponseEntity.ok(postService.createPost(post));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ForumPost> updatePost(@PathVariable Long id, @RequestBody ForumPost post) {
        return ResponseEntity.ok(postService.updatePost(id, post));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }
}
