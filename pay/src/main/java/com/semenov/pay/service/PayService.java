package com.semenov.pay.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.ThreadLocalRandom;

@Service
@Slf4j
public class PayService {

    private BigDecimal BALANCE = BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(1000, 10_000))
            .setScale(2, RoundingMode.HALF_EVEN);

    public Mono<BigDecimal> getBalance() {
        return Mono.just(BALANCE);
    }

    public Mono<Boolean> pay(BigDecimal amount) {
        int result = amount.compareTo(BALANCE);

        if (result < 0) {
            log.error("Amount a more then balance!");
            return Mono.just(false);
        }

        BALANCE.subtract(amount);
        log.info("Current balance = {}", BALANCE);
        return Mono.just(true);
    }
}
