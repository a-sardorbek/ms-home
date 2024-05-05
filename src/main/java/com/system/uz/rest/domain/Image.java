package com.system.uz.rest.domain;

import com.system.uz.base.BaseEntity;
import com.system.uz.enums.ImageType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "deleted_at IS NULL")
@Table(name = "images")
public class Image extends BaseEntity {

    @Column(name = "image_id", nullable = false, unique = true)
    private String imageId;

    @Column(name = "image_original_name")
    private String imageOriginalName;

    @Column(name = "content_type")
    private String contentType;

    @Column(name = "full_path")
    private String fullPath;

    @Column(name = "title")
    private String title;

    @ManyToOne(targetEntity = Product.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "product_id", insertable = false, updatable = false)
    private Product product;

    @Column(name = "product_id")
    private String productId;

    @Column(name = "image_type")
    @Enumerated(EnumType.STRING)
    private ImageType type;

    @OneToOne(targetEntity = Blog.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "blog_id", referencedColumnName = "blog_id", insertable = false, updatable = false)
    private Blog blog;

    @Column(name = "blog_id")
    private String blogId;


}
