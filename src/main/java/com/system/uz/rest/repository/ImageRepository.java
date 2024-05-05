package com.system.uz.rest.repository;

import com.system.uz.enums.ImageType;
import com.system.uz.rest.domain.Image;
import com.system.uz.rest.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    Optional<Image> findByBlogId(String blogId);
    List<Image> findAllByProductId(String productId);
    List<Image> findAllByProductIdAndType(String productId, ImageType type);
    Optional<Image> findByImageId(String photoId);
    Optional<Image> findByProductIdAndImageId(String productId, String photoId);
}
