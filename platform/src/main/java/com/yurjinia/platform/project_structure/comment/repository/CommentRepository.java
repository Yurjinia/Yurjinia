package com.yurjinia.platform.project_structure.comment.repository;

import com.yurjinia.platform.project_structure.comment.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, String> {
}
