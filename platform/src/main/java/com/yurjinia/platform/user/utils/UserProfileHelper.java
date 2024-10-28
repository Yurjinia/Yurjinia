package com.yurjinia.platform.user.utils;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

import java.util.function.Consumer;

@UtilityClass
public class UserProfileHelper {

    public static void updateMetaData(String value, Consumer<String> metaData) {
        if (StringUtils.isNotBlank(value)) {
            metaData.accept(value);
        }
    }

}
