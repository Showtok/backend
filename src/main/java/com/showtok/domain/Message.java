package com.showtok.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "`message`")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User sender;

    @ManyToOne(fetch = FetchType.LAZY)
    private User receiver;

    @Column(nullable = false)
    private String content;

    private LocalDateTime sentAt;

    @Column(name = "`read`")
    private boolean read;

    private boolean deletedBySender;

    private boolean deletedByReceiver;
}
