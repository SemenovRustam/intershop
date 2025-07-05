package com.semenov.pay.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PayServiceTest {

    @InjectMocks
    private PayService payService;

    @BeforeEach
    void init () {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getBalance_shouldReturnCurrentBalance() {
        // When
        Mono<BigDecimal> balanceMono = payService.getBalance();

        // Then
        StepVerifier.create(balanceMono)
                .assertNext(balance -> {
                    assertTrue(balance.compareTo(BigDecimal.valueOf(3000)) >= 0);
                    assertTrue(balance.compareTo(BigDecimal.valueOf(10000)) <= 0);
                    assertEquals(2, balance.scale());
                })
                .verifyComplete();
    }

    @Test
    void pay_whenAmountMoreThanBalance_shouldReturnFalseAndNotUpdateBalance() {
        // Given
        BigDecimal initialBalance = payService.getBalance().block();
        BigDecimal amount = initialBalance.multiply(BigDecimal.valueOf(2));

        // When
        Mono<Boolean> paymentResult = payService.pay(amount);

        // Then
        StepVerifier.create(paymentResult)
                .expectNext(false)
                .verifyComplete();

        BigDecimal currentBalance = payService.getBalance().block();
        assertEquals(initialBalance, currentBalance);
    }
}