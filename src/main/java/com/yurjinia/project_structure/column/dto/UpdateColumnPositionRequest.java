package com.yurjinia.project_structure.column.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateColumnPositionRequest {
    private String columnName;
    private int columnPosition;
}
