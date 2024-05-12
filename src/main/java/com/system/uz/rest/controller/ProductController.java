package com.system.uz.rest.controller;

import static com.system.uz.base.BaseUri.*;

import com.system.uz.enums.Lang;
import com.system.uz.enums.Status;
import com.system.uz.global.PagingResponse;
import com.system.uz.rest.model.admin.category.CategoryRes;
import com.system.uz.rest.model.product.ProductWhiteRes;
import com.system.uz.rest.model.product.ProductWhiteShortRes;
import com.system.uz.rest.service.ProductService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(API_V1 + PRODUCT)
@AllArgsConstructor
public class ProductController {

    private final ProductService productService;


    @ApiImplicitParams({
            @ApiImplicitParam(name = "Accept-Language", value = "Accept-Language", dataTypeClass = Lang.class, paramType = "header", defaultValue = "UZB")
    })
    @GetMapping(BY_ID)
    public ResponseEntity<ProductWhiteRes> getById(@RequestParam(value = "productId") String productId) {
        return productService.getWhiteById(productId);
    }


    @ApiImplicitParams({
            @ApiImplicitParam(name = "Accept-Language", value = "Accept-Language", dataTypeClass = Lang.class, paramType = "header", defaultValue = "UZB")
    })
    @GetMapping(LIST)
    public ResponseEntity<List<ProductWhiteShortRes>> getList(@RequestParam(value = "categoryId", required = false) String categoryId) {
        return productService.getShortWhiteList(categoryId);
    }
}
