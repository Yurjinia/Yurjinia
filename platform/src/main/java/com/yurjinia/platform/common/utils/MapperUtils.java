package com.yurjinia.platform.common.utils;

import lombok.experimental.UtilityClass;
import org.modelmapper.ModelMapper;

@UtilityClass
public class MapperUtils {

    private final static ModelMapper modelMapper = new ModelMapper();

    public static <S, T> T map(S source, Class<T> targetClass) {
        return modelMapper.map(source, targetClass);
    }

}
