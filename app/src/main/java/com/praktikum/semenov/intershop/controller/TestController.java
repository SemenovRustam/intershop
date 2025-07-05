package com.praktikum.semenov.intershop.controller;

import com.praktikum.semenov.intershop.service.PayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Slf4j
public class TestController {

    private final PayService payService;


    @GetMapping("/test")
    public Mono<BigDecimal> test() {
        log.info("test request");
        return payService.checkBalance()
                .doOnNext(result -> log.info("result " + result));
                // возвращаем Mono<Void>
    }
}
