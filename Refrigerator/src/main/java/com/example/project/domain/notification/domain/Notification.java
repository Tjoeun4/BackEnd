package com.example.project.domain.notification.domain;

import java.time.LocalDateTime;

import com.example.project.global.controller.BaseTimeEntity;
import com.example.project.member.domain.Users;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "notifications")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;

    @Column(nullable = false, length = 20)
    private String type; // GROUPBUY인지 EXPIRY인지

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false)
    private String body;

    @Column(name = "target_type", nullable = false, length = 20)
    private String targetType = "NONE";

    @Column(name = "target_id")
    private Long targetId;

    @Column(name = "sent_at")
    private LocalDateTime sentAt;

    @Column(name = "read_at")
    private LocalDateTime readAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;
}
