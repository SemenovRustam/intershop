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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartService {

    private final ItemService itemService;
    private final SecurityService securityService;
    private final Map<Long, Map<Long, Integer>> cart = new HashMap<>();

    public Mono<Void> changeItemCount(Long itemId, String action) {
        CartAction cartAction = CartAction.valueOf(action.toUpperCase());

        return securityService.getCurrentUserId().flatMap(
                userId -> {
                    Map<Long, Integer> userCart = cart.computeIfAbsent(userId, k -> new HashMap<>());

                    switch (cartAction) {
                        case PLUS -> userCart.compute(itemId, (k, v) -> isNull(v) ? 1 : v + 1);
                        case MINUS -> userCart.compute(itemId, (k, v) -> (isNull(v) || v == 0) ? 0 : v - 1);
                        case DELETE -> userCart.remove(itemId);
                        default -> log.info("not found action");
                    }
                    return Mono.empty();
                });
    }

    public Mono<List<ItemDto>> getAllCartItems() {
        return securityService.getCurrentUserId()
                .flatMap(userId -> {
                            var userCart = cart.get(userId);
                            Set<Long> ids = userCart.keySet();
                            return itemService.findAllItemByIds(ids)
                                    .map(itemDto -> convertItemWithCartCount(itemDto, userCart))
                                    .collectList();
                        }
                );
    }

    public Mono<BigDecimal> getTotalPrice(Mono<List<ItemDto>> cartItems) {
        return cartItems
                .flatMapMany(Flux::fromIterable)
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getCount())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Mono<Void> clearUserCart() {
        return securityService.getCurrentUserId()
                .doOnNext(userId -> {
                            cart.remove(userId);
                            log.info("Clear cart for user {}", userId);
                        }
                ).then();
    }


    public Mono<Boolean> cartIsEmpty() {
        return securityService.getCurrentUserId()
                .map(userId -> cart.getOrDefault(userId, new HashMap<>())
                        .isEmpty()
                );
    }

    private ItemDto convertItemWithCartCount(ItemDto item, Map<Long, Integer> userCart) {
        item.setCount(userCart.computeIfAbsent(item.getId(), k -> 0));
        return item;
    }
}