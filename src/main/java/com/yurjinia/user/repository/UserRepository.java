package com.yurjinia.user.repository;

import com.yurjinia.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByUserProfileUsername(String username);
    Set<UserEntity> findAllByEmailIn(Set<String> emails);
    boolean existsByEmail(String email);
}
