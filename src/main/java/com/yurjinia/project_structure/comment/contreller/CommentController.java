package com.yurjinia.project_structure.comment.contreller;

import com.yurjinia.project_structure.comment.contreller.request.CreateCommentRequest;
import com.yurjinia.project_structure.comment.contreller.request.UpdateCommentRequest;
import com.yurjinia.project_structure.comment.dto.CommentDTO;
import com.yurjinia.project_structure.comment.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users/{userEmail}/projects/{projectCode}/boards/{boardCode}/tickets/{ticketCode}/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentDTO> createComment(@PathVariable String userEmail,
                                                    @PathVariable String projectCode,
                                                    @PathVariable String boardCode,
                                                    @PathVariable String ticketCode,
                                                    @Valid @RequestBody CreateCommentRequest createCommentRequest) {
        CommentDTO comment = commentService.createComment(userEmail, projectCode, boardCode, ticketCode, createCommentRequest);
        return ResponseEntity.ok(comment);
    }

    @PutMapping("/{commentId}")
    private ResponseEntity<CommentDTO> updateComment(@PathVariable String userEmail,
                                                     @PathVariable String projectCode,
                                                     @PathVariable String boardCode,
                                                     @PathVariable String ticketCode,
                                                     @PathVariable String commentId,
                                                     @Valid @RequestBody UpdateCommentRequest updateCommentRequest) {
        CommentDTO commentDTO = commentService.updateComment(commentId, userEmail, updateCommentRequest);
        return ResponseEntity.ok(commentDTO);
    }

    @DeleteMapping("/{commentId}")
    private ResponseEntity<CommentDTO> deleteComment(@PathVariable String userEmail,
                                                     @PathVariable String projectCode,
                                                     @PathVariable String boardCode,
                                                     @PathVariable String ticketCode,
                                                     @PathVariable String commentId) {
        commentService.deleteComment(commentId, userEmail);
        return ResponseEntity.noContent().build();
    }

}
