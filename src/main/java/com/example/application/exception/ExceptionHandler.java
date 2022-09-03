package com.example.application.exception;

import java.util.UUID;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import lombok.Getter;
import lombok.Setter;

@Service
public class ExceptionHandler {

  private final Logger log = LoggerFactory.getLogger(getClass());

  public ProcessedException process(Throwable throwable) {
    Throwable rootCause = throwable;
    while (rootCause.getCause() != null) {
      rootCause = rootCause.getCause();
    }
    ProcessedException processedException = new ProcessedException();
    processedException.setUuid(UUID.randomUUID().toString());
    processedException.setDescription(rootCause.getMessage());
    if (StringUtils.isBlank(processedException.getDescription())) {
      processedException.setDescription(rootCause.getClass().getSimpleName());
    }
    String error = processedException.getUuid() + "\n" + processedException.getDescription();
    log.error(error, throwable);
    return processedException;
  }

  @Getter
  @Setter
  public class ProcessedException {
    private String uuid;
    private String description;
    private String stackTrace;
  }
}
