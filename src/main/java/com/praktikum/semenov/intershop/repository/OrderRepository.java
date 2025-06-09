package com.praktikum.semenov.intershop.repository;

import com.praktikum.semenov.intershop.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
