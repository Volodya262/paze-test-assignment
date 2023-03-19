package com.volodya262.pazetestassignment.domain.paymentevent;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.ToString;

@ToString
@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, property = "class")
public abstract sealed class PaymentEvent permits
        PaymentCreated,
        PaymentCompleted,
        PaymentProcessingStarted,
        PaymentFailed {
    public PaymentEvent(
            @JsonProperty("eventNumber") int eventNumber,
            @JsonProperty("createdAt") OffsetDateTime createdAt
    ) {
        this.eventNumber = eventNumber;
        this.createdAt = createdAt;
    }

    @Getter
    private final int eventNumber;

    @Getter
    private final OffsetDateTime createdAt;
}
