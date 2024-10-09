package com.yurjinia.project_structure.comment.contreller.request;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
//TODO: remove @Setter
@Setter
public class CreateCommentRequest {
    @Size(max = 1024)
    private String text;
}
