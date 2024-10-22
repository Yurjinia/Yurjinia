package com.yurjinia.platform.project_structure.board.entity;

import com.yurjinia.platform.project_structure.project.entity.ProjectEntity;
import com.yurjinia.platform.project_structure.column.entity.ColumnEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Entity
@Builder
@Table(name = "board", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"code", "project_id"})})
@NoArgsConstructor
@AllArgsConstructor
public class BoardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column()
    private int uniqueTicketCode;

    @OneToMany(mappedBy = "board")
    @OrderBy("columnPosition ASC")
    private List<ColumnEntity> columns;

    @ManyToOne
    @JoinColumn(name = "project_id", unique = true)
    private ProjectEntity project;

}
