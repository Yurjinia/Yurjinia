package com.yurjinia.project_structure.comment.utils;

import com.yurjinia.common.utils.MapperUtils;
import com.yurjinia.project_structure.comment.dto.CommentDTO;
import com.yurjinia.project_structure.comment.entity.CommentEntity;
import com.yurjinia.user.dto.UserDTO;
import com.yurjinia.user.utils.TimeZoneUtils;

public class CommentMapper {
    // просто передавать LocalDateTime
    public static CommentDTO commentEntityToCommentDTO(CommentEntity entity, String timeZone) {
        CommentDTO dto = new CommentDTO();
        dto.setId(entity.getId());
        dto.setText(entity.getText());
        dto.setCreated(TimeZoneUtils.convertToLocalTime(entity.getCreated(), timeZone));
        dto.setUpdated(TimeZoneUtils.convertToLocalTime(entity.getUpdated(), timeZone));
        dto.setAuthor(MapperUtils.map(entity.getAuthor(), UserDTO.class));
        return dto;
    }
}
