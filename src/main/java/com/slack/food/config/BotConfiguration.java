package com.slack.food.config;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RestTemplate;

@Configuration
public class BotConfiguration {
  @Value("${kakao.key}")
  private String kakaoKey;

  @Bean(name = "kakaoRestTemplate")
  public RestTemplate kakaoRestTemplate() {
    RestTemplate restTemplate = new RestTemplate();
    restTemplate.getInterceptors().add(new ClientHttpRequestInterceptor() {
      @Override
      public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws
          IOException {
        request.getHeaders().set("Authorization", kakaoKey);
        return execution.execute(request, body);
      }
    });
    return restTemplate;
  }

  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }
}
