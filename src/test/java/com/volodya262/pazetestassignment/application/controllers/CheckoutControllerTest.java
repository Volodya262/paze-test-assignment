package com.volodya262.pazetestassignment.application.controllers;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.volodya262.pazetestassignment.application.repository.PaymentRepository;
import com.volodya262.pazetestassignment.application.services.PaymentIdGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import static com.github.tomakehurst.wiremock.client.WireMock.matchingJsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureWireMock
@AutoConfigureMockMvc
class CheckoutControllerTest {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentIdGenerator paymentIdGenerator;

    private final long testPaymentId = 150L;

    @BeforeEach
    void setUp() {
        paymentRepository.deleteAll();
        Mockito.when(paymentIdGenerator.getNextId()).thenReturn(testPaymentId);
    }

    @Test
    @DisplayName("Should send confirm request to paze when checkout")
    void shouldSendPaymentCreateRequestToPazeWhenCheckout() throws Exception {
        setupWireMockPazeCreateSuccessStub();

        var mockMvcRequest = MockMvcRequestBuilders
                .post("/checkout")
                .param("total", "1000");

        mockMvc.perform(mockMvcRequest)
                .andExpect(status().is3xxRedirection());

        WireMock.verify(
                WireMock.postRequestedFor(WireMock.urlEqualTo("/paze/payments"))
                        .withRequestBody(matchingJsonPath("$.paymentType", WireMock.equalTo("DEPOSIT")))
                        .withRequestBody(matchingJsonPath("$.amount", WireMock.equalTo("1000.0")))
                        .withRequestBody(matchingJsonPath("$.currency", WireMock.equalTo("EUR")))
        );
    }

    @Test
    @DisplayName("should return redirect to provider url when checkout")
    void shouldReturnRedirectToProviderUrlWhenCheckout() throws Exception {
        setupWireMockPazeCreateSuccessStub();

        var mockMvcRequest = MockMvcRequestBuilders
                .post("/checkout")
                .param("total", "1000");

        mockMvc.perform(mockMvcRequest)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("https://demo.paze.eu/payment/12345"));
    }

    @Test
    @DisplayName("given created payment should check payment status on provider and redirect to checkout success")
    void givenCreatedPaymentShouldCheckPaymentStatusOnProviderAndRedirectToCheckoutSuccess() throws Exception {
        setupWireMockPazeCreateSuccessStub();

        var mockMvcCheckoutRequest = MockMvcRequestBuilders
                .post("/checkout")
                .param("total", "1000");

        mockMvc.perform(mockMvcCheckoutRequest)
                .andExpect(status().is3xxRedirection());

        setupWireMockPazePaymentSuccessStub();

        mockMvc.perform(get("/checkout/{testPaymentId}/check", testPaymentId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("success"));
    }

    @Test
    @DisplayName("given created payment should check payment status on provider and redirect to checkout failure")
    void givenCreatedPaymentShouldCheckPaymentStatusOnProviderAndRedirectToCheckoutFailure() throws Exception {
        setupWireMockPazeCreateSuccessStub();

        var mockMvcCheckoutRequest = MockMvcRequestBuilders
                .post("/checkout")
                .param("total", "1000");

        mockMvc.perform(mockMvcCheckoutRequest)
                .andExpect(status().is3xxRedirection());

        setupWireMockPazePaymentDeclinedStub();

        mockMvc.perform(get("/checkout/{testPaymentId}/check", testPaymentId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("failure"));
    }

    private void setupWireMockPazeCreateSuccessStub() {
        WireMock.stubFor(
                WireMock.post("/paze/payments")
                        .willReturn(
                                WireMock.aResponse()
                                        .withStatus(200)
                                        .withHeader("Content-Type", "application/json")
                                        .withBody(
                                                """
                                                        {
                                                            "status": 200,
                                                            "result": {
                                                                "id": "12345",
                                                                "state": "CHECKOUT",
                                                                "redirectUrl": "https://demo.paze.eu/payment/12345"
                                                            }
                                                        }"""
                                        )));
    }

    private void setupWireMockPazePaymentSuccessStub() {
        WireMock.stubFor(
                WireMock.get("/paze/payments/12345")
                        .willReturn(
                                WireMock.aResponse()
                                        .withStatus(200)
                                        .withHeader("Content-Type", "application/json")
                                        .withBody(
                                                """
                                                        {
                                                            "status": 200,
                                                            "result": {
                                                                "id": "12345",
                                                                "state": "COMPLETED"
                                                            }
                                                        }"""
                                        )));
    }

    private void setupWireMockPazePaymentDeclinedStub() {
        WireMock.stubFor(
                WireMock.get("/paze/payments/12345")
                        .willReturn(
                                WireMock.aResponse()
                                        .withStatus(200)
                                        .withHeader("Content-Type", "application/json")
                                        .withBody(
                                                """
                                                        {
                                                            "status": 200,
                                                            "result": {
                                                                "id": "12345",
                                                                "state": "DECLINED"
                                                            }
                                                        }"""
                                        )));
    }
}