package com.system.uz.rest.model.product;

import com.system.uz.rest.model.image.ImageRes;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductWhiteRes {
    private String productId;
    private String title;
    private Integer size;
    private String planDescription;
    private ImageRes photo;
}
