package com.showtok.service;

import com.showtok.domain.Post;
import com.showtok.domain.User;
import com.showtok.dto.PostCreateDTO;
import com.showtok.dto.PostResponseDTO;
import com.showtok.dto.PostSearchDTO;
import com.showtok.repository.PostRepository;
import com.showtok.repository.UserRepository;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public void createPost(PostCreateDTO dto, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자 없음"));

        Post post = Post.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .author(user)
                .category(dto.getCategory())
                .postCategory(dto.getPostCategory())
                .level(dto.getLevel())
                .viewCount(0L)
                .likeCount(0L)
                .build();

        postRepository.save(post);
    }

    @Transactional
    public PostResponseDTO getPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("게시글 없음"));
        post.setViewCount(post.getViewCount() + 1); // 조회수 증가
        return toDTO(post);
    }

    public List<PostResponseDTO> search(PostSearchDTO searchDTO) {
        Specification<Post> spec = (root, query, cb) -> {
            Predicate predicate = cb.conjunction();
            if (searchDTO.getKeyword() != null) {
                Predicate title = cb.like(root.get("title"), "%" + searchDTO.getKeyword() + "%");
                Predicate content = cb.like(root.get("content"), "%" + searchDTO.getKeyword() + "%");
                predicate = cb.and(predicate, cb.or(title, content));
            }
            if (searchDTO.getCategory() != null) {
                predicate = cb.and(predicate, cb.equal(root.get("category"), searchDTO.getCategory()));
            }
            if (searchDTO.getPostCategory() != null) {
                predicate = cb.and(predicate, cb.equal(root.get("postCategory"), searchDTO.getPostCategory()));
            }
            if (searchDTO.getLevel() != null) {
                predicate = cb.and(predicate, cb.equal(root.get("level"), searchDTO.getLevel()));
            }
            return predicate;
        };
        return postRepository.findAll(spec).stream().map(this::toDTO).collect(Collectors.toList());
    }

    private PostResponseDTO toDTO(Post post) {
        Long commentCount = postRepository.countByPostId(post.getId());

        return PostResponseDTO.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .authorNickname(post.getAuthor().getNickname())
                .category(post.getCategory())
                .postCategory(post.getPostCategory())
                .level(post.getLevel())
                .viewCount(post.getViewCount())
                .likeCount(post.getLikeCount())
                .commentCount(commentCount)
                .build();
    }
    public List<PostResponseDTO> getTop3PopularPosts() {
        Pageable topThree = PageRequest.of(0, 3);
        return postRepository.findTop3ByCustomPopularity(topThree).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}
