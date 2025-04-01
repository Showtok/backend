package com.showtok.dto;

import com.showtok.domain.Category;
import com.showtok.domain.Level;
import com.showtok.domain.PostCategory;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostResponseDTO {
    private Long id;
    private String title;
    private String content;
    private String authorNickname;
    private Category category;
    private PostCategory postCategory;
    private Level level;
    private Long viewCount;
    private Long likeCount;
    private Long commentCount;
}
