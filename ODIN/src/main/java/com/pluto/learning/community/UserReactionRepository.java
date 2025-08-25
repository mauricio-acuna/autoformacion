package com.pluto.learning.community;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserReactionRepository extends JpaRepository<UserReaction, Long> {
    List<UserReaction> findByPostId(Long postId);
    List<UserReaction> findByUserId(Long userId);
    boolean existsByUserIdAndPostId(Long userId, Long postId);
}
