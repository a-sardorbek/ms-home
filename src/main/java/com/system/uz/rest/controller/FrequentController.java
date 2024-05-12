package com.system.uz.rest.controller;

import com.system.uz.enums.InfoType;
import com.system.uz.enums.Lang;
import com.system.uz.rest.model.frequent.FrequentShortWhiteRes;
import com.system.uz.rest.service.FrequentInfoService;
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
@RequestMapping(API_V1 + FREQUENT)
@AllArgsConstructor
public class FrequentController {

    private final FrequentInfoService frequentInfoService;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Accept-Language", value = "Accept-Language", dataTypeClass = Lang.class, paramType = "header", defaultValue = "UZB")
    })
    @GetMapping(BY_ID)
    public ResponseEntity<FrequentShortWhiteRes> getById(@RequestParam(value = "frequentId") String frequentId) {
        return frequentInfoService.getWhiteById(frequentId);
    }


    @ApiImplicitParams({
            @ApiImplicitParam(name = "Accept-Language", value = "Accept-Language", dataTypeClass = Lang.class, paramType = "header", defaultValue = "UZB")
    })
    @GetMapping(LIST)
    public ResponseEntity<List<FrequentShortWhiteRes>> getList(@RequestParam(value = "type") InfoType type) {
        return frequentInfoService.getWhiteList(type);
    }

}
