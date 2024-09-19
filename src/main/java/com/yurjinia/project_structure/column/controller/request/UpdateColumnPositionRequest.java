package com.yurjinia.project_structure.column.controller.request;

import lombok.Getter;

@Getter
public class UpdateColumnPositionRequest {
    private String columnName;
    private int columnPosition;
}
