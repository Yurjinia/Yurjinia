package com.yurjinia.project_structure.project.confirmationToken.repository;

import com.yurjinia.project_structure.project.confirmationToken.entity.ConfirmationTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationTokenEntity, Long> {
    Optional<ConfirmationTokenEntity> findByToken(String token);

    void deleteByToken(String token);
}
