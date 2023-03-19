package com.volodya262.pazetestassignment.domain.providerpayment;

import lombok.NonNull;

public record CreatedProviderPayment(
        @NonNull String id,
        @NonNull String redirectUrl
) {
}
