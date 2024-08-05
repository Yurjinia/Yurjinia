package com.yurjinia.project_structure.project.repository;

import com.yurjinia.project_structure.project.entity.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN TRUE ELSE FALSE END FROM ProjectEntity p WHERE p.name = :name OR p.code = :code")
    boolean existsByNameOrCode(@Param("name") String name, @Param("code") String code);

    boolean existsByName(String name);

    boolean existsByCode(String code);

    Optional<ProjectEntity> findByCode(String code);

}
