package com.lenovo.service;

import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;
import static org.springframework.http.HttpHeaders.CONTENT_LENGTH;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Optional;

import com.lenovo.configuration.MinioProperties;
import com.lenovo.model.BucketSecurityPolicy;
import com.lenovo.model.FileInfoDto;
import com.lenovo.model.FileUri;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.MinioException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.tika.Tika;
import org.apache.tika.io.TikaInputStream;

@RequiredArgsConstructor
public class MinioStoreService {

  private final MinioClient minioClient;
  private final MinioProperties properties;
  private final Tika tika = new Tika();

  public FileInfoDto putObjectToPublicStorage(
      InputStream data,
      Long size,
      FileUri fileUri) {
    return putObjectToStorage(data, size, fileUri, BucketSecurityPolicy.PUBLIC);
  }

  public FileInfoDto putObjectToPrivateStorage(
      InputStream data,
      Long size,
      FileUri fileUri) {
    return putObjectToStorage(data, size, fileUri, BucketSecurityPolicy.PRIVATE);
  }

  private FileInfoDto putObjectToStorage(
      InputStream data,
      Long size,
      FileUri fileUri,
      BucketSecurityPolicy policy) {
    try {
      var bucketName = policy == BucketSecurityPolicy.PUBLIC
          ? properties.getPublicBucketName()
          : properties.getPrivateBucketName();
      var bufferedInputStream = new BufferedInputStream(data);
      var filePath = fileUri.buildFilePath();
      var contentType = tika.detect(TikaInputStream.get(bufferedInputStream), filePath);
      var objectSize = Optional.ofNullable(size).orElse(-1L);

      HashMap<String, String> headers = new HashMap<>();
      if (StringUtils.isNotBlank(filePath)) {
        headers.put(CONTENT_DISPOSITION, "attachment; fileName=\"" + filePath + "\"");
        headers.put(CONTENT_TYPE, contentType);
        headers.put(CONTENT_LENGTH, String.valueOf(objectSize));
      }

      var fileInfoDto = new FileInfoDto();
      var putObjectArgs = PutObjectArgs.builder()
          .bucket(bucketName)
          .object(filePath)
          .stream(bufferedInputStream, objectSize, properties.getMinPartSize())
          .headers(headers)
          .contentType(contentType)
          .build();
      minioClient.putObject(putObjectArgs);
      fileInfoDto.setDownloadUri("/" + filePath);
      fileInfoDto.setFileSize(size);
      fileInfoDto.setContentType(contentType);
      return fileInfoDto;
    } catch (MinioException | GeneralSecurityException | IOException e) {
      throw new RuntimeException(e);
    }
  }
}
