package com.yurjinia.common.utils;

import lombok.experimental.UtilityClass;
import org.modelmapper.ModelMapper;

@UtilityClass
public class MapperUtil {

    private final static ModelMapper modelMapper = new ModelMapper();

    public static <S, T> T map(S source, Class<T> targetClass) {
        return modelMapper.map(source, targetClass);
    }

}
