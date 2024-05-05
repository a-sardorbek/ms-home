package com.system.uz.rest.controller.admin;

import com.system.uz.global.PagingResponse;
import com.system.uz.rest.model.admin.product.*;
import com.system.uz.rest.service.ProductService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.system.uz.base.BaseUri.*;

@RestController
@RequestMapping(API_V1 + ADMIN + PRODUCT)
@AllArgsConstructor
public class AdminProductController {

    private final ProductService productService;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Token", required = true, paramType = "header", dataTypeClass = String.class),
    })
    @PreAuthorize("hasAuthority(T(com.system.uz.enums.Permission).SYS_ADMIN_MODERN_HOUSE)")
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody ProductCreateReq req) {
        productService.create(req);
        return ResponseEntity.noContent().build();
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Token", required = true, paramType = "header", dataTypeClass = String.class),
    })
    @PreAuthorize("hasAuthority(T(com.system.uz.enums.Permission).SYS_ADMIN_MODERN_HOUSE)")
    @PutMapping
    public ResponseEntity<?> update(@Valid @RequestBody ProductUpdateReq req) {
        productService.update(req);
        return ResponseEntity.noContent().build();
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Token", required = true, paramType = "header", dataTypeClass = String.class),
    })
    @PreAuthorize("hasAuthority(T(com.system.uz.enums.Permission).SYS_ADMIN_MODERN_HOUSE)")
    @PostMapping(ADD_PHOTO)
    public ResponseEntity<?> addPhoto(@Valid @RequestBody ProductAddImageReq req) {
        productService.addImage(req);
        return ResponseEntity.noContent().build();
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Token", required = true, paramType = "header", dataTypeClass = String.class),
    })
    @PreAuthorize("hasAuthority(T(com.system.uz.enums.Permission).SYS_ADMIN_MODERN_HOUSE)")
    @PutMapping(UPDATE_PHOTO)
    public ResponseEntity<?> updatePhoto(@Valid @RequestBody ProductUpdateImageReq req) {
        productService.updateImage(req);
        return ResponseEntity.noContent().build();
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Token", required = true, paramType = "header", dataTypeClass = String.class),
    })
    @PreAuthorize("hasAuthority(T(com.system.uz.enums.Permission).SYS_ADMIN_MODERN_HOUSE)")
    @GetMapping(BY_ID)
    public ResponseEntity<ProductRes> getById(@RequestParam("productId") String productId) {
        return productService.getById(productId);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Token", required = true, paramType = "header", dataTypeClass = String.class),
    })
    @PreAuthorize("hasAuthority(T(com.system.uz.enums.Permission).SYS_ADMIN_MODERN_HOUSE)")
    @GetMapping(LIST)
    public ResponseEntity<PagingResponse<ProductRes>> getList(
            @RequestParam(value = "categoryId", required = false) String categoryId,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size) {
        return productService.getList(categoryId, page, size);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Token", required = true, paramType = "header", dataTypeClass = String.class),
    })
    @PreAuthorize("hasAuthority(T(com.system.uz.enums.Permission).SYS_ADMIN_MODERN_HOUSE)")
    @DeleteMapping
    public ResponseEntity<?> delete(@RequestParam("productId") String productId) {
        productService.delete(productId);
        return ResponseEntity.noContent().build();
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Token", required = true, paramType = "header", dataTypeClass = String.class),
    })
    @PreAuthorize("hasAuthority(T(com.system.uz.enums.Permission).SYS_ADMIN_MODERN_HOUSE)")
    @DeleteMapping(PHOTO)
    public ResponseEntity<?> deletePhoto(@RequestParam("photoId") String photoId,
                                         @RequestParam("productId") String productId) {
        productService.deleteImage(photoId, productId);
        return ResponseEntity.noContent().build();
    }


}
