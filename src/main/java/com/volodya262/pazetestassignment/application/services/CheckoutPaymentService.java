package com.volodya262.pazetestassignment.application.services;

import com.volodya262.pazetestassignment.application.clients.PazeClient;
import com.volodya262.pazetestassignment.application.repository.PaymentRepository;
import com.volodya262.pazetestassignment.domain.Payment;
import com.volodya262.pazetestassignment.domain.PaymentStatus;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CheckoutPaymentService {
    public CheckoutPaymentService(
            PaymentRepository paymentRepository,
            PazeClient pazeClient,
            PaymentIdGenerator paymentIdGenerator,
            @Value("${application-base-url}") String applicationBaseUrl
    ) {
        this.paymentRepository = paymentRepository;
        this.pazeClient = pazeClient;
        this.paymentIdGenerator = paymentIdGenerator;
        this.applicationBaseUrl = applicationBaseUrl;
    }

    private final PaymentRepository paymentRepository;
    private final PazeClient pazeClient;
    private final PaymentIdGenerator paymentIdGenerator;
    private final String applicationBaseUrl;

    public Payment checkout(Money amount, String description) {
        long paymentId = paymentIdGenerator.getNextId();
        String redirectUrl = String.format("%s/checkout/%s/check", applicationBaseUrl, paymentId);
        var paymentCreatedEvent = pazeClient.createPayment(String.valueOf(paymentId), amount, redirectUrl);

        var payment = Payment.create(paymentId, amount, description);
        payment.toProcessingStarted(paymentCreatedEvent.id(), paymentCreatedEvent.redirectUrl());

        paymentRepository.save(payment);
        return payment;
    }

    public Payment checkPaymentStatus(long paymentId) {
        var payment = paymentRepository.getPaymentById(paymentId).orElseThrow();

        if (payment.getStatus() == PaymentStatus.completed || payment.getStatus() == PaymentStatus.failed) {
            return payment;
        }

        var providerPayment = pazeClient.getPayment(payment.getProviderPaymentId());

        if (providerPayment.isSuccess()) {
            payment.toCompleted();
        } else {
            payment.toFailed();
        }

        return payment;
    }
}
