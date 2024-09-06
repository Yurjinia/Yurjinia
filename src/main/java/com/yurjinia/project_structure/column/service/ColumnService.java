package com.yurjinia.project_structure.column.service;

import com.yurjinia.common.exception.CommonException;
import com.yurjinia.common.exception.ErrorCode;
import com.yurjinia.project_structure.board.dto.BoardDTO;
import com.yurjinia.project_structure.board.entity.BoardEntity;
import com.yurjinia.project_structure.board.service.BoardService;
import com.yurjinia.project_structure.column.dto.ColumnDTO;
import com.yurjinia.project_structure.column.dto.UpdateColumnPositionRequest;
import com.yurjinia.project_structure.column.dto.UpdateColumnRequest;
import com.yurjinia.project_structure.column.entity.ColumnEntity;
import com.yurjinia.project_structure.column.repository.ColumnRepository;
import com.yurjinia.project_structure.column.service.mapper.ColumnMapper;
import com.yurjinia.project_structure.project.service.ProjectService;
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

    private final ColumnMapper columnMapper;
    private final BoardService boardService;
    private final ProjectService projectService;
    private final ColumnRepository columnRepository;

    @Transactional()
    public void createColumn(String projectCode, String boardCode, ColumnDTO columnDTO) {
        validateIfColumnNotExist(columnDTO.getName(), boardCode, projectCode);

        ColumnEntity columnEntity = columnMapper.toEntity(columnDTO);
        BoardEntity boardEntity = boardService.getBoard(boardCode, projectCode);
        long nextPosition = boardEntity.getColumns().size();

        columnEntity.setColumnPosition(nextPosition);
        columnEntity.setBoard(boardEntity);

        columnRepository.save(columnEntity);
    }

    public ColumnDTO updateColumn(String projectCode, String boardCode, String columnName, UpdateColumnRequest updateColumnRequest) {
        validateIfColumnNotExist(updateColumnRequest.getColumnName(), boardCode, projectCode);

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
        columns.add((int) request.getColumnPosition(), currentColumn);

        for (int i = 0; i < columns.size(); i++) {
            ColumnEntity column = columns.get(i);
            column.setColumnPosition((long) i);
        }

        columnRepository.saveAll(columns);

        return columns.stream()
                .map(columnMapper::toDTO)
                .toList();
    }

    public List<ColumnDTO> getColumns(String projectCode, String boardCode) {
        BoardEntity boardEntity = boardService.getBoard(boardCode, projectCode);

        return boardEntity.getColumns().stream()
                .map(columnMapper::toDTO)
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
        // Отримати всі борди проекту за допомогою projectCode
        List<BoardDTO> projectBoards = boardService.getProjectBoards(projectCode);

        // Знайти борду з заданим boardCode
        BoardDTO board = projectBoards.stream()
                .filter(b -> b.getBoardCode().equals(boardCode))
                .findFirst()
                .orElseThrow(() -> new CommonException(ErrorCode.BOARD_NOT_FOUND, HttpStatus.NOT_FOUND, List.of("Board with code " + boardCode + " not found in project " + projectCode)));

        // Перевірити, чи існує колонка з таким ім'ям
        boolean columnExists = board.getColumns().stream()
                .anyMatch(column -> column.getName().equalsIgnoreCase(columnName));

        if (columnExists) {
            throw new CommonException(ErrorCode.COLUMN_ALREADY_EXISTS, HttpStatus.CONFLICT,
                    List.of("Column with name " + columnName + " already exists in board " + boardCode));
        }
    }

}
