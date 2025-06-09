package com.praktikum.semenov.intershop.service;

import com.praktikum.semenov.intershop.dto.CartAction;
import com.praktikum.semenov.intershop.dto.ItemDto;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartService {

    private final ItemService itemService;
    private final Map<Long, Integer> cart = new HashMap<>();

    public void changeItemCount(Long itemId, CartAction action) {
//        int count = cart.getOrDefault(itemId, 0);
//        switch (action) {
//            case PLUS -> cart.put(itemId, count + 1);
//            case MINUS -> {
//                if (count <= 1) {
//                    cart.remove(itemId);
//                } else {
//                    cart.put(itemId, count - 1);
//                }
//            }
//            case REMOVE -> cart.remove(itemId);
//        }

        switch (action) {
            case PLUS -> cart.compute(itemId, (k, v) -> isNull(v) ? 1 : v + 1);
            case MINUS -> cart.compute(itemId, (k, v) -> (isNull(v) || v == 0) ? 0 : v - 1);
            case DELETE -> cart.remove(itemId);
            default ->  log.info("not found action");
        }
    }

    public List<ItemDto> getAllCartItems() {
        Set<Long> ids = cart.keySet();
        return itemService.findAllItemByIds(ids)
                .stream()
                .map(this::convertItemWithCartCount)
                .toList();
    }

    public BigDecimal getTotalPrice(List<ItemDto> cartItems) {
        return cartItems.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getCount())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void clearCart() {
        cart.clear();
        log.info("Clear cart");
    }

    private ItemDto convertItemWithCartCount(ItemDto item) {
        item.setCount(cart.computeIfAbsent(item.getId(), k -> 0));
        return item;
    }

    public boolean isCartEmpty() {
        return cart.keySet().isEmpty();
    }
}