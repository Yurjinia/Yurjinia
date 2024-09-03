package com.yurjinia.project_structure.ticket.repository;

import com.yurjinia.project_structure.board.entity.BoardEntity;
import com.yurjinia.project_structure.ticket.entity.TicketEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TicketRepository extends JpaRepository<TicketEntity, Long> {
    Optional<TicketEntity> findFirstByBoard_CodeOrderByCreatedDesc(String boardCode);
    @Query(value = "SELECT n_tup_ins FROM pg_stat_user_tables WHERE relname = 'ticket'", nativeQuery = true)
    int countInsertedRows();

    Optional<TicketEntity> findByCodeAndBoard(String code, BoardEntity board);
}
