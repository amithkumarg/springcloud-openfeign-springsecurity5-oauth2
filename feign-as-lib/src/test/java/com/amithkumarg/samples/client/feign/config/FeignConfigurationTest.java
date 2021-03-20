package com.amithkumarg.samples.client.feign.config;

import feign.Client;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import uk.org.webcompere.systemstubs.environment.EnvironmentVariables;
import uk.org.webcompere.systemstubs.jupiter.SystemStub;
import uk.org.webcompere.systemstubs.jupiter.SystemStubsExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SystemStubsExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FeignConfigurationTest {

  @SystemStub private EnvironmentVariables environment;

  @Test
  void testFeignClientTypeForProxy() {
    // ARRANGE
    environment.set(FeignConfiguration.HTTP_PROXY, "http://fake.proxy.com:80");

    // ACT
    final FeignConfiguration subject = new FeignConfiguration();

    // ASSERT
    assertTrue(subject.defaultFeignClient() instanceof Client.Proxied);
  }

  @Test
  void testFeignClientTypeForNoProxy() {
    // ARRANGE
    environment.set(FeignConfiguration.HTTP_PROXY, null);

    // ACT
    final FeignConfiguration subject = new FeignConfiguration();

    // ASSERT
    assertTrue(subject.defaultFeignClient() instanceof Client.Default);
  }
}
