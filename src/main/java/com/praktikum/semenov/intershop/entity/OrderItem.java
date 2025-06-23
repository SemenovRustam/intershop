package com.praktikum.semenov.intershop.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("orders_items")
public class OrderItem {

    @Column("order_id")
    private Long orderId;

    @Column("item_id")
    private Long itemId;
}
