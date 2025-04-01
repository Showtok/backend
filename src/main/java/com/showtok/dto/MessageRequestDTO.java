package com.showtok.dto;

import lombok.Data;

@Data
public class MessageRequestDTO {
    private String receiverUsername;
    private String content;
}
