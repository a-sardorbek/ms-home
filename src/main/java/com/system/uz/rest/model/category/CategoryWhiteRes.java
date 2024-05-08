package com.system.uz.rest.model.category;

import com.system.uz.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryWhiteRes {

    private String categoryId;

    private Status status;

    private String title;

    private String description;

}
