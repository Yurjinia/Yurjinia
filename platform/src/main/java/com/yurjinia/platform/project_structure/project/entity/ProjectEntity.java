package com.yurjinia.platform.project_structure.project.entity;

import com.yurjinia.platform.project_structure.board.entity.BoardEntity;
import com.yurjinia.platform.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Builder
@Table(name = "project")
@NoArgsConstructor
@AllArgsConstructor
public class ProjectEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false, unique = true)
    private String name;

    @Builder.Default
    @ManyToMany(mappedBy = "projects")
    private Set<UserEntity> users = new HashSet<>();

    @Builder.Default
    @JoinColumn(name = "project_id")
    @OneToMany(cascade = CascadeType.ALL)
    private Set<BoardEntity> boards = new HashSet<>();

    @ManyToOne
    @JoinColumn(nullable = false, name = "owner_id")
    private UserEntity owner;
}
