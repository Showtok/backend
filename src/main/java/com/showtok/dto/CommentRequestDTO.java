// CommentRequestDTO.java
package com.showtok.dto;

import lombok.Data;

@Data
public class CommentRequestDTO {
    // CommentRequestDTO.java
    private Long postId;
    private String content;
    private Long parentId; // → null이면 일반 댓글, 값이 있으면 대댓글
}