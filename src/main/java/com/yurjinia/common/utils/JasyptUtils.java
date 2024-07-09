package com.yurjinia.common.utils;

import lombok.experimental.UtilityClass;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

@UtilityClass
public class JasyptUtils {

    public static String decrypt(String value) {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setAlgorithm("PBEWithMD5AndDES");
        encryptor.setPassword("6fL7NsDqEw8d");
        return encryptor.decrypt(value);
    }
}
