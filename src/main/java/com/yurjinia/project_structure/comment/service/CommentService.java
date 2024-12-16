package com.yurjinia.project_structure.comment.service;

import com.yurjinia.common.exception.CommonException;
import com.yurjinia.common.exception.ErrorCode;
import com.yurjinia.common.utils.MapperUtils;
import com.yurjinia.common.utils.MetadataUtils;
import com.yurjinia.project_structure.comment.controller.request.CreateCommentRequest;
import com.yurjinia.project_structure.comment.controller.request.UpdateCommentRequest;
import com.yurjinia.project_structure.comment.dto.CommentDTO;
import com.yurjinia.project_structure.comment.entity.CommentEntity;
import com.yurjinia.project_structure.comment.repository.CommentRepository;
import com.yurjinia.project_structure.comment.utils.CommentMapper;
import com.yurjinia.project_structure.ticket.service.TicketService;
import com.yurjinia.user.entity.UserEntity;
import com.yurjinia.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final UserService userService;
    private final CommentMapper commentMapper;
    private final TicketService ticketService;
    private final CommentRepository commentRepository;

    public CommentDTO createComment(String userEmail, String projectCode, String boardCode, String ticketCode, CreateCommentRequest createCommentRequest) {
        CommentEntity commentEntity = MapperUtils.map(createCommentRequest, CommentEntity.class);
        commentEntity.setAuthor(userService.getUserByEmail(userEmail));
        commentEntity.setTicket(ticketService.getTicketEntity(projectCode, boardCode, ticketCode));

        commentRepository.save(commentEntity);
        return commentMapper.toDTO(commentEntity, createCommentRequest.getTimeZone());
    }

    public CommentDTO updateComment(String commentId, String userEmail, UpdateCommentRequest updateCommentRequest) {
        CommentEntity commentEntity = getCommentById(commentId);
        validateAuthor(commentEntity.getAuthor(), userEmail);

        MetadataUtils.updateMetadata(updateCommentRequest.getText(), commentEntity::setText);

        commentRepository.save(commentEntity);

        return MapperUtils.map(commentEntity, CommentDTO.class);
    }

    public void deleteComment(String commentId, String userEmail) {
        CommentEntity commentEntity = getCommentById(commentId);
        validateAuthor(commentEntity.getAuthor(), userEmail);
        commentRepository.delete(commentEntity);
    }

    private void validateAuthor(UserEntity author, String userEmail) {
        if (!author.getEmail().equals(userEmail)) {
            throw new CommonException(ErrorCode.USER_IS_NOT_AUTHOR, HttpStatus.FORBIDDEN);
        }
    }

    private CommentEntity getCommentById(String commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new CommonException(ErrorCode.COMMENT_NOT_FOUND, HttpStatus.NOT_FOUND));
    }
}
