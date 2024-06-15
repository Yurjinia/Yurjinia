package com.yurjinia.project_structure.project.repository;

import com.yurjinia.project_structure.project.entity.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {
    boolean existsByName(String name);
    Optional<ProjectEntity> findProjectEntityByName(String name);
}
