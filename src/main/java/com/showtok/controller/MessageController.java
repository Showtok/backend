package com.showtok.controller;

import com.showtok.dto.MessageRequestDTO;
import com.showtok.dto.MessageResponseDTO;
import com.showtok.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    // 쪽지 전송
    @PostMapping
    public ResponseEntity<?> send(@RequestBody MessageRequestDTO dto) {
        messageService.sendMessage(dto);
        return ResponseEntity.ok("쪽지 전송 완료 (2 크레딧 차감)");
    }

    // 받은 쪽지 목록 조회
    @GetMapping
    public ResponseEntity<List<MessageResponseDTO>> myMessages() {
        return ResponseEntity.ok(messageService.getMyMessages());
    }

    // 쪽지 읽음 처리
    @PatchMapping("/{id}/read")
    public ResponseEntity<?> markAsRead(@PathVariable Long id) {
        messageService.markAsRead(id);
        return ResponseEntity.ok("쪽지를 읽음 처리했습니다.");
    }

    // 받은 쪽지 삭제
    @DeleteMapping("/{id}/received")
    public ResponseEntity<?> deleteReceived(@PathVariable Long id) {
        messageService.deleteReceivedMessage(id);
        return ResponseEntity.ok("받은 쪽지를 삭제했습니다.");
    }

    // 보낸 쪽지 삭제
    @DeleteMapping("/{id}/sent")
    public ResponseEntity<?> deleteSent(@PathVariable Long id) {
        messageService.deleteSentMessage(id);
        return ResponseEntity.ok("보낸 쪽지를 삭제했습니다.");
    }
}
