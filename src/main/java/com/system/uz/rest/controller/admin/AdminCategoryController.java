package com.system.uz.rest.controller.admin;

import static com.system.uz.base.BaseUri.*;

import com.system.uz.enums.Status;
import com.system.uz.global.PagingResponse;
import com.system.uz.rest.model.admin.category.CategoryActivationReq;
import com.system.uz.rest.model.admin.category.CategoryCreateReq;
import com.system.uz.rest.model.admin.category.CategoryRes;
import com.system.uz.rest.model.admin.category.CategoryUpdateReq;
import com.system.uz.rest.service.CategoryService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(API_V1 + ADMIN + CATEGORY)
@AllArgsConstructor
public class AdminCategoryController {

    private final CategoryService categoryService;


    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Token", required = true, paramType = "header", dataTypeClass = String.class),
    })
    @PreAuthorize("hasAuthority(T(com.system.uz.enums.Permission).SYS_ADMIN_MODERN_HOUSE)")
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CategoryCreateReq req) {
        categoryService.create(req);
        return ResponseEntity.noContent().build();
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Token", required = true, paramType = "header", dataTypeClass = String.class),
    })
    @PreAuthorize("hasAuthority(T(com.system.uz.enums.Permission).SYS_ADMIN_MODERN_HOUSE)")
    @PutMapping
    public ResponseEntity<?> update(@Valid @RequestBody CategoryUpdateReq req) {
        categoryService.update(req);
        return ResponseEntity.noContent().build();
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Token", required = true, paramType = "header", dataTypeClass = String.class),
    })
    @PreAuthorize("hasAuthority(T(com.system.uz.enums.Permission).SYS_ADMIN_MODERN_HOUSE)")
    @PutMapping(ACTIVATION)
    public ResponseEntity<?> activateDeactivate(@Valid @RequestBody CategoryActivationReq req) {
        categoryService.activateDeactivate(req);
        return ResponseEntity.noContent().build();
    }


    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Token", required = true, paramType = "header", dataTypeClass = String.class),
    })
    @PreAuthorize("hasAuthority(T(com.system.uz.enums.Permission).SYS_ADMIN_MODERN_HOUSE)")
    @GetMapping(BY_ID)
    public ResponseEntity<CategoryRes> getById(@RequestParam("categoryId") String categoryID) {
        return categoryService.getById(categoryID);
    }


    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Token", required = true, paramType = "header", dataTypeClass = String.class),
    })
    @PreAuthorize("hasAuthority(T(com.system.uz.enums.Permission).SYS_ADMIN_MODERN_HOUSE)")
    @GetMapping(LIST)
    public ResponseEntity<PagingResponse<CategoryRes>> getList(
            @RequestParam(value = "status", required = false) Status status,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size) {
        return categoryService.getList(status, page, size);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Token", required = true, paramType = "header", dataTypeClass = String.class),
    })
    @PreAuthorize("hasAuthority(T(com.system.uz.enums.Permission).SYS_ADMIN_MODERN_HOUSE)")
    @DeleteMapping
    public ResponseEntity<?> delete(@RequestParam("categoryId") String categoryID) {
        categoryService.delete(categoryID);
        return ResponseEntity.noContent().build();
    }


}
