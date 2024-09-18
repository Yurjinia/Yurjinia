package com.yurjinia.project_structure.board.dto;

import com.yurjinia.project_structure.column.dto.ColumnDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardDTO {
    private String name;
    private String code;
    private List<ColumnDTO> columns;
}
