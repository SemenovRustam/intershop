package com.praktikum.semenov.intershop.controller;

import com.praktikum.semenov.intershop.service.OrderService;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;


@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/buy")
    public Mono<ServerResponse> createOrder() {
        return orderService.createOrder()
                .flatMap(order ->
                        ServerResponse.seeOther(URI.create("/orders/${order.id}?newOrder=true")).build()
                );
    }

    @GetMapping("/orders")
    public Mono<String> getOrders(Model model) {
        return orderService.getAllOrders()
                .collectList()
                .flatMap(orders -> {
                            model.addAttribute("orders", orders);
                            return Mono.just("orders");
                        }
                );

    }

    @GetMapping("/orders/{id}")
    public Mono<String> getOrderInfo(
            @PathVariable Long id,
            Model model,
            @RequestParam(required = false, defaultValue = "false") Boolean newOrder
    ) {
        return orderService.findById(id)
                .flatMap(order -> {
                    model.addAttribute("order", order);
                    model.addAttribute("newOrder", newOrder);
                    return Mono.just("order");
                });
    }
}
