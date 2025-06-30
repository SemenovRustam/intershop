package com.praktikum.semenov.intershop.controller;


import com.praktikum.semenov.intershop.dto.CartAction;
import com.praktikum.semenov.intershop.dto.ItemDto;
import com.praktikum.semenov.intershop.service.CartService;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Mono;

@Controller
@RequestMapping("/cart/items")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping()
    public Mono<String> getCartItems(Model model) {
        Mono<List<ItemDto>> cartItems = cartService.getAllCartItems();
        Mono<BigDecimal> totalPrice = cartService.getTotalPrice(cartItems);
        Mono<Boolean> cartEmpty = cartService.isCartEmpty();

        return Mono.zip(cartItems, totalPrice, cartEmpty)
                .flatMap(tuple -> {
                    model.addAttribute("items", tuple.getT1());
                    model.addAttribute("total", tuple.getT2());
                    model.addAttribute("empty", tuple.getT3());
                    return Mono.just("cart");
                });
    }


    @PostMapping("/{itemId}")
    public Mono<String> changeItemCount(@PathVariable Long itemId, @RequestParam String action) {

        return cartService.changeItemCount(itemId, action)
                .then(Mono.just("redirect:/main/items"));
    }
}

