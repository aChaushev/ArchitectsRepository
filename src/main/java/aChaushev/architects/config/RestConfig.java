package aChaushev.architects.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

@Configuration
public class RestConfig {

    @Bean("genericRestClient")
    public RestClient restClient() {
        return RestClient.create();
    }

    //for REST API
    @Bean("projectsRestClient")
    public RestClient projectsRestClient(ProjectApiConfig projectApiConfig) {
        return RestClient
                .builder()
                .baseUrl(projectApiConfig.getBaseUrl())
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
