package com.yurjinia.user.repository;

import com.yurjinia.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByUserProfileUsername(String username);
    List<UserEntity> findAllByEmailIn(List<String> emails);
    boolean existsByEmail(String email);
}
