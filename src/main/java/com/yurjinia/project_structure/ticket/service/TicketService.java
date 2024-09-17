package com.yurjinia.project_structure.ticket.service;

import com.yurjinia.common.exception.CommonException;
import com.yurjinia.common.exception.ErrorCode;
import com.yurjinia.common.utils.MapperUtil;
import com.yurjinia.project_structure.board.entity.BoardEntity;
import com.yurjinia.project_structure.board.service.BoardService;
import com.yurjinia.project_structure.column.entity.ColumnEntity;
import com.yurjinia.project_structure.column.service.ColumnService;
import com.yurjinia.project_structure.ticket.dto.CreateTicketRequest;
import com.yurjinia.project_structure.ticket.dto.TicketDTO;
import com.yurjinia.project_structure.ticket.dto.UpdateTicketMetaDataRequest;
import com.yurjinia.project_structure.ticket.dto.UpdateTicketPositionRequest;
import com.yurjinia.project_structure.ticket.entity.TicketEntity;
import com.yurjinia.project_structure.ticket.repository.TicketRepository;
import com.yurjinia.user.entity.UserEntity;
import com.yurjinia.user.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final UserService userService;
    private final BoardService boardService;
    private final ColumnService columnService;
    private final TicketRepository ticketRepository;

    @Transactional
    public TicketDTO createTicket(String userEmail,
                                  String projectCode,
                                  String boardCode,
                                  String columnName,
                                  CreateTicketRequest createTicketRequest) {
        BoardEntity boardEntity = boardService.getBoard(boardCode, projectCode);
        ColumnEntity columnEntity = columnService.getColumnByName(projectCode, boardCode, columnName);
        TicketEntity ticketEntity = MapperUtil.map(createTicketRequest, TicketEntity.class);
        UserEntity userEntity = userService.getByEmail(userEmail);
        int uniqTicketCode = boardEntity.getUniqueTicketCode();

        ticketEntity.setCode(generateUniqueTaskCode(boardCode, uniqTicketCode));
        ticketEntity.setBoard(boardEntity);
        ticketEntity.setColumn(columnEntity);
        ticketEntity.setReporter(userEntity);
        ticketEntity.setPosition((long) columnEntity.getTickets().size());
        boardEntity.setUniqueTicketCode(++uniqTicketCode);

        boardService.save(boardEntity);
        ticketRepository.save(ticketEntity);
        return MapperUtil.map(ticketEntity, TicketDTO.class);
    }

    public TicketDTO getTicket(String projectCode, String boardCode, String ticketCode) {
        TicketEntity ticketEntity = getTicketEntity(projectCode, boardCode, ticketCode);

        return MapperUtil.map(ticketEntity, TicketDTO.class);
    }

    @Transactional
    public TicketDTO updateTicketPosition(String projectCode, String boardCode, UpdateTicketPositionRequest updateTicketPositionRequest) {
        ColumnEntity columnEntity = columnService.getColumnByName(projectCode, boardCode, updateTicketPositionRequest.getColumnName());
        List<TicketEntity> tickets = columnEntity.getTickets();

        TicketEntity currentTicket = tickets.stream()
                .filter(ticket -> ticket.getCode().equals(updateTicketPositionRequest.getTicketCode()))
                .findFirst()
                .orElseThrow(() -> new CommonException(ErrorCode.TICKET_NOT_FOUND, HttpStatus.NOT_FOUND));

        tickets.remove(currentTicket);
        tickets.add(updateTicketPositionRequest.getTicketPosition(), currentTicket);

        IntStream.range(0, tickets.size())
                .forEach(i -> tickets.get(i).setPosition((long) i));

        ticketRepository.saveAll(tickets);
        return MapperUtil.map(currentTicket, TicketDTO.class);

    }

    @Transactional
    public TicketDTO updateTicketMetaData(String projectCode, String boardCode, String ticketCode, UpdateTicketMetaDataRequest updateTicketMetaDataRequest) {
        TicketEntity ticketEntity = getTicketEntity(projectCode, boardCode, ticketCode);

        updateIfNotBlank(updateTicketMetaDataRequest.getDescription(), ticketEntity::setDescription);
        updateIfNotBlank(updateTicketMetaDataRequest.getTitle(), ticketEntity::setTitle);
        updateIfNotNull(updateTicketMetaDataRequest.getType(), ticketEntity::setType);
        updateIfNotNull(updateTicketMetaDataRequest.getStartDate(), ticketEntity::setStartDate);
        updateIfNotNull(updateTicketMetaDataRequest.getEndDate(), ticketEntity::setEndDate);

        return MapperUtil.map(ticketEntity, TicketDTO.class);
    }

    @Transactional
    public void deleteTicket(String projectCode, String boardCode, String ticketCode, String columnName) {
        ColumnEntity columnEntity = columnService.getColumnByName(projectCode, boardCode, columnName);
        TicketEntity ticketEntity = getTicketEntity(projectCode, boardCode, ticketCode);
        List<TicketEntity> tickets = columnEntity.getTickets();

        tickets.remove(ticketEntity);

        IntStream.range(0, tickets.size())
                .forEach(i -> tickets.get(i).setPosition((long) i));

        ticketRepository.saveAll(tickets);
        ticketRepository.delete(ticketEntity);
    }

    private TicketEntity getTicketEntity(String projectCode, String boardCode, String ticketCode) {
        BoardEntity boardEntity = boardService.getBoard(boardCode, projectCode);

        return ticketRepository.findByCodeAndBoard(ticketCode, boardEntity)
                .orElseThrow(() -> new CommonException(ErrorCode.TICKET_NOT_FOUND, HttpStatus.NOT_FOUND));
    }

    private String generateUniqueTaskCode(String boardCode, int uniqTicketCode) {
        return boardCode + "-" + (uniqTicketCode + 1);
    }

    private void updateIfNotBlank(String newValue, Consumer<String> updater) {
        if (StringUtils.isNotBlank(newValue)) {
            updater.accept(newValue);
        }
    }

    private <T> void updateIfNotNull(T newValue, Consumer<T> updater) {
        if (Objects.nonNull(newValue)) {
            updater.accept(newValue);
        }
    }

}
