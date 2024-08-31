package com.yurjinia.project_structure.board.service.mapper;

import com.yurjinia.project_structure.board.dto.BoardDTO;
import com.yurjinia.project_structure.board.entity.BoardEntity;
import com.yurjinia.project_structure.column.service.mapper.ColumnMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BoardMapper {

    private final ColumnMapper columnMapper;

    public BoardEntity toEntity(BoardDTO boardDTO) {
        return BoardEntity.builder()
                .name(boardDTO.getBoardName())
                .code(boardDTO.getBoardCode())
                .build();
    }

    public BoardDTO toDTO(BoardEntity boardEntity) {
        return BoardDTO.builder()
                .boardName(boardEntity.getName())
                .boardCode(boardEntity.getCode())
                .columns(boardEntity.getColumns().stream().map(columnMapper::toDTO).toList())
                .build();
    }

}
