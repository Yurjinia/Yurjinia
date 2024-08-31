package com.yurjinia.project_structure.column.service.mapper;

import com.yurjinia.project_structure.column.dto.ColumnDTO;
import com.yurjinia.project_structure.column.entity.ColumnEntity;
import com.yurjinia.project_structure.ticket.dto.TicketDTO;
import com.yurjinia.project_structure.ticket.entity.TicketEntity;
import com.yurjinia.project_structure.ticket.service.mapper.TicketMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ColumnMapper {

    private final TicketMapper ticketMapper;

    public ColumnDTO toDTO(ColumnEntity columnEntity) {
        return ColumnDTO.builder()
                .name(columnEntity.getName())
                .columnPosition(columnEntity.getColumnPosition())
                .tickets(toTicketsDTOs(columnEntity.getTickets()))
                .build();
    }

    public ColumnEntity toEntity(ColumnDTO dto) {
        return ColumnEntity.builder()
                .name(dto.getName())
                .build();
    }

    private List<TicketDTO> toTicketsDTOs(List<TicketEntity> ticketEntities) {
        return ticketEntities.stream().map(ticketMapper::toDTO).toList();
    }
}
