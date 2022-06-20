package com.lenovo.model;

import static java.util.Optional.ofNullable;

import static org.apache.commons.lang3.StringUtils.defaultIfBlank;
import static org.apache.commons.lang3.StringUtils.defaultString;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileUri {
  private String folderName;
  private String fileName;
  private String fullPackageName;
  private String extension;

  public String buildFilePath() {
    if (StringUtils.isNoneBlank(extension) && !StringUtils.startsWith(extension, ".")) {
      extension = "." + extension;
    }
    return buildUri(constructFolderName(), constructFileName());
  }

  private String constructFolderName() {
    return ofNullable(folderName)
        .filter(StringUtils::isNotBlank)
        .orElse("unknown");
  }

  private String constructFileName() {
    return defaultIfBlank(fileName, UUID.randomUUID().toString()) + defaultString(extension);
  }

  public static String buildUri(String folderName, String fileName) {
    return UriComponentsBuilder.newInstance()
        .path(folderName)
        .path("/")
        .path(fileName)
        .build().toUriString();
  }
}
