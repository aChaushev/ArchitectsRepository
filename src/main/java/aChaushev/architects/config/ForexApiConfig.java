package aChaushev.architects.config;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "forex.api")
public class ForexApiConfig {


    private String key;

    private String url;

    private String base;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    @PostConstruct
    public void checkConfiguration() throws IllegalAccessException {
        verifyNotNullOrEmpty(key, "key");
        verifyNotNullOrEmpty(url, "url");
        verifyNotNullOrEmpty(base, "base");

        if (!"USD".equals(base)) {
            throw new IllegalAccessException("Sorry, but the free API does not support base currencies," +
                    " different than USD");
        }

    }

    private static void verifyNotNullOrEmpty(String name, String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Property " + name + " can not be empty");
        }
    }
}
