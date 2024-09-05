package com.yurjinia.project_structure.comment.service.mapper;

import com.yurjinia.project_structure.comment.dto.CommentDTO;
import com.yurjinia.project_structure.comment.dto.CreateCommentRequest;
import com.yurjinia.project_structure.comment.entity.CommentEntity;
import com.yurjinia.user.dto.UserProfileDTO;
import com.yurjinia.user.entity.UserEntity;
import com.yurjinia.user.service.mapper.UserMapper;
import com.yurjinia.user.service.mapper.UserProfileMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CommentMapper {

    private final UserMapper userMapper;
    private final UserProfileMapper userProfileMapper;

    public CommentEntity toEntity(CreateCommentRequest createCommentRequest) {
        return CommentEntity.builder()
                .text(createCommentRequest.getText())
                .build();
    }

    public List<CommentDTO> toDtos(List<CommentEntity> commentEntity) {
        return commentEntity.stream().map(this::toDto).toList();
    }

    public CommentDTO toDto(CommentEntity commentEntity) {
        UserEntity userEntity = commentEntity.getAuthor();
        UserProfileDTO userProfileDTO = userProfileMapper.toDto(userEntity.getUserProfile());
        return CommentDTO.builder()
                .text(commentEntity.getText())
                .author(userMapper.toDto(userEntity, userProfileDTO))
                .created(commentEntity.getCreated())
                .updated(commentEntity.getUpdated())
                .build();
    }
}
