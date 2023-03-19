package com.volodya262.pazetestassignment.application.clients;

import com.volodya262.pazetestassignment.application.clients.dtos.PazeGetPaymentResponse;
import com.volodya262.pazetestassignment.application.clients.dtos.PazePaymentCreateRequest;
import com.volodya262.pazetestassignment.application.clients.dtos.PazePaymentCreateResponse;
import com.volodya262.pazetestassignment.domain.providerpayment.CreatedProviderPayment;
import com.volodya262.pazetestassignment.domain.providerpayment.ProviderPayment;
import java.util.Optional;
import lombok.NonNull;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class PazeClient {

    public PazeClient(
            RestTemplate pazeRestTemplate,
            @Value("${client.paze.url}") String baseUrl,
            @Value("${client.paze.bearer-token}") String bearerToken
    ) {
        this.pazeRestTemplate = pazeRestTemplate;
        this.baseUrl = baseUrl;
        this.headers = createHeaders(bearerToken);
    }

    private final RestTemplate pazeRestTemplate;

    private final String baseUrl;

    private final HttpHeaders headers;

    private static HttpHeaders createHeaders(String bearerToken) {
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + bearerToken);

        return headers;
    }

    public CreatedProviderPayment createPayment(
            @NonNull String referenceId,
            @NonNull Money amount,
            @NonNull String returnUrl
    ) {
        var url = String.format("%s/payments", baseUrl);
        var body = new PazePaymentCreateRequest(
                "DEPOSIT",
                amount.getAmount(),
                amount.getCurrencyUnit().toString(),
                referenceId,
                returnUrl
        );

        var entity = new HttpEntity<>(body, headers);
        var response = pazeRestTemplate.exchange(url, HttpMethod.POST, entity, PazePaymentCreateResponse.class);

        return Optional.ofNullable(response.getBody())
                .orElseThrow()
                .getProviderPayment();
    }

    public ProviderPayment getPayment(@NonNull String providerPaymentId) {
        var url = String.format("%s/payments/%s", baseUrl, providerPaymentId);
        var entity = new HttpEntity(headers);
        var response = pazeRestTemplate.exchange(url, HttpMethod.GET, entity, PazeGetPaymentResponse.class);

        return Optional.ofNullable(response.getBody())
                .orElseThrow()
                .getProviderPayment();
    }
}


