package com.system.uz.rest.controller;

import com.system.uz.enums.Lang;
import com.system.uz.rest.model.blog.BlogWhiteShortRes;
import com.system.uz.rest.service.BlogService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.system.uz.base.BaseUri.*;

@RestController
@RequestMapping(API_V1 + BLOG)
@AllArgsConstructor
public class BlogController {

    private final BlogService blogService;


    @ApiImplicitParams({
            @ApiImplicitParam(name = "Language", value = "Language", dataTypeClass = Lang.class, paramType = "header", defaultValue = "UZB")
    })
    @GetMapping(BY_ID)
    public ResponseEntity<BlogWhiteShortRes> getById(@RequestParam(value = "blogId") String blogId) {
        return blogService.getWhiteById(blogId);
    }


    @ApiImplicitParams({
            @ApiImplicitParam(name = "Language", value = "Language", dataTypeClass = Lang.class, paramType = "header", defaultValue = "UZB")
    })
    @GetMapping(LIST)
    public ResponseEntity<List<BlogWhiteShortRes>> getList() {
        return blogService.getWhiteList();
    }

}
