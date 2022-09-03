package com.example.application.data.generator;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.FileCopyUtils;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Utils {

  public static String readContent(String resourcePath) {
    if (resourcePath == null) {
      return null;
    }
    Resource resource = new ClassPathResource("sample-data/" + resourcePath);
    try (InputStream inputStream = resource.getInputStream()) {
      byte[] bdata = FileCopyUtils.copyToByteArray(inputStream);
      return new String(bdata, StandardCharsets.UTF_8);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

  }
}
