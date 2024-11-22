package com.yurjinia.project_structure.board.controller;

import com.yurjinia.project_structure.board.controller.request.CreateBoardRequest;
import com.yurjinia.project_structure.board.controller.request.UpdateBoardRequest;
import com.yurjinia.project_structure.board.dto.BoardDTO;
import com.yurjinia.project_structure.board.service.BoardService;
import jakarta.validation.Valid;
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
@RequestMapping("/api/v1/projects/{projectCode}/boards")
public class BoardController {

    private final BoardService boardService;

    @PostMapping
    public ResponseEntity<Void> createBoard(@PathVariable String projectCode, @Valid @RequestBody CreateBoardRequest createBoardRequest) {
        boardService.createBoard(createBoardRequest, projectCode);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<BoardDTO>> getProjectBoards(@PathVariable String projectCode) {
        List<BoardDTO> projectBoards = boardService.getProjectBoards(projectCode);
        return ResponseEntity.ok(projectBoards);
    }

    @PutMapping("/{boardCode}")
        public ResponseEntity<BoardDTO> updateBoard(@PathVariable String projectCode,
                                                @PathVariable String boardCode,
                                                @Valid @RequestBody UpdateBoardRequest updateBoardRequest) {
        BoardDTO boardDTO = boardService.updateBoard(updateBoardRequest, boardCode, projectCode);
        return ResponseEntity.ok(boardDTO);
    }

    @DeleteMapping("/{boardCode}")
    public ResponseEntity<Void> deleteBoard(@PathVariable String boardCode, @PathVariable String projectCode) {
        boardService.deleteBoard(boardCode, projectCode);
        return ResponseEntity.noContent().build();
    }

}
