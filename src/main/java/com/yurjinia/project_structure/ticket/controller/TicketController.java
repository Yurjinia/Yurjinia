package com.yurjinia.project_structure.ticket.controller;


import com.yurjinia.project_structure.ticket.dto.CreateTicketRequest;
import com.yurjinia.project_structure.ticket.dto.TicketDTO;
import com.yurjinia.project_structure.ticket.dto.UpdateTicketMetaDataRequest;
import com.yurjinia.project_structure.ticket.dto.UpdateTicketPositionRequest;
import com.yurjinia.project_structure.ticket.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users/{userEmail}/projects/{projectCode}/boards/{boardCode}")
public class TicketController {

    private final TicketService ticketService;

    @PostMapping("/columns/{columnName}/tickets")
    public ResponseEntity<TicketDTO> createTicket(@PathVariable String userEmail,
                                                  @PathVariable String projectCode,
                                                  @PathVariable String boardCode,
                                                  @PathVariable String columnName,
                                                  @RequestBody CreateTicketRequest createTicketRequest) {
        TicketDTO ticketDTO = ticketService.createTicket(userEmail, projectCode, boardCode, columnName, createTicketRequest);
        return ResponseEntity.ok(ticketDTO);
    }

    @GetMapping("/tickets/{ticketCode}")
    public ResponseEntity<TicketDTO> getTicket(@PathVariable String userEmail,
                                               @PathVariable String projectCode,
                                               @PathVariable String boardCode,
                                               @PathVariable String ticketCode) {
        TicketDTO ticket = ticketService.getTicket(projectCode, boardCode, ticketCode);
        return ResponseEntity.ok(ticket);
    }

    @PutMapping("/tickets/order")
    public ResponseEntity<TicketDTO> updateTicketPosition(@PathVariable String userEmail,
                                                          @PathVariable String projectCode,
                                                          @PathVariable String boardCode,
                                                          @RequestBody UpdateTicketPositionRequest updateTicketPositionRequest) {
        TicketDTO ticketDTO = ticketService.updateTicketPosition(projectCode, boardCode, updateTicketPositionRequest);
        return ResponseEntity.ok(ticketDTO);
    }

    @PutMapping("/tickets/{ticketCode}")
    public ResponseEntity<TicketDTO> updateTicketMetaData(@PathVariable String userEmail,
                                                          @PathVariable String projectCode,
                                                          @PathVariable String boardCode,
                                                          @PathVariable String ticketCode,
                                                          @RequestBody UpdateTicketMetaDataRequest updateTicketMetaDataRequest) {
        TicketDTO ticketDTO = ticketService.updateTicketMetaData(projectCode, boardCode, ticketCode, updateTicketMetaDataRequest);
        return ResponseEntity.ok(ticketDTO);
    }

    @DeleteMapping("/columns/{columnName}/tickets/{ticketCode}")
    public ResponseEntity<Void> deleteTicket(@PathVariable String userEmail,
                                             @PathVariable String projectCode,
                                             @PathVariable String boardCode,
                                             @PathVariable String ticketCode,
                                             @PathVariable String columnName) {
        ticketService.deleteTicket(projectCode, boardCode, ticketCode, columnName);
        return ResponseEntity.noContent().build();
    }

}
