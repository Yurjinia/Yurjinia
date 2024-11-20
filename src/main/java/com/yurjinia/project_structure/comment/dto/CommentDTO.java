package com.yurjinia.project_structure.comment.dto;

import com.yurjinia.user.dto.UserDTO;
import com.yurjinia.user.utils.TimeZoneUtils;
import lombok.Getter;
import lombok.Setter;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;

import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
public class CommentDTO {
    private String id;
    private String text;
    private String created;
    private String updated;
    private UserDTO author;

}
