package com.praktikum.semenov.intershop.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {

    private Long id;
    private String title;
    private String description;
    private String imgPath;
    private Integer count;
    private BigDecimal price;
}
