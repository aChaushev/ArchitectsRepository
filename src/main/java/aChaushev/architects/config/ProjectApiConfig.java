package aChaushev.architects.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

//for REST API
@Configuration
@ConfigurationProperties(prefix = "projects.api")
public class ProjectApiConfig {
  private String baseUrl;

  public String getBaseUrl() {
    return baseUrl;
  }

  public ProjectApiConfig setBaseUrl(String baseUrl) {
    this.baseUrl = baseUrl;
    return this;
  }
}
