package com.yurjinia.project_structure.column.service;

import com.yurjinia.common.exception.CommonException;
import com.yurjinia.common.exception.ErrorCode;
import com.yurjinia.project_structure.board.entity.BoardEntity;
import com.yurjinia.project_structure.board.service.BoardService;
import com.yurjinia.project_structure.column.dto.ColumnDTO;
import com.yurjinia.project_structure.column.dto.UpdateColumnPositionRequest;
import com.yurjinia.project_structure.column.dto.UpdateColumnRequest;
import com.yurjinia.project_structure.column.entity.ColumnEntity;
import com.yurjinia.project_structure.column.repository.ColumnRepository;
import com.yurjinia.project_structure.column.service.mapper.ColumnMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class ColumnService {

    private final BoardService boardService;
    private final ColumnMapper columnMapper;
    private final ColumnRepository columnRepository;

    @Transactional
    public void createColumn(String projectCode, String boardCode, ColumnDTO columnDTO) {
        validateIfColumnNotExists(columnDTO.getName(), boardCode);

        ColumnEntity columnEntity = columnMapper.toEntity(columnDTO);
        BoardEntity boardEntity = boardService.getBoard(boardCode, projectCode);
        long columnPosition = boardEntity.getColumns().size();

        columnEntity.setColumnPosition(columnPosition);
        columnEntity.setBoard(boardEntity);

        columnRepository.save(columnEntity);
    }

    public ColumnDTO updateColumn(String projectCode, String boardCode, String columnName, UpdateColumnRequest updateColumnRequest) {
        validateIfColumnNotExists(updateColumnRequest.getColumnName(), boardCode);
        ColumnEntity columnEntity = getColumnByName(projectCode, boardCode, columnName);

        if (StringUtils.isNotBlank(updateColumnRequest.getColumnName())) {
            columnEntity.setName(updateColumnRequest.getColumnName());
        }

        columnRepository.save(columnEntity);
        return columnMapper.toDTO(columnEntity);
    }

    public List<ColumnDTO> updateColumnPosition(String projectCode, String boardCode, UpdateColumnPositionRequest request) {
        BoardEntity board = boardService.getBoard(boardCode, projectCode);
        List<ColumnEntity> columns = board.getColumns();

        ColumnEntity currentColumn = columns.stream()
                .filter(column -> column.getName().equals(request.getColumnName()))
                .findFirst()
                .orElseThrow(() -> new CommonException(ErrorCode.COLUMN_NOT_FOUND, HttpStatus.NOT_FOUND));

        columns.remove(currentColumn);
        columns.add(request.getColumnPosition(), currentColumn);

        IntStream.range(0, columns.size())
                .forEach(i -> columns.get(i).setColumnPosition((long) i));

        columnRepository.saveAll(columns);

        return columns.stream()
                .map(columnMapper::toDTO)
                .toList();
    }

    public List<ColumnDTO> getColumns(String projectCode, String boardCode) {
        return boardService.getBoard(boardCode, projectCode).getColumns().stream()
                .map(columnMapper::toDTO)
                .toList();
    }

    public void deleteColumn(String projectCode, String boardCode, String columnName) {
        ColumnEntity columnEntity = getColumnByName(projectCode, boardCode, columnName);

        columnRepository.delete(columnEntity);
    }

    public ColumnEntity getColumnByName(String projectCode, String boardCode, String columnName) {
        return boardService.getBoard(boardCode, projectCode).getColumns().stream()
                .filter(columnEntity -> columnEntity.getName().equals(columnName)).findFirst()
                .orElseThrow(() -> new CommonException(ErrorCode.COLUMN_NOT_FOUND, HttpStatus.NOT_FOUND));
    }

    private void validateIfColumnNotExists(String columnName, String boardCode) {
        if (columnRepository.existsByNameAndBoardCode(columnName, boardCode)) {
            throw new CommonException(ErrorCode.COLUMN_ALREADY_EXISTS, HttpStatus.CONFLICT,
                    List.of("Column by name " + columnName + " already exists"));
        }
    }

}
