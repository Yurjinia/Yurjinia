package com.yurjinia.project_structure.comment.dto;

import com.yurjinia.user.dto.UserDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentDTO {
    private String id;
    private String text;
    private LocalDateTime created;
    private LocalDateTime updated;
    private UserDTO author;
}
