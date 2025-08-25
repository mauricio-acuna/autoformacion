package com.pluto.learning.community;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ForumPostRepository extends JpaRepository<ForumPost, Long> {
    List<ForumPost> findByForumIdAndIsDeletedFalse(Long forumId);
    List<ForumPost> findByAuthorId(Long authorId);
    List<ForumPost> findByParentPostId(Long parentPostId);
    List<ForumPost> findByIsPinnedTrue();
    List<ForumPost> findByTagsContaining(String tag);
}
