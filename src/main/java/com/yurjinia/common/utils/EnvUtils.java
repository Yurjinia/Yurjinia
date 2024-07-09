package com.yurjinia.common.utils;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.experimental.UtilityClass;

@UtilityClass
public class EnvUtils {
    public static String getEnv(String key) {
        Dotenv dotenv = Dotenv.configure()
                .filename("credentials.env")
                .load();
        String encryptedValue = dotenv.get(key);
        return JasyptUtils.decrypt(encryptedValue);
    }
}
