package com.volodya262.pazetestassignment.application.clients.dtos;

import lombok.NonNull;

public record PazeGetPaymentResponseResult(
        @NonNull String id,
        @NonNull String state
) {
    public boolean isSuccess() {
        return state.equals("COMPLETED");
    }


    public boolean isFailure() {
        return state.equals("DECLINED");
    }
}
