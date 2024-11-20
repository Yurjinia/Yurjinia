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

    public static CommentDTO commentEntityToCommentDTO(CommentEntity entity, String timeZone) {
        String format="d MMMM yyyy 'в' HH:mm";
        CommentDTO dto = new CommentDTO();
        dto.setId(entity.getId());
        dto.setText(entity.getText());
        dto.setCreated(TimeZoneUtils.formatDate(TimeZoneUtils.convertToLocalTime(entity.getCreated(), timeZone),format));
        dto.setUpdated(TimeZoneUtils.formatDate(TimeZoneUtils.convertToLocalTime(entity.getUpdated(), timeZone),format));
        dto.setAuthor(MapperUtils.map(entity.getAuthor(), UserDTO.class));
        return dto;
    }

}
