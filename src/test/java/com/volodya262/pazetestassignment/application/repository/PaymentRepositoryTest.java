package com.volodya262.pazetestassignment.application.repository;

import com.volodya262.pazetestassignment.domain.Payment;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class PaymentRepositoryTest {

    @Autowired
    private PaymentRepository paymentRepository;

    @BeforeEach
    void setUp() {
        paymentRepository.deleteAll();
    }

    @Test
    void shouldSaveAndGetCreatedPayment() {
        var payment = Payment.create(1, Money.of(CurrencyUnit.EUR, 100), "some payment");
        paymentRepository.save(payment);

        var dbPayment = paymentRepository.getPaymentById(1).orElseThrow();
        assertEquals(payment.getStatus(), dbPayment.getStatus());
        assertEquals(payment.getPaymentId(), dbPayment.getPaymentId());
        assertEquals(payment.getAmount().getAmount(), dbPayment.getAmount().getAmount());
    }
}