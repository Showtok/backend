package com.showtok.dto;

import lombok.Data;

@Data
public class UserSignupDTO {
    private String username;
    private String password;
    private String nickname;
    private String phone;
}
