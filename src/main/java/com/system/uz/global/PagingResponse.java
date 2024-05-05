package com.system.uz.global;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PagingResponse<T> {

    private List<T> content;

    private PagingParams paging;

    public PagingResponse<T> getDefaultResponse(Integer page, Integer size) {
        PagingParams pagingParams = new PagingParams(page, size, 0, 0L);
        return new PagingResponse<>(new ArrayList<>(), pagingParams);
    }

    public void setPagingParams(PagingResponse<T> response, Integer page, Integer size, Long totalElements) {
        PagingParams pagingParams = new PagingParams();
        pagingParams.setPageNumber(page);
        pagingParams.setPageSize(size);
        pagingParams.setTotalPages((int) Math.ceil(totalElements.doubleValue() / size));
        pagingParams.setTotalItems(totalElements);
        response.setPaging(pagingParams);
    }

}
