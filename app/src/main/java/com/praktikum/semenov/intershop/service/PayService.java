package com.praktikum.semenov.intershop.service;

import com.praktikum.semenov.intershop.client.PayClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class PayService {

    private final PayClient payClient;

    public Mono<BigDecimal> checkBalance() {
        System.out.println("cache+++++");
        return payClient.getBalance();
    }

    public Mono<Boolean> pay(BigDecimal amount) {
        return payClient.pay(amount);
    }
}
