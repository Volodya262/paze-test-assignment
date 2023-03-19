package com.volodya262.pazetestassignment.application.clients.dtos;

import com.volodya262.pazetestassignment.domain.providerpayment.ProviderPayment;
import lombok.NonNull;

public record PazeGetPaymentResponse(@NonNull String status, @NonNull PazeGetPaymentResponseResult result) {
    public ProviderPayment getProviderPayment() {
        return new ProviderPayment(result.id(), result().isSuccess(), result().isFailure());
    }
}
