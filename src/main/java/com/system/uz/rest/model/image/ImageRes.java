package com.system.uz.rest.model.image;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageRes {
    private List<ProductImage> products;
    private List<PlanImage> plans;
}
