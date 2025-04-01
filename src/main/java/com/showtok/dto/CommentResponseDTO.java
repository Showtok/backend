package com.showtok.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CommentResponseDTO {
    private Long id;
    private String content;
    private String author;
    private List<CommentResponseDTO> children;
}