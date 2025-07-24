package com.praktikum.semenov.intershop.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    private Long id;
    private BigDecimal totalSum;
    private List<ItemDto> items = new ArrayList<>();;

    public List<ItemDto> items() {
        return items;
    }

    public BigDecimal totalSum() {
        return totalSum;
    }
}
