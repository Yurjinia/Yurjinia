package com.yurjinia.platform.project_structure.comment.contreller;

import com.yurjinia.platform.project_structure.comment.service.CommentService;
import com.yurjinia.platform.project_structure.comment.contreller.request.CreateCommentRequest;
import com.yurjinia.platform.project_structure.comment.contreller.request.UpdateCommentRequest;
import com.yurjinia.platform.project_structure.comment.dto.CommentDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
