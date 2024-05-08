package com.system.uz.rest.controller;

import com.system.uz.enums.Lang;
import com.system.uz.enums.Status;
import com.system.uz.global.PagingResponse;
import com.system.uz.rest.model.admin.category.CategoryActivationReq;
import com.system.uz.rest.model.admin.category.CategoryCreateReq;
import com.system.uz.rest.model.admin.category.CategoryRes;
import com.system.uz.rest.model.admin.category.CategoryUpdateReq;
import com.system.uz.rest.model.category.CategoryWhiteRes;
import com.system.uz.rest.service.CategoryService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;

import static com.system.uz.base.BaseUri.*;

@RestController
@RequestMapping(API_V1 + CATEGORY)
@AllArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;


    @ApiImplicitParams({
            @ApiImplicitParam(name = "Language", value = "Language", dataTypeClass = Lang.class, paramType = "header", defaultValue = "UZB")
    })
    @GetMapping(BY_ID)
    public ResponseEntity<CategoryWhiteRes> getById(@RequestParam("categoryId") String categoryID) {
        return categoryService.getWhiteById(categoryID);
    }


    @ApiImplicitParams({
            @ApiImplicitParam(name = "Language", value = "Language", dataTypeClass = Lang.class, paramType = "header", defaultValue = "UZB")
    })
    @GetMapping(LIST)
    public ResponseEntity<List<CategoryWhiteRes>> getList() {
        return categoryService.getWhiteList();
    }



}
