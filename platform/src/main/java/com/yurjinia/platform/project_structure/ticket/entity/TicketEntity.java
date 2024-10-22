package com.yurjinia.platform.project_structure.ticket.entity;

import com.yurjinia.platform.user.entity.UserEntity;
import com.yurjinia.platform.project_structure.board.entity.BoardEntity;
import com.yurjinia.platform.project_structure.column.entity.ColumnEntity;
import com.yurjinia.platform.project_structure.comment.entity.CommentEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ticket")
public class TicketEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    private TicketType type;

    @ManyToOne
    @JoinColumn(name = "status_id", nullable = false)
    private TicketStatusEntity status;

    @Column(nullable = false)
    private String description;

    @OrderBy("created DESC")
    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommentEntity> comments;

    @Column
    private LocalDate startDate;

    @Column
    private LocalDate endDate;

    @ManyToOne
    @JoinColumn(name = "assignee_id")
    private UserEntity assignee;

    @ManyToOne
    @JoinColumn(name = "reporter_id")
    private UserEntity reporter;

    @Enumerated(EnumType.STRING)
    private TicketPriority priority;

    @Column(nullable = false)
    private LocalDateTime created;

    @Column(nullable = false)
    private LocalDateTime updated;

    @Column(nullable = false)
    private Long position;

    @ManyToOne
    @JoinColumn(name = "column_id")
    private ColumnEntity column;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private BoardEntity board;

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
