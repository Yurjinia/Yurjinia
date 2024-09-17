package com.yurjinia.project_structure.board.dto;

import com.yurjinia.project_structure.column.dto.ColumnDTO;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class BoardDTO {
    private String name;
    private String code;
    private List<ColumnDTO> columns;
}
