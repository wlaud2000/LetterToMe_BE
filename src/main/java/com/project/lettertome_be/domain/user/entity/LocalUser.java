package com.project.lettertome_be.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "local_users")
public class LocalUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(nullable = false)
    private String password;

    @OneToOne
    @MapsId //식별관계 설정
    @JoinColumn(name = "user_id") //user의 id를 참조
    private User user;

    //비밀번호 변경 메서드
    public void changePassword(String newPassword) {
        this.password = newPassword;
    }
}
