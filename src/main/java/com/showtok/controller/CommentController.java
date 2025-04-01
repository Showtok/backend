package com.showtok.controller;

import com.showtok.dto.CommentRequestDTO;
import com.showtok.dto.CommentResponseDTO;
import com.showtok.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CommentRequestDTO dto) {
        commentService.addComment(dto);
        return ResponseEntity.ok("댓글이 등록되었습니다");
    }

    @GetMapping("/{postId}")
    public ResponseEntity<List<CommentResponseDTO>> getByPost(@PathVariable Long postId) {
        return ResponseEntity.ok(commentService.getComments(postId));
    }
    @PutMapping("/{commentId}")
    public ResponseEntity<?> update(@PathVariable Long commentId,
                                    @RequestBody CommentRequestDTO dto) {
        commentService.updateComment(commentId, dto.getContent());
        return ResponseEntity.ok("댓글이 수정되었습니다");
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> delete(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.ok("댓글이 삭제되었습니다");
    }
}
