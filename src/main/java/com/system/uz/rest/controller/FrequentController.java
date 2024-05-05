package com.system.uz.rest.controller;

import com.system.uz.enums.InfoType;
import com.system.uz.rest.model.frequent.FrequentShortWhiteRes;
import com.system.uz.rest.service.FrequentInfoService;
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

    @GetMapping(BY_ID)
    public ResponseEntity<FrequentShortWhiteRes> getById(@RequestParam(value = "frequentId") String frequentId) {
        return frequentInfoService.getWhiteById(frequentId);
    }

    @GetMapping(LIST)
    public ResponseEntity<List<FrequentShortWhiteRes>> getList(@RequestParam(value = "type") InfoType type) {
        return frequentInfoService.getWhiteList(type);
    }

}
