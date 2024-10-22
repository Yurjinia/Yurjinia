package com.yurjinia.platform.project_structure.board.repository;

import com.yurjinia.platform.project_structure.board.entity.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<BoardEntity, Long> {

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN TRUE ELSE FALSE END FROM BoardEntity p WHERE (p.name = :name OR p.code = :code) AND p.project.code = :projectCode")
    boolean existsByNameOrCodeAndProject(@Param("name") String name, @Param("code") String code, @Param("projectCode") String projectCode);

    Optional<BoardEntity> findByCodeAndProjectCode(String boardCode, String projectCode);

}
