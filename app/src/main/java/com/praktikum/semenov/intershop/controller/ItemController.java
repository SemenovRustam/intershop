package com.praktikum.semenov.intershop.controller;

import com.praktikum.semenov.intershop.exception.ResourceNotFoundException;
import com.praktikum.semenov.intershop.service.CartService;
import com.praktikum.semenov.intershop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;
    private final CartService cartService;

    @GetMapping("/{id}")
    public Mono<String> getItemInfo(@PathVariable Long id, Model model) {
        return itemService.getItemById(id)
                .flatMap(item -> {
                    model.addAttribute("item", item);
                    return Mono.just("item");
                })
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Item by id " + id + " not found.")));
    }

    @PostMapping("/{id}")
    public Mono<String> changeItemCount(@PathVariable Long id, @RequestParam String action) {
        return cartService.changeItemCount(id, action)
                .then(Mono.just("redirect:/items/{id}"));
    }
}
