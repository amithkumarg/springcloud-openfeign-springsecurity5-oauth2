package com.amithkumarg.samples.client.feign.it;

import com.amithkumarg.samples.client.feign.service.FakeEmployeeClient;
import com.amithkumarg.samples.client.feign.utils.DataConstants;
import com.amithkumarg.samples.client.feign.utils.LibTestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.annotation.DirtiesContext;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;

@SpringBootTest
@AutoConfigureWireMock(port = 0)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@WithAnonymousUser
class AuthZIT {

  @Autowired FakeEmployeeClient employeeClient;

  @BeforeEach
  void setup() {
    // ARRANGE
    stubFor(
        get(urlMatching(LibTestUtil.EMPLOYEE_URL_PATTERN))
            .willReturn(
                aResponse()
                    .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)));
  }

  @Test
  void testAuthTokenIsInsertedForSecuredOutboundCall() {
    // ARRANGE
    stubFor(
        post(urlPathEqualTo(LibTestUtil.AUTH_URL))
            .willReturn(
                aResponse()
                    .withBodyFile(DataConstants.AUTH_RESP_WM_RELATIVE_PATH)
                    .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)));

    // ACT
    employeeClient.fetchEmployee("123");

    // ASSERT
    verify(
        getRequestedFor(urlMatching(LibTestUtil.EMPLOYEE_URL_PATTERN))
            .withHeader(
                HttpHeaders.AUTHORIZATION,
                equalTo(
                    String.format(
                        "%s %s",
                        OAuth2AccessToken.TokenType.BEARER.getValue(),
                        DataConstants.AUTH_RESPONSE.getTokenValue()))));
  }

  @Test
  // NOTE: spring secuirty oauth2 clockskew is set to 60secs
  void testAuthTokenIsRetrievedFromCache() {
    // ARRANGE
    stubFor(
        post(urlPathEqualTo(LibTestUtil.AUTH_URL))
            .willReturn(
                aResponse()
                    .withBodyFile(DataConstants.AUTH_RESP_WM_RELATIVE_PATH)
                    .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)));

    // ACT
    // multiple calls
    employeeClient.fetchEmployee("123");
    employeeClient.fetchEmployee("123");
    employeeClient.fetchEmployee("123");

    // ASSERT
    verify(1, postRequestedFor(urlPathEqualTo(LibTestUtil.AUTH_URL)));
  }

  @Test
  // NOTE: spring secuirty oauth2 clockskew is set to 60secs
  void testAuthTokenCacheIsRefreshedWhenExpired() {
    // ARRANGE
    stubFor(
        post(urlPathEqualTo(LibTestUtil.AUTH_URL))
            .willReturn(
                aResponse()
                    .withBodyFile(DataConstants.AUTH_RESP_SHORT_WM_RELATIVE_PATH)
                    .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)));
    // ACT
    // multiple calls
    employeeClient.fetchEmployee("123");
    employeeClient.fetchEmployee("123");
    employeeClient.fetchEmployee("123");

    // ASSERT
    verify(3, postRequestedFor(urlPathEqualTo(LibTestUtil.AUTH_URL)));
  }
}
