package com.yurjinia.project_structure.comment.dto;

import com.yurjinia.user.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {
    private String text;
    private LocalDateTime created;
    private LocalDateTime updated;
    private UserDTO author;
}
