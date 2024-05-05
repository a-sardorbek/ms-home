package com.system.uz.rest.controller.admin;

import com.system.uz.enums.InfoType;
import com.system.uz.global.PagingResponse;
import com.system.uz.rest.model.admin.frequent.FrequentCreateReq;
import com.system.uz.rest.model.admin.frequent.FrequentRes;
import com.system.uz.rest.model.admin.frequent.FrequentUpdateReq;
import com.system.uz.rest.service.FrequentInfoService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.system.uz.base.BaseUri.*;

@RestController
@RequestMapping(API_V1 + ADMIN + FREQUENT)
@AllArgsConstructor
public class AdminFrequentInfoController {

    private final FrequentInfoService frequentInfoService;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Token", required = true, paramType = "header", dataTypeClass = String.class),
    })
    @PreAuthorize("hasAuthority(T(com.system.uz.enums.Permission).SYS_ADMIN_MODERN_HOUSE)")
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody FrequentCreateReq req) {
        frequentInfoService.create(req);
        return ResponseEntity.noContent().build();
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Token", required = true, paramType = "header", dataTypeClass = String.class),
    })
    @PreAuthorize("hasAuthority(T(com.system.uz.enums.Permission).SYS_ADMIN_MODERN_HOUSE)")
    @PutMapping
    public ResponseEntity<?> update(@Valid @RequestBody FrequentUpdateReq req) {
        frequentInfoService.update(req);
        return ResponseEntity.noContent().build();
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Token", required = true, paramType = "header", dataTypeClass = String.class),
    })
    @PreAuthorize("hasAuthority(T(com.system.uz.enums.Permission).SYS_ADMIN_MODERN_HOUSE)")
    @GetMapping(BY_ID)
    public ResponseEntity<FrequentRes> getById(@RequestParam("frequentId") String frequentId) {
        return frequentInfoService.getById(frequentId);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Token", required = true, paramType = "header", dataTypeClass = String.class),
    })
    @PreAuthorize("hasAuthority(T(com.system.uz.enums.Permission).SYS_ADMIN_MODERN_HOUSE)")
    @GetMapping(LIST)
    public ResponseEntity<PagingResponse<FrequentRes>> getList(
            @RequestParam(value = "type", required = false) InfoType type,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size) {
        return frequentInfoService.getList(type, page, size);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Token", required = true, paramType = "header", dataTypeClass = String.class),
    })
    @PreAuthorize("hasAuthority(T(com.system.uz.enums.Permission).SYS_ADMIN_MODERN_HOUSE)")
    @DeleteMapping
    public ResponseEntity<?> delete(@RequestParam("frequentId") String frequentId) {
        frequentInfoService.delete(frequentId);
        return ResponseEntity.noContent().build();
    }


}
