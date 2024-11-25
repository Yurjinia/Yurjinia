package com.yurjinia.common.utils;

import com.yurjinia.project_structure.comment.dto.CommentDTO;
import com.yurjinia.project_structure.comment.entity.CommentEntity;
import com.yurjinia.user.dto.UserDTO;
import com.yurjinia.user.utils.TimeZoneUtils;
import lombok.experimental.UtilityClass;
import org.modelmapper.ModelMapper;

@UtilityClass
public class MapperUtils {

    private final static ModelMapper modelMapper = new ModelMapper();

    public static <S, T> T map(S source, Class<T> targetClass) {
        return modelMapper.map(source, targetClass);
    }

}
