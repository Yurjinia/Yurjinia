package com.yurjinia.common.environment;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.RequiredArgsConstructor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EnvironmentService {

    private final Dotenv dotenv;
    private final StandardPBEStringEncryptor encryptor;

    public String getEnv(String key) {
        String encryptedValue = dotenv.get(key);
        return encryptor.decrypt(encryptedValue);
    }
}
