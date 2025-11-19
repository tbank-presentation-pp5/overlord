package ru.pp.gamma.overlord.common.util;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Optional;

@Service
public class MinioRepository {

    private final MinioClient minioClient;

    public MinioRepository(@Qualifier("minioClient") MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    public void put(String bucket, String name, String contentType, byte[] object) {
        try {
            minioClient.putObject(PutObjectArgs.builder()
                    .object(name)
                    .contentType(contentType)
                    .stream(new ByteArrayInputStream(object), object.length, -1)
                    .bucket(bucket)
                    .build());
        } catch (Exception e) {
            throw new CustomMinioException(e);
        }
    }

    public Optional<byte[]> get(String bucket, String name) {
        try {
            InputStream stream = minioClient.getObject(GetObjectArgs.builder()
                    .object(name)
                    .bucket(bucket)
                    .build());
            return Optional.of(stream.readAllBytes());
        } catch (Exception e) {
            throw new CustomMinioException(e);
        }
    }
}
