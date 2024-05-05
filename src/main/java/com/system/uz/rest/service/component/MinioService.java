package com.system.uz.rest.service.component;

import com.system.uz.enums.BucketFolder;
import com.system.uz.env.MinioServiceEnv;
import com.system.uz.exceptions.BadRequestException;
import com.system.uz.global.MessageKey;
import com.system.uz.rest.model.image.ImageInfo;
import io.minio.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@AllArgsConstructor
public class MinioService {

    private final MinioServiceEnv minioServiceEnv;
    private final MinioClient minioClient;

    public void deleteImage(String path) {
        String minioBucket = minioServiceEnv.getBucket();
        try {
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(minioBucket)
                    .object(path)
                    .build());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public ImageInfo uploadImage(String newImage, BucketFolder bucketFolder) {
        try {
            String[] splitImage = newImage.split("\\,");
            String regex = ":(.*);";
            String contentType = "";

            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(splitImage[0]);

            if (matcher.find()) {
                contentType = matcher.group(1);
            }

            byte[] decodedBytes = Base64.getDecoder().decode(splitImage[1]);

            String type = validateContentType(contentType);
            String name = HashService.generateFileName() + type;

            String minioBucket = minioServiceEnv.getBucket();
            BucketExistsArgs barg = BucketExistsArgs.builder().bucket(minioBucket).build();

            if (!minioClient.bucketExists(barg)) {
                MakeBucketArgs bnarg = MakeBucketArgs.builder().bucket(minioBucket).build();
                minioClient.makeBucket(bnarg);
            }

            InputStream inputStream = new ByteArrayInputStream(decodedBytes);
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(minioBucket)
                    .object(bucketFolder + "/" + name)
                    .stream(inputStream, inputStream.available(), -1)
                    .build());

            return new ImageInfo(name, bucketFolder + "/" + name, contentType);
        } catch (Exception e) {
            throw new BadRequestException(MessageKey.MINIO_FAILED);
        }
    }

    private String validateContentType(String contentType) {
        if (StringUtils.hasText(contentType)) {
            switch (contentType) {
                case "image/avif":
                    return ".avif";
                case "image/webp":
                    return ".webp";
                case "image/svg":
                    return ".svg";
                case "image/jpeg":
                    return ".jpg";
                case "image/png":
                    return ".png";
            }
        }
        throw new BadRequestException(MessageKey.FILE_NOT_SUPPORTED);
    }

}
