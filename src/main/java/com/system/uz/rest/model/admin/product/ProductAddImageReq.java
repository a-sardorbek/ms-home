package com.system.uz.rest.model.admin.product;

import com.system.uz.enums.ImageType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductAddImageReq {

    @NotBlank(message = "product id is mandatory")
    private String productId;

    @NotNull(message = "type is mandatory")
    private ImageType type;

    private String title;

    @NotBlank(message = "photo is mandatory")
    private String photo;
}
