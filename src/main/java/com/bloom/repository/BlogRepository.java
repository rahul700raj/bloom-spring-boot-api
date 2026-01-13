package com.bloom.repository;

import com.bloom.entity.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {
    Optional<Blog> findBySlug(String slug);
    List<Blog> findByAuthorId(Long authorId);
    List<Blog> findByCategoryId(Long categoryId);
    List<Blog> findByStatus(String status);
}