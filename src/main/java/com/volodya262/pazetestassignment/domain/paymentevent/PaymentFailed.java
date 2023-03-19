package com.volodya262.pazetestassignment.domain.paymentevent;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

public final class PaymentFailed extends PaymentEvent {
    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public PaymentFailed(
            @JsonProperty("eventNumber") int eventNumber,
            @JsonProperty("createdAt") OffsetDateTime createdAt
    ) {
        super(eventNumber, createdAt);
    }
}
