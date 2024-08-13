package com.project.lettertome_be.domain.user.entity;

import com.project.lettertome_be.domain.user.entity.enums.Provider;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "social_users")
public class SocialUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Provider provider;

    @Column(name = "provider_key", nullable = false)
    private String providerKey;

    @OneToOne
    @MapsId //식별관계 설정
    @JoinColumn(name = "user_id") //user의 id를 참조
    private User user;
}
