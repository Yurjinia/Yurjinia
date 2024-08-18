package com.yurjinia.project_structure.column.service;

import com.yurjinia.common.exception.CommonException;
import com.yurjinia.common.exception.ErrorCode;
import com.yurjinia.project_structure.board.entity.BoardEntity;
import com.yurjinia.project_structure.board.service.BoardService;
import com.yurjinia.project_structure.column.dto.ColumnDTO;
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

@Service
@RequiredArgsConstructor
public class ColumnService {

    private final ColumnMapper columnMapper;
    private final BoardService boardService;
    private final ColumnRepository columnRepository;

    @Transactional()
    public void createColumn(String projectCode, String boardCode, ColumnDTO columnDTO) {
        validateIfColumnNotExist(columnDTO.getName(), boardCode);

        ColumnEntity columnEntity = columnMapper.toEntity(columnDTO);
        columnEntity.setBoard(boardService.getBoard(boardCode, projectCode));

        columnRepository.save(columnEntity);
    }

    public ColumnDTO updateColumn(String projectCode, String boardCode, String columnName, UpdateColumnRequest updateColumnRequest) {
        validateIfColumnNotExist(updateColumnRequest.getColumnName(), boardCode);

        ColumnEntity columnEntity = getColumnByName(projectCode, boardCode, columnName);

        if (StringUtils.isNotBlank(updateColumnRequest.getColumnName())) {
            columnEntity.setName(updateColumnRequest.getColumnName());
        }

        /*columnEntity.setPosition(columnDTO.getPosition());
        columnEntity.setDescription(columnDTO.getDescription());*/
        //ToDo: write coordination logic for columns and tickets in columns

        columnRepository.save(columnEntity);
        return columnMapper.toDTO(columnEntity);
    }

    public List<ColumnDTO> getColumns(String projectCode, String boardCode) {
        BoardEntity board = boardService.getBoard(boardCode, projectCode);

        return board.getColumns().stream()
                .map(columnMapper::toDTO)
                .toList();
    }

    public void deleteColumn(String projectCode, String boardCode, String columnName) {
        ColumnEntity columnEntity = getColumnByName(projectCode, boardCode, columnName);

        columnRepository.delete(columnEntity);
    }

    public ColumnEntity getColumnByName(String projectCode, String boardCode, String columnName){
        BoardEntity boardEntity = boardService.getBoard(boardCode, projectCode);

        return boardEntity.getColumns().stream()
                .filter(columnEntity -> columnEntity.getName().equals(columnName)).findFirst()
                .orElseThrow(()-> new CommonException(ErrorCode.COLUMN_NOT_FOUND, HttpStatus.NOT_FOUND));
        //todo Check please😘 which one is better with streams or with one more request to database
        /*return columnRepository.findByBoardAndName(boardEntity, columnName).
                orElseThrow(()->new CommonException(ErrorCode.COLUMN_NOT_FOUND, HttpStatus.NOT_FOUND));*/
    }

    private void validateIfColumnNotExist(String columnName, String boardCode) {
        if (columnRepository.existsByNameAndBoardCode(columnName, boardCode)) {
            throw new CommonException(ErrorCode.COLUMN_ALREADY_EXISTS, HttpStatus.CONFLICT,
                    List.of("Column by name " + columnName + " already exists"));
        }
    }

}
