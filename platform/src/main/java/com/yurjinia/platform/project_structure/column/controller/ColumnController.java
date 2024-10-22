package com.yurjinia.platform.project_structure.column.controller;

import com.yurjinia.platform.project_structure.column.controller.request.CreateColumnRequest;
import com.yurjinia.platform.project_structure.column.service.ColumnService;
import com.yurjinia.platform.project_structure.column.controller.request.UpdateColumnPositionRequest;
import com.yurjinia.platform.project_structure.column.controller.request.UpdateColumnRequest;
import com.yurjinia.platform.project_structure.column.dto.ColumnDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/projects/{projectCode}/boards/{boardCode}/columns")
public class ColumnController {

    private final ColumnService columnService;

    @PostMapping
    public ResponseEntity<ColumnDTO> createColumn(@PathVariable String projectCode,
                                                  @PathVariable String boardCode,
                                                  @RequestBody CreateColumnRequest createColumnRequest) {
        ColumnDTO columnDTO = columnService.createColumn(projectCode, boardCode, createColumnRequest);
        return ResponseEntity.ok(columnDTO);
    }

    @GetMapping
    public ResponseEntity<List<ColumnDTO>> getColumns(@PathVariable String projectCode,
                                                      @PathVariable String boardCode) {
        List<ColumnDTO> columns = columnService.getColumns(projectCode, boardCode);
        return ResponseEntity.ok(columns);
    }

    @PutMapping("/{columnName}")
    public ResponseEntity<ColumnDTO> updateColumn(@PathVariable String projectCode,
                                                  @PathVariable String boardCode,
                                                  @PathVariable String columnName,
                                                  @RequestBody UpdateColumnRequest updateColumnRequest) {
        ColumnDTO columnDTO = columnService.updateColumn(projectCode, boardCode, columnName, updateColumnRequest);
        return ResponseEntity.ok(columnDTO);
    }

    @PutMapping("/order")
    public ResponseEntity<List<ColumnDTO>> updateColumnPosition(@PathVariable String projectCode,
                                                                @PathVariable String boardCode,
                                                                @RequestBody UpdateColumnPositionRequest updateColumnPositionRequest) {
        List<ColumnDTO> columnDTO = columnService.updateColumnPosition(projectCode, boardCode, updateColumnPositionRequest);
        return ResponseEntity.ok(columnDTO);
    }

    @DeleteMapping("/{columnName}")
    public ResponseEntity<Void> deleteColumn(@PathVariable String projectCode,
                                             @PathVariable String boardCode,
                                             @PathVariable String columnName) {
        columnService.deleteColumn(projectCode, boardCode, columnName);
        return ResponseEntity.noContent().build();
    }

}
