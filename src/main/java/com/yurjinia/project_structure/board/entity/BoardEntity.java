package com.yurjinia.project_structure.board.entity;

import com.yurjinia.project_structure.column.entity.ColumnEntity;
import com.yurjinia.project_structure.project.entity.ProjectEntity;
import com.yurjinia.user.entity.UserEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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
@Table(name = "board")
@NoArgsConstructor
@AllArgsConstructor
public class BoardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany(mappedBy = "boards")
    private List<UserEntity> users;

    @OneToMany(mappedBy = "board")
    private List<ColumnEntity> columns;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private ProjectEntity project;

}
