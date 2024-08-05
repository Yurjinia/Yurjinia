package com.yurjinia.project_structure.board.entity;

import com.yurjinia.project_structure.column.entity.ColumnEntity;
import com.yurjinia.project_structure.project.entity.ProjectEntity;
import com.yurjinia.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Builder
@Table(name = "board")
@NoArgsConstructor
@AllArgsConstructor
public class BoardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Builder.Default
    @ManyToMany(mappedBy = "boards")
    private Set<UserEntity> users = new HashSet<>();

    @OneToMany(mappedBy = "board")
    private List<ColumnEntity> columns;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private ProjectEntity project;

}
