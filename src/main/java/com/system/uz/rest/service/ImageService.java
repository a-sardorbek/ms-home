package com.system.uz.rest.service;

import com.system.uz.enums.BucketFolder;
import com.system.uz.enums.ImageType;
import com.system.uz.env.MinioServiceEnv;
import com.system.uz.exceptions.BadRequestException;
import com.system.uz.exceptions.NotFoundException;
import com.system.uz.global.MessageKey;
import com.system.uz.global.Utils;
import com.system.uz.rest.domain.Image;
import com.system.uz.rest.repository.ImageRepository;
import com.system.uz.rest.service.component.HashService;
import com.system.uz.rest.service.component.MinioService;
import io.minio.*;
import jdk.jshell.execution.Util;
import lombok.AllArgsConstructor;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;
    private MinioClient minioClient;
    private MinioServiceEnv minioServiceEnv;
    private MinioService minioService;



    public void uploadFile(MultipartFile file) {
        if (file.isEmpty())
            throw new BadRequestException(MessageKey.FILE_CANNOT_BE_EMPTY);

        String contentType = file.getContentType();
        String originalName = file.getOriginalFilename();

        if (originalName == null || contentType == null || !contentType.equals("application/pdf")) {
            throw new BadRequestException(MessageKey.FILE_NOT_SUPPORTED);
        }

        String[] split = originalName.split("\\.");
        String name = originalName + "." + split[split.length - 1];

        Image image;
        Optional<Image> optionalImage = imageRepository.findByType(ImageType.FILE);
        if(optionalImage.isPresent()){
            image = optionalImage.get();
            minioService.deleteImage(image.getFullPath());
            image.setDeletedAt(LocalDateTime.now());
            imageRepository.save(image);
        }

        try {
            String minioBucket = minioServiceEnv.getBucket();
            BucketExistsArgs barg = BucketExistsArgs.builder().bucket(minioBucket).build();
            if (!minioClient.bucketExists(barg)) {
                MakeBucketArgs bnarg = MakeBucketArgs.builder().bucket(minioBucket).build();
                minioClient.makeBucket(bnarg);
            }
            PutObjectArgs uarg = PutObjectArgs.builder()
                    .bucket(minioBucket)
                    .object(BucketFolder.FILE + "/" + name)
                    .contentType(contentType)
                    .stream(file.getInputStream(), file.getInputStream().available(), -1)
                    .build();
            minioClient.putObject(uarg);

            image = new Image();
            image.setImageId(Utils.generateToken());
            image.setType(ImageType.FILE);
            image.setImageOriginalName(originalName);
            image.setContentType(contentType);
            image.setFullPath(BucketFolder.FILE + "/" + name);
            imageRepository.save(image);

        } catch (Exception e) {
            throw new BadRequestException(MessageKey.MINIO_FAILED);
        }
    }


    public ResponseEntity<Resource> downloadFile(String photoId, boolean isPdfFile) {

        Optional<Image> optionalImage;
        if(isPdfFile){
            optionalImage = imageRepository.findByType(ImageType.FILE);
        }else {
            if (!Utils.isValidString(photoId)) {
                throw new BadRequestException(MessageKey.MINIO_FAILED);
            }
            optionalImage = imageRepository.findByImageId(photoId);
        }

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
