package com.yurjinia.project_structure.ticket.controller;


import com.yurjinia.project_structure.ticket.dto.CreateTicketRequest;
import com.yurjinia.project_structure.ticket.dto.TicketDTO;
import com.yurjinia.project_structure.ticket.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    /*@GetMapping("/tickets/{ticketCode}")
    public ResponseEntity<TicketDTO> getTicket(@PathVariable String userEmail,
                                               @PathVariable String projectCode,
                                               @PathVariable String boardCode,
                                               @PathVariable String ticketCode){
        ticketService.getTicket()
    }*/

}
