package com.yurjinia.project_structure.comment.contreller.request;

import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class CreateCommentRequest {
    @Size(max = 1024)
    private String text;
    private String timeZone;
}
