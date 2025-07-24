package com.praktikum.semenov.intershop.mapper;

import com.praktikum.semenov.intershop.dto.ItemDto;
import com.praktikum.semenov.intershop.entity.Item;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

public class ItemMapperTest {

    private ItemMapper itemMapper;

    @BeforeEach
    void setUp() {
        // Получаем реализацию маппера
        itemMapper = Mappers.getMapper(ItemMapper.class);
    }

    @Test
    void testToItemDto() {
        Item item = new Item();
        item.setId(1L);
        item.setPrice(BigDecimal.valueOf(100));


        ItemDto dto = itemMapper.toItemDto(item);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(item.getId());

        assertThat(dto.getPrice()).isEqualTo(item.getPrice());
    }

    @Test
    void testToItem() {
        ItemDto dto = new ItemDto();
        dto.setId(2L);
        dto.setPrice(BigDecimal.valueOf(200));

        Item item = itemMapper.toItem(dto);

        assertThat(item).isNotNull();
        assertThat(item.getId()).isEqualTo(dto.getId());
        assertThat(item.getPrice()).isEqualTo(dto.getPrice());
    }
}
