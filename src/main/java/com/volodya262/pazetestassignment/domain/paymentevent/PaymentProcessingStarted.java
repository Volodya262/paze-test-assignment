package com.volodya262.pazetestassignment.domain.paymentevent;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.time.OffsetDateTime;
import lombok.Getter;

@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, property = "class")
public final class PaymentProcessingStarted extends PaymentEvent {
    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public PaymentProcessingStarted(
            @JsonProperty("eventNumber") int eventNumber,
            @JsonProperty("createdAt") OffsetDateTime createdAt,
            @JsonProperty("providerPaymentId") String providerPaymentId,
            @JsonProperty("providerRedirectUrl") String providerRedirectUrl
    ) {
        super(eventNumber, createdAt);
        this.providerPaymentId = providerPaymentId;
        this.providerRedirectUrl = providerRedirectUrl;
    }

    @Getter
    private final String providerPaymentId;

    @Getter
    private final String providerRedirectUrl;
}
