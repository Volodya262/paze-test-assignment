package com.volodya262.pazetestassignment.domain.providerpayment;

import lombok.NonNull;

public record ProviderPayment(
        @NonNull String id,
        boolean isSuccess,
        boolean isFailure
) {
}
