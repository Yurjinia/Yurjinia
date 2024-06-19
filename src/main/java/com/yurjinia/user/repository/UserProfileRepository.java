package com.yurjinia.user.repository;

import com.yurjinia.user.entity.UserProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfileEntity, Long> {
    boolean existsByUsername(String username);

    Optional<UserProfileEntity> findByUserEmail(String email);
}
