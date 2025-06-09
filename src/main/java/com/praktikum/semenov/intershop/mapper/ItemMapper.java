package com.praktikum.semenov.intershop.mapper;

import com.praktikum.semenov.intershop.dto.ItemDto;
import com.praktikum.semenov.intershop.entity.Item;
import org.mapstruct.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface ItemMapper {

    ItemDto toItemDto(Item item);

    Item toItem(ItemDto itemDto);
}
