package com.project.lettertome_be.domain.letter.entity;

import com.project.lettertome_be.domain.user.entity.User;
import com.project.lettertome_be.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
@Table(name = "letters")
public class Letter extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(name = "send_at",nullable = false)
    private LocalDateTime sendAt;

    @Column(name = "is_opened", nullable = false)
    @ColumnDefault("false")
    private boolean isOpened;

    @Column(name = "d_day")
    private Integer dDay;

    // 발송 후 isOpened 상태 변경 메서드
    public void setOpened(boolean opened) {
        this.isOpened = opened;
    }

    // dDay를 설정하는 메서드 추가
    public void setDDay(int dDay) {
        this.dDay = dDay;
    }
}
