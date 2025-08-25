package com.pluto.learning.community;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DiscussionForumRepository extends JpaRepository<DiscussionForum, Long> {
    List<DiscussionForum> findByIsActiveTrue();
    List<DiscussionForum> findByCategory(DiscussionForum.ForumCategory category);
    List<DiscussionForum> findByModeratorId(Long moderatorId);
}
