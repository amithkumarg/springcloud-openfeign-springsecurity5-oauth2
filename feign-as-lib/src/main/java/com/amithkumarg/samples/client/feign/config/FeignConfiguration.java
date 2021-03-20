package com.amithkumarg.samples.client.feign.config;

import feign.Client;
import feign.Logger;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetSocketAddress;
import java.net.Proxy;

@Configuration
public class FeignConfiguration {

  private final String proxyHost;
  private final int proxyPort;
  private final boolean useProxy;
  private static final String HTTP = "http://";
  private static final String COLON = ":";
  public static final String HTTP_PROXY = "HTTP_PROXY";

  public FeignConfiguration() {
    final String proxyUrl = System.getenv(HTTP_PROXY);
    if (StringUtils.isBlank(proxyUrl)) {
      useProxy = false;
      this.proxyHost = StringUtils.EMPTY;
      this.proxyPort = -1;
    } else {
      useProxy = true;
      final String[] proxyConfig = proxyUrl.replace(HTTP, StringUtils.EMPTY).split(COLON);
      this.proxyHost = proxyConfig[0];
      this.proxyPort = Integer.parseInt(proxyConfig[1]);
    }
  }

  @Bean
  public Client defaultFeignClient() {
    return useProxy
        ? new Client.Proxied(
            null, null, new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort)))
        : new Client.Default(null, null);
  }

  @Bean
  public Logger.Level feignLoggerLevel() {
    return Logger.Level.FULL;
  }
}
