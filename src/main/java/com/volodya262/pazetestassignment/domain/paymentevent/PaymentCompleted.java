package com.volodya262.pazetestassignment.domain.paymentevent;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import lombok.ToString;

@ToString
public final class PaymentCompleted extends PaymentEvent {
    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public PaymentCompleted(
            @JsonProperty("eventNumber") int eventNumber,
            @JsonProperty("createdAt") OffsetDateTime createdAt
    ) {
        super(eventNumber, createdAt);
    }
}
