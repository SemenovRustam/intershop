package com.praktikum.semenov.intershop.service;

import com.praktikum.semenov.intershop.dto.ItemDto;
import com.praktikum.semenov.intershop.dto.OrderDto;
import com.praktikum.semenov.intershop.entity.Item;
import com.praktikum.semenov.intershop.entity.Order;
import com.praktikum.semenov.intershop.entity.OrderItem;
import com.praktikum.semenov.intershop.exception.ResourceNotFoundException;
import com.praktikum.semenov.intershop.mapper.ItemMapper;
import com.praktikum.semenov.intershop.repository.ItemRepository;
import com.praktikum.semenov.intershop.repository.OrderItemRepository;
import com.praktikum.semenov.intershop.repository.OrderRepository;
import java.math.BigDecimal;
import java.util.List;
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
    private final OrderItemRepository orderItemRepository;
    private final ItemRepository itemRepository;

    @Transactional
    public Mono<Order> createOrder() {
        return cartService.getAllCartItems()
                .flatMap(allCartItems -> {
                    List<Item> items = allCartItems.stream()
                            .map(mapper::toItem)
                            .toList();

                    Mono<BigDecimal> totalPriceMono = cartService.getTotalPrice(Mono.just(allCartItems));

                    return totalPriceMono.flatMap(totalPrice -> {
                        Order order = new Order();
                        order.setTotalSum(totalPrice);

                        // Сохраняем заказ без items, чтобы получить ID
                        return orderRepository.save(order)
                                .flatMap(savedOrder -> {
                                    // Для каждого Item создаём OrderItem с savedOrder.getId()
                                    List<Mono<OrderItem>> orderItemsSaves = items.stream()
                                            .map(item -> {
                                                OrderItem orderItem = new OrderItem();
                                                orderItem.setOrderId(savedOrder.getId());
                                                orderItem.setItemId(item.getId());
                                                return orderItemRepository.save(orderItem);
                                            })
                                            .toList();

                                    // Сохраняем все связи, ждём их завершения
                                    return Flux.concat(orderItemsSaves)
                                            .then(cartService.clearCart())  // очищаем корзину после сохранения связей
                                            .thenReturn(savedOrder);        // возвращаем сохранённый заказ
                                });
                    });
                });
    }

    @Transactional(readOnly = true)
    public Flux<OrderDto> getAllOrders() {
        return orderRepository.findAll()
                .flatMap(order -> orderItemRepository.findItemsByOrderId(order.getId())
                        .flatMap(item -> itemRepository.findById(item.getId()))
                        .map(mapper::toItemDto)
                        .collectList()
                        .map(items -> new OrderDto(order.getId(), order.getTotalSum(), items))
                );
    }


    public Mono<OrderDto> findById(Long id) {
        return orderRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Order by id " + id + " not found")))
                .flatMap(order ->
                        orderItemRepository.findItemsByOrderId(order.getId())
                                .map(mapper::toItemDto)
                                .collectList()
                                .map(items -> new OrderDto(order.getId(), order.getTotalSum(), items))
                );
    }
}
