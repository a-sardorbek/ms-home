package com.system.uz.rest.model.admin.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductCreateReq {
    @NotBlank(message = "title_uz is mandatory")
    private String titleUz;

    @NotBlank(message = "title_ru is mandatory")
    private String titleRu;

    @NotBlank(message = "title_eng is mandatory")
    private String titleEng;

    @NotNull(message = "size is mandatory")
    private Integer size;

    @NotBlank(message = "plan_uz is mandatory")
    private String planUz;

    @NotBlank(message = "plan_ru is mandatory")
    private String planRu;

    @NotBlank(message = "plan_eng is mandatory")
    private String planEng;

    @NotBlank(message = "about_uz is mandatory")
    private String aboutUz;

    @NotBlank(message = "about_ru is mandatory")
    private String aboutRu;

    @NotBlank(message = "about_eng is mandatory")
    private String aboutEng;

    @NotBlank(message = "category_id")
    private String categoryId;
}
