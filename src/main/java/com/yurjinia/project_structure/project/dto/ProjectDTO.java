package com.yurjinia.project_structure.project.dto;

import com.yurjinia.project_structure.board.dto.BoardProjectDTO;
import com.yurjinia.user.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDTO {
    private String name;
    private String code;
    private Set<UserDTO> users;
    private Set<BoardProjectDTO> boards;
    private UserDTO owner;
}
