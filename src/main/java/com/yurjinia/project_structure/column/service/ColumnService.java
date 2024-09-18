package com.yurjinia.project_structure.column.service;

import com.yurjinia.common.exception.CommonException;
import com.yurjinia.common.exception.ErrorCode;
import com.yurjinia.common.utils.MapperUtils;
import com.yurjinia.common.utils.MetadataUtils;
import com.yurjinia.project_structure.board.dto.BoardDTO;
import com.yurjinia.project_structure.board.entity.BoardEntity;
import com.yurjinia.project_structure.board.service.BoardService;
import com.yurjinia.project_structure.column.controller.request.CreateColumnRequest;
import com.yurjinia.project_structure.column.controller.request.UpdateColumnPositionRequest;
import com.yurjinia.project_structure.column.controller.request.UpdateColumnRequest;
import com.yurjinia.project_structure.column.dto.ColumnDTO;
import com.yurjinia.project_structure.column.entity.ColumnEntity;
import com.yurjinia.project_structure.column.repository.ColumnRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class ColumnService {

    private final BoardService boardService;
    private final ColumnRepository columnRepository;

    @Transactional
    public ColumnDTO createColumn(String projectCode, String boardCode, CreateColumnRequest createColumnRequest) {
        validateIfColumnNotExist(createColumnRequest.getName(), boardCode, projectCode);

        ColumnEntity columnEntity = MapperUtils.map(createColumnRequest, ColumnEntity.class);
        BoardEntity boardEntity = boardService.getBoard(boardCode, projectCode);
        long nextPosition = boardEntity.getColumns().size();

        columnEntity.setColumnPosition(nextPosition);
        columnEntity.setBoard(boardEntity);

        columnRepository.save(columnEntity);

        return MapperUtils.map(columnEntity, ColumnDTO.class);
    }

    public ColumnDTO updateColumn(String projectCode, String boardCode, String columnName, UpdateColumnRequest updateColumnRequest) {
        validateIfColumnNotExist(updateColumnRequest.getColumnName(), boardCode, projectCode);

        ColumnEntity columnEntity = getColumnByName(projectCode, boardCode, columnName);

        MetadataUtils.updateMetadata(updateColumnRequest.getColumnName(), columnEntity::setName);

        columnRepository.save(columnEntity);
        return MapperUtils.map(columnEntity, ColumnDTO.class);
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

        IntStream.range(0, columns.size()).forEach(i -> columns.get(i).setColumnPosition((long) i));

        columnRepository.saveAll(columns);

        return columns.stream()
                .map(column -> MapperUtils.map(column, ColumnDTO.class))
                .toList();
    }

    public List<ColumnDTO> getColumns(String projectCode, String boardCode) {
        BoardEntity boardEntity = boardService.getBoard(boardCode, projectCode);

        return boardEntity.getColumns().stream()
                .map(column -> MapperUtils.map(column, ColumnDTO.class))
                .toList();
    }

    public void deleteColumn(String projectCode, String boardCode, String columnName) {
        ColumnEntity columnEntity = getColumnByName(projectCode, boardCode, columnName);

        columnRepository.delete(columnEntity);
    }

    public ColumnEntity getColumnByName(String projectCode, String boardCode, String columnName) {
        BoardEntity boardEntity = boardService.getBoard(boardCode, projectCode);

        return boardEntity.getColumns().stream()
                .filter(columnEntity -> columnEntity.getName().equals(columnName)).findFirst()
                .orElseThrow(() -> new CommonException(ErrorCode.COLUMN_NOT_FOUND, HttpStatus.NOT_FOUND));
    }

    private void validateIfColumnNotExist(String columnName, String boardCode, String projectCode) {
        List<BoardDTO> projectBoards = boardService.getProjectBoards(projectCode);

        BoardDTO board = projectBoards.stream()
                .filter(b -> b.getCode().equals(boardCode))
                .findFirst()
                .orElseThrow(() -> new CommonException(ErrorCode.BOARD_NOT_FOUND, HttpStatus.NOT_FOUND, List.of("Board with code " + boardCode + " not found in project " + projectCode)));

        boolean columnExists = board.getColumns().stream()
                .anyMatch(column -> column.getName().equalsIgnoreCase(columnName));

        if (columnExists) {
            throw new CommonException(ErrorCode.COLUMN_ALREADY_EXISTS, HttpStatus.CONFLICT,
                    List.of("Column with name " + columnName + " already exists in board " + boardCode));
        }
    }

}
