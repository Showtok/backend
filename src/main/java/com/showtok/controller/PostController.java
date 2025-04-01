package com.showtok.controller;

import com.showtok.dto.PostCreateDTO;
import com.showtok.dto.PostResponseDTO;
import com.showtok.dto.PostSearchDTO;
import com.showtok.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody PostCreateDTO dto,
                                    @AuthenticationPrincipal UserDetails userDetails) {
        postService.createPost(dto, userDetails.getUsername());
        return ResponseEntity.ok("게시글 등록 완료");
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDTO> getPost(@PathVariable Long id) {
        return ResponseEntity.ok(postService.getPost(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<PostResponseDTO>> search(PostSearchDTO searchDTO) {
        return ResponseEntity.ok(postService.search(searchDTO));
    }

    @GetMapping("/popular")
    public ResponseEntity<List<PostResponseDTO>> getPopularPosts() {
        return ResponseEntity.ok(postService.getTop3PopularPosts());
    }
}
