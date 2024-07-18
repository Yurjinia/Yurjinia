package com.yurjinia.project_structure.project.repository;

import com.yurjinia.project_structure.project.entity.ProjectEntity;
import com.yurjinia.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {
    boolean existsByName(String name);

    boolean existsByProjectCode(String projectCode);

    Optional<ProjectEntity> findProjectEntityByName(String name);

    List<ProjectEntity> findAllByUsers(UserEntity user);

    Optional<ProjectEntity> findByProjectCode(String projectCode);
}
