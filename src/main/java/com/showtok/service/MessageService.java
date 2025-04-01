package com.showtok.service;

import com.showtok.domain.Message;
import com.showtok.domain.User;
import com.showtok.dto.MessageRequestDTO;
import com.showtok.dto.MessageResponseDTO;
import com.showtok.repository.MessageRepository;
import com.showtok.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    @Transactional
    public void sendMessage(MessageRequestDTO dto) {
        String senderUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        User sender = userRepository.findByUsername(senderUsername)
                .orElseThrow(() -> new RuntimeException("보내는 사용자가 존재하지 않습니다"));

        User receiver = userRepository.findByUsername(dto.getReceiverUsername())
                .orElseThrow(() -> new RuntimeException("받는 사용자가 존재하지 않습니다"));

        // 🔥 크레딧 차감
        if (sender.getCredit() < 2) {
            throw new RuntimeException("크레딧이 부족합니다");
        }
        sender.setCredit(sender.getCredit() - 2);
        userRepository.save(sender);

        Message message = Message.builder()
                .sender(sender)
                .receiver(receiver)
                .content(dto.getContent())
                .sentAt(LocalDateTime.now())
                .read(false)
                .deletedBySender(false)
                .deletedByReceiver(false)
                .build();

        messageRepository.save(message);
    }

    public List<MessageResponseDTO> getMyMessages() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자 없음"));

        return messageRepository.findByReceiver(user).stream()
                .filter(msg -> !msg.isDeletedByReceiver())
                .map(msg -> MessageResponseDTO.builder()
                        .sender(msg.getSender().getNickname())
                        .receiver(msg.getReceiver().getNickname())
                        .content(msg.getContent())
                        .sentAt(msg.getSentAt())
                        .read(msg.isRead())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public void markAsRead(Long messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("쪽지를 찾을 수 없습니다"));
        message.setRead(true);
    }

    @Transactional
    public void deleteReceivedMessage(Long messageId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("쪽지를 찾을 수 없습니다"));
        if (!message.getReceiver().getUsername().equals(username)) {
            throw new RuntimeException("삭제 권한이 없습니다");
        }
        message.setDeletedByReceiver(true);
    }

    @Transactional
    public void deleteSentMessage(Long messageId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("쪽지를 찾을 수 없습니다"));
        if (!message.getSender().getUsername().equals(username)) {
            throw new RuntimeException("삭제 권한이 없습니다");
        }
        message.setDeletedBySender(true);
    }
}