package com.system.uz.rest.model.admin.category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryActivationReq {
    @NotBlank(message = "category id is mandatory")
    private String categoryId;

    private boolean isActive = true;
}
