package com.system.uz.rest.controller;

import static com.system.uz.base.BaseUri.*;

import com.system.uz.enums.Status;
import com.system.uz.global.PagingResponse;
import com.system.uz.rest.model.admin.category.CategoryRes;
import com.system.uz.rest.model.product.ProductWhiteRes;
import com.system.uz.rest.model.product.ProductWhiteShortRes;
import com.system.uz.rest.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(API_V1 + PRODUCT)
@AllArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping(BY_ID)
    public ResponseEntity<ProductWhiteRes> getById(@RequestParam(value = "productId") String productId) {
        return productService.getWhiteById(productId);
    }

    @GetMapping(LIST)
    public ResponseEntity<List<ProductWhiteShortRes>> getList(@RequestParam(value = "categoryId", required = false) String categoryId) {
        return productService.getShortWhiteList(categoryId);
    }
}
