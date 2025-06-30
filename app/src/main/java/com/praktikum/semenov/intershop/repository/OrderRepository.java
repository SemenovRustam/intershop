package com.praktikum.semenov.intershop.repository;

import com.praktikum.semenov.intershop.entity.Order;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface OrderRepository extends ReactiveCrudRepository<Order, Long> {

}
