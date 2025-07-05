package com.praktikum.semenov.intershop.client;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


import java.math.BigDecimal;

@Component
public class PayClient {

    private final WebClient webClient;

    public PayClient(WebClient.Builder webClientBuilder,
                     @Value("${pay.service.base-url}") String baseUrl) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }


    public Mono<BigDecimal> getBalance() {
        return webClient.get()
                .uri("/balance")
                .retrieve()
                .bodyToMono(BigDecimal.class);
    }

    public Mono<Boolean> pay(BigDecimal amount) {
        return webClient.post()
                .uri(uriBuilder -> uriBuilder.queryParam("amount", amount).build())
                .retrieve()
                .bodyToMono(Boolean.class);
    }
}
