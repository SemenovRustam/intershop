package com.praktikum.semenov.intershop.controller;


import com.praktikum.semenov.intershop.dto.ItemDto;
import com.praktikum.semenov.intershop.service.CartService;
import com.praktikum.semenov.intershop.service.PayService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/cart/items")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final PayService payService;

    @GetMapping()
    public Mono<String> getCartItems(Model model) {
        Mono<List<ItemDto>> cartItems = cartService.getAllCartItems();
        Mono<BigDecimal> totalPrice = cartService.getTotalPrice(cartItems);
        Mono<Boolean> cartEmpty = cartService.isCartEmpty();
        Mono<BigDecimal> balance = payService.checkBalance();

        return Mono.zip(cartItems, totalPrice, cartEmpty, balance)
                .flatMap(tuple -> {
                    model.addAttribute("items", tuple.getT1());
                    model.addAttribute("total", tuple.getT2());
                    model.addAttribute("empty", tuple.getT3());
                    model.addAttribute("canBuy", tuple.getT2().compareTo(tuple.getT4()) > 0);
                    return Mono.just("cart");
                });
    }


    @PostMapping("/{itemId}")
    public Mono<String> changeItemCount(@PathVariable Long itemId, @RequestParam String action) {

        return cartService.changeItemCount(itemId, action)
                .then(Mono.just("redirect:/main/items"));
    }
}

