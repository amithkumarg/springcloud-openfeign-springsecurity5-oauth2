package com.amithkumarg.samples.client.feign.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.net.URL;
import java.util.function.Function;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LibTestUtil {
  public static final Function<String, URL> RESOURCE_URL =
      (String path) -> LibTestUtil.class.getResource(path);

  public static final String AUTH_URL = "/oauth/token";

  public static final String EMPLOYEE_URL_PATTERN = "/employee/.*";

  public static final ObjectMapper OBJECT_MAPPER =
      new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
}
