package com.volodya262.pazetestassignment.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ClientsConfig {
    public ClientsConfig(
            @Autowired ObjectMapper objectMapper,
            @Value("${client.max-total-connections}") int maxTotalConnections,
            @Value("${client.default-max-per-route}") int defaultMaxPerRoute
    ) {
        this.objectMapper = objectMapper;
        this.maxTotalConnections = maxTotalConnections;
        this.defaultMaxPerRoute = defaultMaxPerRoute;
    }

    private final ObjectMapper objectMapper;
    private final int maxTotalConnections;
    private final int defaultMaxPerRoute;

    @Bean
    public RestTemplate pazeRestTemplate() {
        var poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager();
        poolingHttpClientConnectionManager.setMaxTotal(maxTotalConnections);
        poolingHttpClientConnectionManager.setDefaultMaxPerRoute(defaultMaxPerRoute);

        var httpClient = HttpClientBuilder.create()
                .setConnectionManager(poolingHttpClientConnectionManager)
                .build();

        return new RestTemplateBuilder()
                .setConnectTimeout(Duration.ofMillis(5000))
                .setReadTimeout(Duration.ofMillis(5000))
                .additionalMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                .requestFactory(() -> new HttpComponentsClientHttpRequestFactory(httpClient))
                .build();
    }
}
