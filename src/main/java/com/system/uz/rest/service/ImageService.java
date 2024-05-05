package com.system.uz.rest.service;

import com.system.uz.env.MinioServiceEnv;
import com.system.uz.exceptions.BadRequestException;
import com.system.uz.exceptions.NotFoundException;
import com.system.uz.global.MessageKey;
import com.system.uz.global.Utils;
import com.system.uz.rest.domain.Image;
import com.system.uz.rest.repository.ImageRepository;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import jdk.jshell.execution.Util;
import lombok.AllArgsConstructor;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;
    private MinioClient minioClient;
    private MinioServiceEnv minioServiceEnv;

    public ResponseEntity<Resource> downloadFile(String photoId) {

        if(!Utils.isValidString(photoId)){
            throw new BadRequestException(MessageKey.MINIO_FAILED);
        }

        Optional<Image> optionalImage = imageRepository.findByImageId(photoId);
        if (optionalImage.isEmpty())
            throw new NotFoundException(MessageKey.IMAGE_NOT_FOUND);
        Image image = optionalImage.get();

        byte[] content = getFileFromMinio(minioServiceEnv.getBucket(), image.getFullPath());
        if (Objects.isNull(content))
            throw new NotFoundException(MessageKey.IMAGE_NOT_FOUND);

        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + image.getImageOriginalName());
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");

        return ResponseEntity.ok()
                .headers(header)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new ByteArrayResource(content, image.getImageOriginalName()));
    }

    private byte[] getFileFromMinio(String bucketName, String fullPath) {
        try {
            InputStream obj = minioClient.getObject(GetObjectArgs.builder().bucket(bucketName).object(fullPath).build());
            byte[] content = IOUtils.toByteArray(obj);
            obj.close();
            return content;
        } catch (Exception e) {
            throw new BadRequestException(MessageKey.MINIO_FAILED);
        }
    }

}
