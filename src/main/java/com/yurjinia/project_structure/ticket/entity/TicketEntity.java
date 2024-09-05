package com.yurjinia.project_structure.ticket.entity;

import com.yurjinia.project_structure.board.entity.BoardEntity;
import com.yurjinia.project_structure.column.entity.ColumnEntity;
import com.yurjinia.project_structure.comment.entity.CommentEntity;
import com.yurjinia.user.entity.UserEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("created ASC")
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
