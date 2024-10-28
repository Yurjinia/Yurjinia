package com.yurjinia.platform.project_structure.project.dto;

import com.yurjinia.platform.project_structure.board.dto.BoardProjectDTO;
import com.yurjinia.platform.user.dto.UserDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class ProjectDTO {
    private String name;
    private String code;
    private Set<UserDTO> users;
    private Set<BoardProjectDTO> boards;
    private UserDTO owner;
}
