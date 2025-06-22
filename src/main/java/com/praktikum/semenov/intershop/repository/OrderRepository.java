package com.praktikum.semenov.intershop.repository;

import com.praktikum.semenov.intershop.entity.Order;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface OrderRepository extends ReactiveCrudRepository<Order, Long> {

}
