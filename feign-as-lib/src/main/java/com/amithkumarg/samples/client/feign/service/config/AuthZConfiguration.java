package com.amithkumarg.samples.client.feign.service.config;

import com.amithkumarg.samples.client.feign.utils.LibConstants;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class AuthZConfiguration {

  private final OAuth2AuthorizedClientManager authorizedClientManager;

  @Bean
  public AuthZInteceptor authZInterceptor() {
    return new AuthZInteceptor(authorizedClientManager);
  }

  @RequiredArgsConstructor
  private static class AuthZInteceptor implements RequestInterceptor {

    private final OAuth2AuthorizedClientManager authorizedClientManager;

    @Override
    public void apply(final RequestTemplate requestTemplate) {
      requestTemplate.headers(Map.of(HttpHeaders.AUTHORIZATION, getAuthorizationToken()));
    }

    @SuppressFBWarnings(
        value = "NP_NULL_ON_SOME_PATH_FROM_RETURN_VALUE",
        justification =
            "authorizedClientManager.authorize won't be null as configured authz is supported")
    private List<String> getAuthorizationToken() {
      final OAuth2AuthorizeRequest request =
          OAuth2AuthorizeRequest.withClientRegistrationId(LibConstants.FAKE_EMPLOYEE)
              .principal(SecurityContextHolder.getContext().getAuthentication())
              .build();
      final OAuth2AccessToken accessToken =
          authorizedClientManager.authorize(request).getAccessToken();
      return Arrays.asList(
          String.format(
              "%s %s", accessToken.getTokenType().getValue(), accessToken.getTokenValue()));
    }
  }
}
