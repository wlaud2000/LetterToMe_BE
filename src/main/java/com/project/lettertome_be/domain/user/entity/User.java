package com.project.lettertome_be.domain.user.entity;

import com.project.lettertome_be.domain.user.entity.enums.Provider;
import com.project.lettertome_be.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
@Table(name = "users")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column
    private String password;

    @Column(name = "profile_img")
    private String profileImg;

    @Enumerated(EnumType.STRING)
    @Column
    private Provider provider;

    @Column(name = "oauth_id", unique = true)
    private String oauthId;

    public void changeEmail(String newEmail) {
        this.email = newEmail;
    }

    public void changeNickName(String newNickName) {
        this.nickname = newNickName;
    }

    public void changePassword(String newPassword) {
        this.password = newPassword;
    }

    // 닉네임을 업데이트하는 메서드 (OAuth에서 사용)
    public User updateNickname(String newNickname) {
        this.nickname = newNickname;
        return this;  // 메서드 체이닝을 위해 this 반환
    }

    // 프로필 이미지 변경 메서드
    public void changeProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

}
