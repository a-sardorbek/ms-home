package com.system.uz.rest.controller.admin;

import com.system.uz.rest.service.ImageService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.system.uz.base.BaseUri.*;

@RestController
@RequestMapping(API_V1 + ADMIN + FILE)
@AllArgsConstructor
public class AdminFileController {

    private final ImageService imageService;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Token", required = true, paramType = "header", dataTypeClass = String.class),
    })
    @PreAuthorize("hasAuthority(T(com.system.uz.enums.Permission).SYS_ADMIN_MODERN_HOUSE)")
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> upload(@RequestPart MultipartFile file) {
        imageService.uploadFile(file);
        return ResponseEntity.noContent().build();
    }

}
