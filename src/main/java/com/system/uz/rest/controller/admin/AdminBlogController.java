package com.system.uz.rest.controller.admin;

import com.system.uz.global.PagingResponse;
import com.system.uz.rest.model.admin.blog.BlogCreateReq;
import com.system.uz.rest.model.admin.blog.BlogRes;
import com.system.uz.rest.model.admin.blog.BlogUpdateReq;
import com.system.uz.rest.service.BlogService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.system.uz.base.BaseUri.*;

@RestController
@RequestMapping(API_V1 + ADMIN + BLOG)
@AllArgsConstructor
public class AdminBlogController {

    private final BlogService blogService;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Token", required = true, paramType = "header", dataTypeClass = String.class),
    })
    @PreAuthorize("hasAuthority(T(com.system.uz.enums.Permission).SYS_ADMIN_MODERN_HOUSE)")
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody BlogCreateReq req) {
        blogService.create(req);
        return ResponseEntity.noContent().build();
    }


    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Token", required = true, paramType = "header", dataTypeClass = String.class),
    })
    @PreAuthorize("hasAuthority(T(com.system.uz.enums.Permission).SYS_ADMIN_MODERN_HOUSE)")
    @PutMapping
    public ResponseEntity<?> update(@Valid @RequestBody BlogUpdateReq req) {
        blogService.update(req);
        return ResponseEntity.noContent().build();
    }


    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Token", required = true, paramType = "header", dataTypeClass = String.class),
    })
    @PreAuthorize("hasAuthority(T(com.system.uz.enums.Permission).SYS_ADMIN_MODERN_HOUSE)")
    @GetMapping(BY_ID)
    public ResponseEntity<BlogRes> getById(@RequestParam("blogId") String blogId) {
        return blogService.getById(blogId);
    }


    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Token", required = true, paramType = "header", dataTypeClass = String.class),
    })
    @PreAuthorize("hasAuthority(T(com.system.uz.enums.Permission).SYS_ADMIN_MODERN_HOUSE)")
    @GetMapping(LIST)
    public ResponseEntity<PagingResponse<BlogRes>> getList(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size) {
        return blogService.getList(page, size);
    }


    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Token", required = true, paramType = "header", dataTypeClass = String.class),
    })
    @PreAuthorize("hasAuthority(T(com.system.uz.enums.Permission).SYS_ADMIN_MODERN_HOUSE)")
    @DeleteMapping
    public ResponseEntity<?> delete(@RequestParam("blogId") String blogId) {
        blogService.delete(blogId);
        return ResponseEntity.noContent().build();
    }


}
