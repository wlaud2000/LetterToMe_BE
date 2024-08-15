package com.project.lettertome_be.domain.user.entity;

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

    @Column(nullable = false)
    private String email;

    @Column(name = "nickname", nullable = false)
    private String nickName;

    /*@Column(name = "profile_img")
    private String profileImg;*/

    public void changeEmail(String newEmail) {
        this.email = newEmail;
    }

    public void changeNickName(String newNickName) {
        this.nickName = newNickName;
    }

}
