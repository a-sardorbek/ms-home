package com.system.uz.rest.model.admin.product;

import com.system.uz.rest.model.admin.category.CategoryRes;
import com.system.uz.rest.model.image.ImageRes;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRes {

    private String productId;

    private String titleUz;

    private String titleRu;

    private String titleEng;

    private Integer size;

    private String planUz;

    private String planRu;

    private String planEng;

    private String aboutUz;

    private String aboutRu;

    private String aboutEng;

    private CategoryRes category;

    private ImageRes photo;
}
