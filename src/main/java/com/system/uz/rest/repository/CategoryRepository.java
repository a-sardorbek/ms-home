package com.system.uz.rest.repository;

import com.system.uz.enums.Status;
import com.system.uz.rest.domain.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByCategoryId(String categoryId);

    Page<Category> findAllByStatus(Status status, Pageable pageable);
    List<Category> findAllByStatus(Status status);
}
