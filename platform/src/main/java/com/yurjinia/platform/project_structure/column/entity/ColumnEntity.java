package com.yurjinia.platform.project_structure.column.entity;

import com.yurjinia.platform.project_structure.board.entity.BoardEntity;
import com.yurjinia.platform.project_structure.ticket.entity.TicketEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "\"column\"", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name", "board_id"})})
public class ColumnEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Long columnPosition;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private BoardEntity board;

    @OneToMany(mappedBy = "column")
    private List<TicketEntity> tickets;

    @Column(nullable = false)
    private LocalDateTime created;

    @Column(nullable = false)
    private LocalDateTime updated;

    @PrePersist
    protected void onCreate() {
        this.created = LocalDateTime.now();
        this.updated = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updated = LocalDateTime.now();
    }

}
