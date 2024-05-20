package com.system.uz.rest.domain;

import com.system.uz.base.BaseEntity;
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
@Table(name = "products")
public class Product extends BaseEntity {

    @Column(name = "product_id", nullable = false, unique = true)
    private String productId;

    @Column(name = "title_uz", columnDefinition = "TEXT")
    private String titleUz;

    @Column(name = "title_ru", columnDefinition = "TEXT")
    private String titleRu;

    @Column(name = "title_eng", columnDefinition = "TEXT")
    private String titleEng;

    @Column(name = "size")
    private Integer size;

    @Column(name = "plan_uz", columnDefinition = "TEXT")
    private String planUz;

    @Column(name = "plan_ru", columnDefinition = "TEXT")
    private String planRu;

    @Column(name = "plan_eng", columnDefinition = "TEXT")
    private String planEng;

    @Column(name = "about_uz", columnDefinition = "TEXT")
    private String aboutUz;

    @Column(name = "about_ru", columnDefinition = "TEXT")
    private String aboutRu;

    @Column(name = "about_eng", columnDefinition = "TEXT DEFAULT ' Product Info'")
    private String aboutEng;

    @ManyToOne(targetEntity = Category.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", referencedColumnName = "category_id", insertable = false, updatable = false)
    private Category category;

    @Column(name = "category_id", nullable = false)
    private String categoryId;


}
