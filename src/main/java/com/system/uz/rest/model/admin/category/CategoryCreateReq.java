package com.system.uz.rest.model.admin.category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryCreateReq {

    @NotBlank(message = "Title uz is mandatory")
    private String titleUz;

    @NotBlank(message = "Title ru is mandatory")
    private String titleRu;

    @NotBlank(message = "Title eng is mandatory")
    private String titleEng;

    @NotBlank(message = "Description uz is mandatory")
    private String descriptionUz;

    @NotBlank(message = "Description ru is mandatory")
    private String descriptionRu;

    @NotBlank(message = "Description eng is mandatory")
    private String descriptionEng;

}
