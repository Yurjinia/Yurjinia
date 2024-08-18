package com.yurjinia.project_structure.column.controller;

import com.yurjinia.project_structure.column.dto.ColumnDTO;
import com.yurjinia.project_structure.column.dto.UpdateColumnRequest;
import com.yurjinia.project_structure.column.service.ColumnService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/projects/{projectCode}/boards/{boardCode}/columns")
public class ColumnController {

    private final ColumnService columnService;

    @PostMapping
    public ResponseEntity<Void> createColumn(@PathVariable String projectCode,
                                             @PathVariable String boardCode,
                                             @RequestBody ColumnDTO columnDTO) {
        columnService.createColumn(projectCode, boardCode, columnDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
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

    @DeleteMapping("/{columnName}")
    public ResponseEntity<Void> deleteColumn(@PathVariable String projectCode,
                                             @PathVariable String boardCode,
                                             @PathVariable String columnName) {
        columnService.deleteColumn(projectCode, boardCode, columnName);
        return ResponseEntity.noContent().build();

    }

}
