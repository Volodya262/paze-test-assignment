package com.volodya262.pazetestassignment.application.controllers;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

public class CheckoutModel {
    public CheckoutModel() {
    }

    public CheckoutModel(BigDecimal total) {
        this.total = total;
    }

    @Getter
    @Setter
    private BigDecimal total;
}
