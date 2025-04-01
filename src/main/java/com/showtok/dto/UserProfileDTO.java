package com.showtok.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserProfileDTO {
    private String username;
    private String nickname;
    private String phone;
    private int credit;
}
