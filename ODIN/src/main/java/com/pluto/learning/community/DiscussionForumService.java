package com.pluto.learning.community;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class DiscussionForumService {
    @Autowired
    private DiscussionForumRepository forumRepository;

    public List<DiscussionForum> getAllActiveForums() {
        return forumRepository.findByIsActiveTrue();
    }

    public Optional<DiscussionForum> getForumById(Long id) {
        return forumRepository.findById(id);
    }

    public DiscussionForum createForum(DiscussionForum forum) {
        return forumRepository.save(forum);
    }

    public DiscussionForum updateForum(Long id, DiscussionForum updated) {
        return forumRepository.findById(id)
            .map(forum -> {
                forum.setName(updated.getName());
                forum.setDescription(updated.getDescription());
                forum.setCategory(updated.getCategory());
                forum.setIsActive(updated.getIsActive());
                forum.setModerator(updated.getModerator());
                return forumRepository.save(forum);
            })
            .orElseThrow();
    }

    public void deleteForum(Long id) {
        forumRepository.deleteById(id);
    }
}
