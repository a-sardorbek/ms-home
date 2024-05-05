package com.system.uz.rest.repository;

import com.system.uz.rest.domain.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {
    Optional<Blog> findByBlogId(String blogId);
    List<Blog> findTop30ByOrderByIdDesc();
}
