package com.system.uz.global;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PagingParams {

    private Integer pageNumber;
    private Integer pageSize;

    private Integer totalPages;
    private Long totalItems;
}
