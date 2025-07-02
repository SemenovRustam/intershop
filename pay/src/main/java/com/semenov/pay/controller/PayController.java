package com.semenov.pay.controller;

import com.semenov.pay.service.PayService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/pay")
@RequiredArgsConstructor
public class PayController {

    private final PayService payService;

    @GetMapping("/balance")
    public Mono<BigDecimal> getBalance() {
        return payService.getBalance();
    }

    @PostMapping
    public Mono<Boolean> pay(@RequestParam BigDecimal amount) {
            return payService.pay(amount);
    }
}
