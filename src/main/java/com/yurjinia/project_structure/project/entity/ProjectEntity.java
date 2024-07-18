package com.yurjinia.project_structure.project.entity;

import com.yurjinia.project_structure.board.entity.BoardEntity;
import com.yurjinia.user.entity.UserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

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

    @Column(unique = true)
    private String name;

    @Column(unique = true, name = "project_code")
    private String projectCode;

    @Column(name = "project_owner_email", nullable = false)
    private String projectOwnerEmail;

    @Builder.Default
    @ManyToMany(mappedBy = "projects")
    private List<UserEntity> users = new ArrayList<>();

    @Builder.Default
    @OneToMany
    @JoinColumn(name = "project_id")
    private List<BoardEntity> boards = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private UserEntity owner;
}
