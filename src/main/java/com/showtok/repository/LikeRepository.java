package com.showtok.repository;

import com.showtok.domain.Like;
import com.showtok.domain.Post;
import com.showtok.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByUserAndPost(User user, Post post);
    Long countByPost(Post post);
}
