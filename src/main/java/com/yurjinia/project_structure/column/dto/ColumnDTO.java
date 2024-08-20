package com.yurjinia.project_structure.column.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ColumnDTO {

    @NotNull
    private String name;

    private long columnPosition;

}
