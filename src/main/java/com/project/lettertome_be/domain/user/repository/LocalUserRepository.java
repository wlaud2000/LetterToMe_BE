package com.project.lettertome_be.domain.user.repository;

import com.project.lettertome_be.domain.user.entity.LocalUser;
import com.project.lettertome_be.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LocalUserRepository extends JpaRepository<LocalUser, Long> {

    Optional<LocalUser> findByUser(User user);
}
