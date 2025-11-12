package ru.pp.gamma.overlord.image.service;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.pp.gamma.overlord.image.exception.CustomMinioException;

import java.io.ByteArrayInputStream;

@Service
public class ImagesMinioService {

    @Value("${minio.bucket}")
    private String bucket;

    private final MinioClient minioClient;

    public ImagesMinioService(@Qualifier("imagesMinioClient") MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    public void put(String name, String contentType, byte[] image) {
        try {
            minioClient.putObject(PutObjectArgs.builder()
                    .object(name)
                    .contentType(contentType)
                    .stream(new ByteArrayInputStream(image), image.length, -1)
                    .bucket(bucket)
                    .build());
        } catch (Exception e) {
            throw new CustomMinioException(e);
        }
    }
}
