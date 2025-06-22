package com.praktikum.semenov.intershop.service;

import com.praktikum.semenov.intershop.dto.ItemDto;
import com.praktikum.semenov.intershop.entity.Item;
import com.praktikum.semenov.intershop.entity.Order;
import com.praktikum.semenov.intershop.exception.ResourceNotFoundException;
import com.praktikum.semenov.intershop.mapper.ItemMapper;
import com.praktikum.semenov.intershop.repository.OrderRepository;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final ItemMapper mapper;
    private final CartService cartService;

    private final OrderRepository orderRepository;

    @Transactional
    public Mono<Order> createOrder() {
        List<ItemDto> allCartItems = cartService.getAllCartItems();

        List<Item> items = allCartItems
                .stream()
                .map(mapper::toItem)
                .toList();
        Order order = new Order();
        order.setItems(items);
        order.setTotalSum(cartService.getTotalPrice(allCartItems));
        cartService.clearCart();
        return orderRepository.save(order);
    }

    @Transactional(readOnly = true)
    public Flux<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Mono<Order> findById(Long id) {
        return orderRepository.findById(id)
                .switchIfEmpty(
                        Mono.error(new ResourceNotFoundException("Order by id " + id + " not found"))
                );
    }
}
