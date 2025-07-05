package com.praktikum.semenov.intershop.dto;

import com.praktikum.semenov.intershop.entity.Item;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemsPageDto {
    private List<Item> items;
    private int pageNumber;
    private int pageSize;
    private long total;
    private String sort; // или более сложный класс для сортировки
}