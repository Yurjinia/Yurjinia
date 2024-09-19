package com.yurjinia.common.utils;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;
import java.util.function.Consumer;

@UtilityClass
public class MetadataUtils {

    public static void updateMetadata(String newValue, Consumer<String> updater) {
        if (StringUtils.isNotBlank(newValue)) {
            updater.accept(newValue);
        }
    }

    public static <T> void updateMetadata(T newValue, Consumer<T> updater) {
        if (Objects.nonNull(newValue)) {
            updater.accept(newValue);
        }
    }

}
