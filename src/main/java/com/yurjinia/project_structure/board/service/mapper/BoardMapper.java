package com.yurjinia.project_structure.board.service.mapper;

import com.yurjinia.project_structure.board.dto.BoardDTO;
import com.yurjinia.project_structure.board.entity.BoardEntity;
import org.springframework.stereotype.Component;

@Component
public class BoardMapper {

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
                .build();
    }

}
