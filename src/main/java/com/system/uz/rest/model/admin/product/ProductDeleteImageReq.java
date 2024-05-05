package com.system.uz.rest.model.admin.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDeleteImageReq {
    @NotBlank(message = "product id is mandatory")
    private String productId;

    @NotBlank(message = "photo Id is mandatory")
    private String photoId;
}
