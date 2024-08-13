package com.project.lettertome_be.domain.user.repository;

import com.project.lettertome_be.domain.user.entity.LocalUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocalUserRepository extends JpaRepository<LocalUser, Long> {

}
