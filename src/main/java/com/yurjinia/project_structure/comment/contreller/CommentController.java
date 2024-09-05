package com.yurjinia.project_structure.comment.contreller;

import com.yurjinia.project_structure.comment.dto.CommentDTO;
import com.yurjinia.project_structure.comment.dto.CreateCommentRequest;
import com.yurjinia.project_structure.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users/{userEmail}/projects/{projectCode}/boards/{boardCode}/tickets/{ticketCode}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentDTO> createComment(@PathVariable String userEmail,
                                                    @PathVariable String projectCode,
                                                    @PathVariable String boardCode,
                                                    @PathVariable String ticketCode,
                                                    @RequestBody CreateCommentRequest createCommentRequest){
        CommentDTO comment = commentService.createComment(userEmail, projectCode, boardCode, ticketCode, createCommentRequest);
        return ResponseEntity.ok(comment);
    }

}
