package com.yurjinia.common.utils;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;
import java.util.function.Consumer;

@UtilityClass
public class MetadataUtils {

    public static <T> void updateMetadata(T newValue, Consumer<T> updater) {
        if (newValue instanceof String) {
            if (StringUtils.isNotBlank((String) newValue)) {
                updater.accept(newValue);
            }
        } else if (Objects.nonNull(newValue)) {
            updater.accept(newValue);
        }
    }

}
