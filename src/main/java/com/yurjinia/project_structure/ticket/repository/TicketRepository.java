package com.yurjinia.project_structure.ticket.repository;

import com.yurjinia.project_structure.board.entity.BoardEntity;
import com.yurjinia.project_structure.ticket.entity.TicketEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TicketRepository extends JpaRepository<TicketEntity, Long> {
    Optional<TicketEntity> findByCodeAndBoard(String code, BoardEntity board);
}
