package com.yurjinia.project_structure.comment.service;

import com.yurjinia.project_structure.comment.dto.CommentDTO;
import com.yurjinia.project_structure.comment.dto.CreateCommentRequest;
import com.yurjinia.project_structure.comment.entity.CommentEntity;
import com.yurjinia.project_structure.comment.repository.CommentRepository;
import com.yurjinia.project_structure.comment.service.mapper.CommentMapper;
import com.yurjinia.project_structure.ticket.service.TicketService;
import com.yurjinia.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final UserService userService;
    private final TicketService ticketService;
    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;

    public CommentDTO createComment(String userEmail, String projectCode, String boardCode, String ticketCode, CreateCommentRequest createCommentRequest) {
        CommentEntity commentEntity = commentMapper.toEntity(createCommentRequest);
        commentEntity.setAuthor(userService.getByEmail(userEmail));
        commentEntity.setTicket(ticketService.getTicketEntity(projectCode, boardCode, ticketCode));
        commentRepository.save(commentEntity);
        return commentMapper.toDto(commentEntity);
    }
}
