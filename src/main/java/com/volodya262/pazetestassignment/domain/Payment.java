package com.volodya262.pazetestassignment.domain;

import com.volodya262.pazetestassignment.domain.paymentevent.PaymentCompleted;
import com.volodya262.pazetestassignment.domain.paymentevent.PaymentCreated;
import com.volodya262.pazetestassignment.domain.paymentevent.PaymentEvent;
import com.volodya262.pazetestassignment.domain.paymentevent.PaymentFailed;
import com.volodya262.pazetestassignment.domain.paymentevent.PaymentProcessingStarted;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import lombok.Getter;
import org.joda.money.Money;

public class Payment {

    @Getter
    private final List<PaymentEvent> eventsToStore = new ArrayList<>();

    @Getter
    private final List<PaymentEvent> events = new ArrayList<>();

    public Payment(long paymentId) {
        this.paymentId = paymentId;
    }

    @Getter
    private final long paymentId;

    @Getter
    private String description;

    @Getter
    private Money amount;

    @Getter
    private OffsetDateTime updatedAt;

    @Getter
    private PaymentStatus status;

    @Getter
    private String providerPaymentId;

    @Getter
    private String providerRedirectUrl;

    private int getNextEventNumber() {
        return this.events.size() + this.eventsToStore.size();
    }

    public static Payment fromEvents(long paymentId, List<PaymentEvent> events) {
        var sortedEvents = events.stream().sorted(Comparator.comparingInt(PaymentEvent::getEventNumber)).toList();
        var payment = new Payment(paymentId);
        payment.applyExisting(sortedEvents);
        return payment;
    }

    public static Payment create(long paymentId, Money amount, String description) {
        var payment = new Payment(paymentId);
        payment.applyNew(new PaymentCreated(payment.getNextEventNumber(), OffsetDateTime.now(), amount, description));
        return payment;
    }

    public Payment toProcessingStarted(String providerPaymentId, String providerRedirectUrl) {
        var event = new PaymentProcessingStarted(getNextEventNumber(), OffsetDateTime.now(), providerPaymentId, providerRedirectUrl);
        this.applyNew(event);
        return this;
    }

    public Payment toCompleted() {
        this.applyNew(new PaymentCompleted(getNextEventNumber(), OffsetDateTime.now()));
        return this;
    }

    public Payment toFailed() {
        this.applyNew(new PaymentFailed(getNextEventNumber(), OffsetDateTime.now()));
        return this;
    }

    private void applyNew(PaymentEvent event) {
        eventsToStore.add(event);
        apply(event);
    }

    private void applyExisting(List<PaymentEvent> events) {
        this.events.addAll(events);
        for (var event : events) {
            apply(event);
        }
    }

    private void apply(PaymentEvent event) {
        updatedAt = event.getCreatedAt();

        switch (event) {
            case PaymentCreated paymentCreated -> {
                status = PaymentStatus.created;
                amount = paymentCreated.getAmount();
                description = paymentCreated.getDescription();
            }
            case PaymentProcessingStarted paymentProcessingStarted -> {
                status = PaymentStatus.processingStarted;
                providerPaymentId = paymentProcessingStarted.getProviderPaymentId();
                providerRedirectUrl = paymentProcessingStarted.getProviderRedirectUrl();
            }
            case PaymentCompleted paymentCompleted -> {
                status = PaymentStatus.completed;
            }
            case PaymentFailed paymentFailed -> {
                status = PaymentStatus.failed;
            }
        }
    }
}
