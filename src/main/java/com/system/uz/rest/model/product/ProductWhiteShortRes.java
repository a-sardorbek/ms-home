package com.system.uz.rest.model.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductWhiteShortRes {
    private String productId;
    private String title;
    private Integer size;
    private String photo;
}
