package com.project.lettertome_be.domain.user.repository;

import com.project.lettertome_be.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);

    Optional<User> findByOauthId(String oauthId);  // OauthId로 유저를 찾는 메소드
}
