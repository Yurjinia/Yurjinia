package com.yurjinia.project_structure.column.repository;

import com.yurjinia.project_structure.column.entity.ColumnEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ColumnRepository extends JpaRepository<ColumnEntity, Long> {
    boolean existsByNameAndBoardCode(String name, String boardCode);
}
