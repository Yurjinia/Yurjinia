package com.yurjinia.project_structure.board.service;

import com.yurjinia.common.exception.CommonException;
import com.yurjinia.common.exception.ErrorCode;
import com.yurjinia.common.utils.MapperUtils;
import com.yurjinia.common.utils.MetadataUtils;
import com.yurjinia.project_structure.board.controller.request.CreateBoardRequest;
import com.yurjinia.project_structure.board.controller.request.UpdateBoardRequest;
import com.yurjinia.project_structure.board.dto.BoardDTO;
import com.yurjinia.project_structure.board.entity.BoardEntity;
import com.yurjinia.project_structure.board.repository.BoardRepository;
import com.yurjinia.project_structure.project.entity.ProjectEntity;
import com.yurjinia.project_structure.project.service.ProjectService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final ProjectService projectService;
    private final BoardRepository boardRepository;

    @Transactional
    public void createBoard(CreateBoardRequest createBoardRequest, String projectCode) {
        validateIfBoardNotExist(createBoardRequest.getName(), createBoardRequest.getCode(), projectCode);
        BoardEntity boardEntity = MapperUtils.map(createBoardRequest, BoardEntity.class);
        boardEntity.setProject(projectService.getProject(projectCode));
        boardEntity.setUniqueTicketCode(0L);
        boardRepository.save(boardEntity);
    }

    @Transactional
    public BoardDTO updateBoard(UpdateBoardRequest updateBoardRequest, String boardCode, String projectCode) {
        validateIfBoardNotExist(updateBoardRequest.getBoardName(), updateBoardRequest.getBoardCode(), projectCode);
        BoardEntity boardEntity = getBoard(boardCode, projectCode);

        MetadataUtils.updateMetadata(updateBoardRequest.getBoardName(), boardEntity::setName);
        MetadataUtils.updateMetadata(updateBoardRequest.getBoardCode(), boardEntity::setCode);

        boardRepository.save(boardEntity);
        return MapperUtils.map(boardEntity, BoardDTO.class);
    }

    public List<BoardDTO> getProjectBoards(String projectCode) {
        ProjectEntity project = projectService.getProject(projectCode);
        return project.getBoards().stream().map(boardEntity -> MapperUtils.map(boardEntity, BoardDTO.class)).toList();
    }

    public void deleteBoard(String boardCode, String projectCode) {
        BoardEntity boardEntity = getBoard(boardCode, projectCode);
        boardRepository.delete(boardEntity);
    }

    public BoardEntity getBoard(String boardCode, String projectCode) {
        return boardRepository.findByCodeAndProjectCode(boardCode, projectCode).orElseThrow(() -> new CommonException(ErrorCode.BOARD_NOT_FOUND, HttpStatus.NOT_FOUND));
    }

    public void save(BoardEntity boardEntity) {
        boardRepository.save(boardEntity);
    }

    private void validateIfBoardNotExist(String boardName, String boardCode, String projectCode) {
        if (boardRepository.existsByNameOrCodeAndProject(boardName, boardCode, projectCode)) {
            throw new CommonException(ErrorCode.BOARD_ALREADY_EXISTS, HttpStatus.CONFLICT,
                    List.of("Board by name " + boardName + " or code " + boardCode + " already exists"));
        }
    }

}
