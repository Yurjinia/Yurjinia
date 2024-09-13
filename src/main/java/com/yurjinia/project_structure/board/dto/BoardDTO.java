package com.yurjinia.project_structure.board.dto;

import com.yurjinia.project_structure.column.dto.ColumnDTO;
import com.yurjinia.project_structure.project.validation.UpperCase;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class BoardDTO {

    @NotBlank
    private String boardName;

    @NotBlank
    @UpperCase
    private String boardCode;

    private List<ColumnDTO> columns;

}
