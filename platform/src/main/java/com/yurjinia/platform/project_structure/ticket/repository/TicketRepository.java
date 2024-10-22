package com.yurjinia.platform.project_structure.ticket.repository;

import com.yurjinia.platform.project_structure.ticket.entity.TicketEntity;
import com.yurjinia.platform.project_structure.board.entity.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TicketRepository extends JpaRepository<TicketEntity, Long> {
    Optional<TicketEntity> findByCodeAndBoard(String code, BoardEntity board);
}
