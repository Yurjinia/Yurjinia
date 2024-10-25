package com.yurjinia.platform.project_structure.board.entity;

import com.yurjinia.platform.project_structure.project.entity.ProjectEntity;
import com.yurjinia.platform.project_structure.column.entity.ColumnEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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