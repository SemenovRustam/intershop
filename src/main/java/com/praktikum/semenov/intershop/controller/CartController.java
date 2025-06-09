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

@Controller
@RequestMapping("/cart/items")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping()
    public String getCartItems(Model model) {
        List<ItemDto> cartItems = cartService.getAllCartItems();
        BigDecimal totalPrice = cartService.getTotalPrice(cartItems);
        boolean isEmpty = cartService.isCartEmpty();

        model.addAttribute("items", cartItems);
        model.addAttribute("total", totalPrice);
        model.addAttribute("empty", isEmpty);

        return "/cart";
    }

    @PostMapping("/{itemId}")
    public String changeItemCount(
            @PathVariable Long itemId,
            @RequestParam String action
    ) {
        CartAction cartAction = CartAction.valueOf(action.toUpperCase());

        cartService.changeItemCount(itemId, cartAction);
        return "redirect:/main/items/";
    }
}

