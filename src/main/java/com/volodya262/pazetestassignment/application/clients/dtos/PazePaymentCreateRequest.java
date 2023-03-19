package com.volodya262.pazetestassignment.application.clients.dtos;

import java.math.BigDecimal;

public record PazePaymentCreateRequest(
        String paymentType,
        BigDecimal amount,
        String currency,
        String referenceId,
        String returnUrl
) {

}
