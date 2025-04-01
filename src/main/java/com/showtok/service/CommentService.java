package com.showtok.service;

import com.showtok.domain.Comment;
import com.showtok.domain.Post;
import com.showtok.domain.User;
import com.showtok.dto.CommentRequestDTO;
import com.showtok.dto.CommentResponseDTO;
import com.showtok.repository.CommentRepository;
import com.showtok.repository.PostRepository;
import com.showtok.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public void addComment(CommentRequestDTO dto) {
        Post post = postRepository.findById(dto.getPostId())
                .orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다"));
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자가 존재하지 않습니다"));

        Comment parent = null;
        if (dto.getParentId() != null) {
            parent = commentRepository.findById(dto.getParentId())
                    .orElseThrow(() -> new RuntimeException("부모 댓글이 존재하지 않습니다"));
        }

        Comment comment = Comment.builder()
                .post(post)
                .author(user)
                .content(dto.getContent())
                .parent(parent)
                .build();

        commentRepository.save(comment);
    }

    public List<CommentResponseDTO> getComments(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다"));

        List<Comment> topLevelComments = commentRepository.findByPost(post).stream()
                .filter(comment -> comment.getParent() == null)
                .toList();

        return topLevelComments.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private CommentResponseDTO convertToDTO(Comment comment) {
        return CommentResponseDTO.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .author(comment.getAuthor().getNickname())
                .children(comment.getChildren().stream()
                        .map(this::convertToDTO)
                        .collect(Collectors.toList()))
                .build();
    }

    @Transactional
    public void updateComment(Long commentId, String newContent) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글이 존재하지 않습니다"));

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!comment.getAuthor().getUsername().equals(username)) {
            throw new RuntimeException("본인의 댓글만 수정할 수 있습니다");
        }

        comment.setContent(newContent);
    }

    @Transactional
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글이 존재하지 않습니다"));

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!comment.getAuthor().getUsername().equals(username)) {
            throw new RuntimeException("본인의 댓글만 삭제할 수 있습니다");
        }

        commentRepository.delete(comment);
    }
}