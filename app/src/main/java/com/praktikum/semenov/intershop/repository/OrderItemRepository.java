package com.praktikum.semenov.intershop.repository;

import com.praktikum.semenov.intershop.entity.Item;
import com.praktikum.semenov.intershop.entity.OrderItem;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.util.function.Tuple2;

public interface OrderItemRepository extends R2dbcRepository<OrderItem, Tuple2<Long, Long>> {

    @Query("""
            SELECT i.id, i.title, i.price
            FROM items i
            JOIN orders_items oi ON i.id = oi.item_id
            WHERE oi.order_id = :orderId
            """)
    Flux<Item> findItemsByOrderId(Long orderId);
}
