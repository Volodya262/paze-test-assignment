package com.volodya262.pazetestassignment.application.clients.dtos;

import com.volodya262.pazetestassignment.domain.providerpayment.CreatedProviderPayment;
import lombok.NonNull;

public record PazePaymentCreateResponse(@NonNull String status, @NonNull CreatedProviderPayment result) {
    public CreatedProviderPayment getProviderPayment() {
        return result;
    }
}

