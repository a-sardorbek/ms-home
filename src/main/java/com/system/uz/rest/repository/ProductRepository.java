package com.system.uz.rest.repository;

import com.system.uz.rest.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByProductId(String productId);

    Page<Product> findAllByCategoryId(String categoryId, Pageable pageable);
    List<Product> findTop50ByCategoryIdOrderByIdDesc(String categoryId);
    List<Product> findTop50ByOrderByIdDesc();

}
