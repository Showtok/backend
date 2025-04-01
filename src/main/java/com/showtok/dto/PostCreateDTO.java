package com.showtok.dto;

import com.showtok.domain.Category;
import com.showtok.domain.Level;
import com.showtok.domain.PostCategory;
import lombok.Data;

@Data
public class PostCreateDTO {
    private String title;
    private String content;
    private Category category;
    private PostCategory postCategory;
    private Level level;
}
