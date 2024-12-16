package com.yurjinia.project_structure.comment.controller.request;

import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UpdateCommentRequest {
    @Size(max = 1024)
    private String text;
}
