package com.praktikum.semenov.intershop.repository;

import com.praktikum.semenov.intershop.entity.Order;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface OrderRepository extends ReactiveCrudRepository<Order, Long> {

    public Flux<Order> findAllOrderByUserId(Long userId);
}
