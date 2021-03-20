package com.amithkumarg.samples.client.feign;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;

@EnableFeignClients
@ComponentScan
@Configuration
public class ClientConfiguration {

  /**
   * creates AuthManager in spring context for OAuth token management in InMemory cache.
   *
   * @param clientRegistrationRepository - repo to retrieve auto configured registrations in spring
   *     context.
   * @param authorizedClientService - service to fetch & refresh auth token in memory.
   * @return AuthorizedClientManager
   */
  @Bean
  public OAuth2AuthorizedClientManager authorizedClientManager(
      final ClientRegistrationRepository clientRegistrationRepository,
      final OAuth2AuthorizedClientService authorizedClientService) {
    return new AuthorizedClientServiceOAuth2AuthorizedClientManager(
        clientRegistrationRepository, authorizedClientService);
  }
}
