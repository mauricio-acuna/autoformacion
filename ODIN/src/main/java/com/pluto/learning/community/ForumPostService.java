package com.pluto.learning.community;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ForumPostService {
    @Autowired
    private ForumPostRepository postRepository;

    public List<ForumPost> getPostsByForum(Long forumId) {
        return postRepository.findByForumIdAndIsDeletedFalse(forumId);
    }

    public Optional<ForumPost> getPostById(Long id) {
        return postRepository.findById(id);
    }

    public ForumPost createPost(ForumPost post) {
        return postRepository.save(post);
    }

    public ForumPost updatePost(Long id, ForumPost updated) {
        return postRepository.findById(id)
            .map(post -> {
                post.setTitle(updated.getTitle());
                post.setContent(updated.getContent());
                post.setTags(updated.getTags());
                post.setPostType(updated.getPostType());
                post.setIsPinned(updated.getIsPinned());
                post.setIsLocked(updated.getIsLocked());
                post.setIsDeleted(updated.getIsDeleted());
                post.setCodeSnippet(updated.getCodeSnippet());
                post.setProgrammingLanguage(updated.getProgrammingLanguage());
                return postRepository.save(post);
            })
            .orElseThrow();
    }

    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }
}
