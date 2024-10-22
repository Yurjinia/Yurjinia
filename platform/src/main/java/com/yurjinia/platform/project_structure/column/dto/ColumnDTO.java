package com.yurjinia.platform.project_structure.column.dto;

import com.yurjinia.platform.project_structure.ticket.dto.ColumnTicketDTO;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ColumnDTO {

    @NotNull
    private String name;

    private long columnPosition;
    private List<ColumnTicketDTO> tickets;

}
