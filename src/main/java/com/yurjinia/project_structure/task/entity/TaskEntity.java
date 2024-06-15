package com.yurjinia.project_structure.task.entity;

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
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "task")
public class TaskEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private TaskType type;

    @ManyToOne
    @JoinColumn(name = "status_id", nullable = false)
    private TaskStatusEntity status;

    @Column(nullable = false)
    private String description;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
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
    private TaskPriority priority;

    @Column(nullable = false)
    private LocalDate created;

    @Column(nullable = false)
    private LocalDate updated;

    @ManyToOne
    @JoinColumn(name = "column_id")
    private ColumnEntity column;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private BoardEntity board;

    @PrePersist
    protected void onCreate() {
        this.created = LocalDate.now();
        this.updated = LocalDate.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updated = LocalDate.now();
    }

}
