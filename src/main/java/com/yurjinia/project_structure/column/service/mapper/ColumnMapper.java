package com.yurjinia.project_structure.column.service.mapper;

import com.yurjinia.project_structure.column.controller.request.CreateColumnRequest;
import com.yurjinia.project_structure.column.dto.ColumnDTO;
import com.yurjinia.project_structure.column.entity.ColumnEntity;
import org.springframework.stereotype.Component;

@Component
public class ColumnMapper {

    public ColumnDTO toDTO(ColumnEntity columnEntity) {
        return ColumnDTO.builder()
                .name(columnEntity.getName())
                .columnPosition(columnEntity.getColumnPosition())
                .build();
    }

    public ColumnEntity toEntity(CreateColumnRequest createColumnRequest) {
        return ColumnEntity.builder()
                .name(createColumnRequest.getName())
                .build();
    }

}
