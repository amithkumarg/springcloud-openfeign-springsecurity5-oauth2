package com.amithkumarg.samples.client.feign.utils;

import com.nimbusds.oauth2.sdk.ParseException;
import com.nimbusds.oauth2.sdk.TokenResponse;
import com.nimbusds.oauth2.sdk.token.AccessToken;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.function.Function;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class DataConstants {
  private static final Function<String, String> WM_RELATIVE_PATH =
      (String path) -> String.join("/", "mocks", path);

  public static final String AUTH_RESP_WM_RELATIVE_PATH =
      WM_RELATIVE_PATH.apply("auth_response.json");

  public static final String AUTH_RESP_SHORT_WM_RELATIVE_PATH =
      WM_RELATIVE_PATH.apply("auth_response_shortlife.json");

  private static final Function<String, String> ABS_PATH =
      (String path) -> String.join("/", "/__files/mocks", path);

  public static final OAuth2AccessToken AUTH_RESPONSE;

  static {
    OAuth2AccessToken authResponse = null;
    try {
      // load mock jsons
      authResponse =
          converter(
              LibTestUtil.OBJECT_MAPPER.readValue(
                  LibTestUtil.RESOURCE_URL.apply(ABS_PATH.apply("auth_response.json")), Map.class));
    } catch (IOException | ParseException e) {
      log.error(
          "Exception occurred in loading data constant objects from json, tests will fail", e);
    }
    AUTH_RESPONSE = authResponse;
  }

  private static OAuth2AccessToken converter(final Map<String, Object> jsonMap)
      throws ParseException {
    final AccessToken accessToken =
        TokenResponse.parse(new JSONObject(jsonMap))
            .toSuccessResponse()
            .getTokens()
            .getAccessToken();
    return OAuth2AccessTokenResponse.withToken(accessToken.getValue())
        .tokenType(OAuth2AccessToken.TokenType.BEARER)
        .expiresIn(accessToken.getLifetime())
        .scopes(new HashSet<>(accessToken.getScope().toStringList()))
        .build()
        .getAccessToken();
  }
}
