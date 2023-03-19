package com.volodya262.pazetestassignment.application.controllers;

import com.volodya262.pazetestassignment.application.services.CheckoutPaymentService;
import com.volodya262.pazetestassignment.domain.PaymentStatus;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/checkout")
@RequiredArgsConstructor
public class CheckoutController {
    private final CheckoutPaymentService checkoutPaymentService;

    @GetMapping("")
    public String getMainCheckout(Model model) {
        model.addAttribute("checkout", new CheckoutModel(new BigDecimal(1000)));
        return "/checkout";
    }

    @PostMapping("")
    public String postCheckout(@ModelAttribute("checkout") CheckoutModel checkoutModel) {
        var amount = Money.of(CurrencyUnit.EUR, checkoutModel.getTotal());
        var payment = checkoutPaymentService.checkout(amount, "payment for order 123");
        return String.format("redirect:%s", payment.getProviderRedirectUrl());
    }

    @GetMapping("/{paymentId}/check")
    public String getCheckoutPaymentStatus(@PathVariable long paymentId) {
        var payment = checkoutPaymentService.checkPaymentStatus(paymentId);
        if (payment.getStatus() == PaymentStatus.completed) {
            return "redirect:success";
        } else {
            return "redirect:failure";
        }
    }
}
