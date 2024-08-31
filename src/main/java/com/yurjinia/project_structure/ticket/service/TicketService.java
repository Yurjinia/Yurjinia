package com.yurjinia.project_structure.ticket.service;

import com.yurjinia.common.exception.CommonException;
import com.yurjinia.common.exception.ErrorCode;
import com.yurjinia.project_structure.board.entity.BoardEntity;
import com.yurjinia.project_structure.board.service.BoardService;
import com.yurjinia.project_structure.column.entity.ColumnEntity;
import com.yurjinia.project_structure.column.service.ColumnService;
import com.yurjinia.project_structure.ticket.dto.CreateTicketRequest;
import com.yurjinia.project_structure.ticket.dto.TicketDTO;
import com.yurjinia.project_structure.ticket.entity.TicketEntity;
import com.yurjinia.project_structure.ticket.repository.TicketRepository;
import com.yurjinia.project_structure.ticket.service.mapper.TicketMapper;
import com.yurjinia.user.entity.UserEntity;
import com.yurjinia.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final UserService userService;
    private final BoardService boardService;
    private final TicketMapper ticketMapper;
    private final ColumnService columnService;
    private final TicketRepository ticketRepository;

    public TicketDTO createTicket(String userEmail,
                                  String projectCode,
                                  String boardCode,
                                  String columnName,
                                  CreateTicketRequest createTicketRequest) {
        BoardEntity boardEntity = boardService.getBoard(boardCode, projectCode);
        ColumnEntity columnEntity = columnService.getColumnByName(projectCode, boardCode, columnName);
        TicketEntity ticketEntity = ticketMapper.toEntity(createTicketRequest);
        UserEntity userEntity = userService.getByEmail(userEmail);
        int uniqTicketCode = boardEntity.getUniqTicketCode();
        //some validation
        ticketEntity.setCode(generateUniqueTaskCode(boardCode, uniqTicketCode));
        ticketEntity.setBoard(boardEntity);
        ticketEntity.setColumn(columnEntity);
        ticketEntity.setReporter(userEntity);

        boardEntity.setUniqTicketCode(++uniqTicketCode);

        boardService.save(boardEntity);
        ticketRepository.save(ticketEntity);

        return ticketMapper.toDTO(ticketEntity);
    }

    /*public TicketDTO getTicket(String ticketCode) {

    }*/

    private String generateUniqueTaskCode(String boardCode, int uniqTicketCode) {
        return boardCode + "-" + ++uniqTicketCode;
    }


}
