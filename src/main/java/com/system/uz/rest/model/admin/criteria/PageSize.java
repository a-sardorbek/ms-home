package com.system.uz.rest.model.admin.criteria;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageSize {
    private Integer page;
    private Integer size;
}
