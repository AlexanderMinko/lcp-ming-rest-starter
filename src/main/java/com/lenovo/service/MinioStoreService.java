package com.lenovo.service;

import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Optional;

import com.lenovo.configuration.MinioProperties;
import com.lenovo.model.FileInfoDto;
import com.lenovo.model.FileUri;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.MinioException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.tika.Tika;
import org.apache.tika.io.TikaInputStream;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
public class MinioStoreService {

  private final MinioClient minioClient;
  private final MinioProperties properties;
  private final Tika tika = new Tika();

  public FileInfoDto putObjectToStorage(
      InputStream data,
      Long size,
      FileUri fileUri) {
    try {
      var filePath = fileUri.buildFilePath();
      var fullPackageName = fileUri.getFullPackageName();
      var bufferedInputStream = new BufferedInputStream(data);
      var contentType = tika.detect(TikaInputStream.get(bufferedInputStream), filePath);
      var fileStoreDto = new FileInfoDto();

      var headers = new HashMap<String, String>();
      if (StringUtils.isNotBlank(fullPackageName)) {
        headers.put(CONTENT_DISPOSITION, "attachment; fileName=\"" + fullPackageName + "\"");
      }

      var objectSize = Optional.ofNullable(size).orElse(-1L);
      var putObjectArgs = PutObjectArgs.builder()
          .bucket(properties.getBucketName())
          .object(filePath)
          .stream(bufferedInputStream, objectSize, properties.getMinPartSize())
          .headers(headers)
          .contentType(contentType)
          .build();
      minioClient.putObject(putObjectArgs);
      fileStoreDto.setDownloadUri("/" + filePath);
      fileStoreDto.setFileSize(size);
      fileStoreDto.setContentType(contentType);
      return fileStoreDto;
    } catch (MinioException | GeneralSecurityException | IOException e) {
      throw new RuntimeException(e);
    }
  }
}
