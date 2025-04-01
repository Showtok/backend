package com.showtok.repository;

import com.showtok.domain.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long>, JpaSpecificationExecutor<Post> {

    @Query("SELECT COUNT(c) FROM Comment c WHERE c.post.id = :postId")
    Long countByPostId(@Param("postId") Long postId); // 🔥 댓글 개수 조회 쿼리 추가

    @Query("SELECT p FROM Post p ORDER BY (p.viewCount / 10.0 + p.likeCount) DESC")
    List<Post> findTop3ByCustomPopularity(Pageable pageable);
}
