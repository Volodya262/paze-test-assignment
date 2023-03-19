package com.volodya262.pazetestassignment.domain.paymentevent;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.ToString;
import org.joda.money.Money;

@ToString
public final class PaymentCreated extends PaymentEvent {
    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public PaymentCreated(
            @JsonProperty("eventNumber") int eventNumber,
            @JsonProperty("createdAt") OffsetDateTime createdAt,
            @JsonProperty("amount") Money amount,
            @JsonProperty("description") String description
    ) {
        super(eventNumber, createdAt);
        this.amount = amount;
        this.description = description;
    }

    @Getter
    private final Money amount;

    @Getter
    private final String description;
}
