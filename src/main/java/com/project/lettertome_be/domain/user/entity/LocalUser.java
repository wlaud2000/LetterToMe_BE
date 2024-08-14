package com.project.lettertome_be.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Builder
@Table(name = "local_users")
public class LocalUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false) // 새로운 이메일 필드 추가
    private String email;

    @OneToOne
    @MapsId //식별관계 설정
    @JoinColumn(name = "user_id") //user의 id를 참조
    private User user;

    //비밀번호 변경 메서드
    public void changePassword(String newPassword) {
        this.password = newPassword;
    }

    public void changeEmail(String newEmail) {
        this.email = newEmail;
    }
}
