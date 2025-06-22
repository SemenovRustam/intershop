package com.praktikum.semenov.intershop.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("orders_items")
public class OrderItem {
    @Id
    private Long id;

    private Long orderId;

    private Long itemId;
}
