package com.praktikum.semenov.intershop.controller;

import com.praktikum.semenov.intershop.dto.CartAction;
import com.praktikum.semenov.intershop.entity.Item;
import com.praktikum.semenov.intershop.exception.ResourceNotFoundException;
import com.praktikum.semenov.intershop.service.CartService;
import com.praktikum.semenov.intershop.service.ItemService;
import java.util.Optional;
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
    public String changeItemCount(@PathVariable Long id, @RequestParam String action) {

        cartService.changeItemCount(id, CartAction.valueOf(action.toUpperCase()));
        return "redirect:/items/{id}";
    }
}
