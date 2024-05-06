package com.system.uz.rest.controller;

import com.system.uz.rest.service.ImageService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.system.uz.base.BaseUri.*;

@RestController
@AllArgsConstructor
@RequestMapping(API_V1 + FILE)
public class ImageController {

    private final ImageService imageService;

    @GetMapping
    public ResponseEntity<Resource> downloadFile() {
        return imageService.downloadFile("", true);
    }

    @GetMapping(PHOTO)
    public ResponseEntity<Resource> download(@RequestParam("photoId") String photoId) {
        return imageService.downloadFile(photoId, false);
    }
}
