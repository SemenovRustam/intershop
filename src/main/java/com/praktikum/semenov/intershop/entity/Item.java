package com.praktikum.semenov.intershop.entity;


import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "items")
public class Item {

    @Id
    private Long id;

    @Column
    private String title;

    @Column
    private String description;

    private String imgPath;

    private Integer count; // количество на складе

    private BigDecimal price;
}
