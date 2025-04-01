package com.showtok.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class MessageResponseDTO {
    private Long id; // 쪽지 삭제/읽음 처리를 위해 추가
    private String sender;
    private String receiver;
    private String content;
    private LocalDateTime sentAt;
    private boolean read; // 읽음 여부 표시
}
