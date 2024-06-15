package com.yurjinia.project_structure.project.confirmationToken.service;

import com.yurjinia.common.exception.CommonException;
import com.yurjinia.common.exception.ErrorCode;
import com.yurjinia.project_structure.project.confirmationToken.entity.ConfirmationTokenEntity;
import com.yurjinia.project_structure.project.confirmationToken.repository.ConfirmationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository;

    public String createToken(String email, String projectName) {
        String token = UUID.randomUUID().toString();
        ConfirmationTokenEntity confirmationToken = new ConfirmationTokenEntity(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                email,
                projectName
        );

        confirmationTokenRepository.save(confirmationToken);
        return token;
    }

    public ConfirmationTokenEntity getToken(String token) {
        return confirmationTokenRepository.findByToken(token)
                .orElseThrow(() -> new CommonException(ErrorCode.TOKEN_NOT_FOUND));
    }

    public void deleteToken(String token) {
        confirmationTokenRepository.deleteByToken(token);
    }

}
