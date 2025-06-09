package com.praktikum.semenov.intershop.service;

import com.praktikum.semenov.intershop.dto.ItemDto;
import com.praktikum.semenov.intershop.entity.Item;
import com.praktikum.semenov.intershop.exception.ResourceNotFoundException;
import com.praktikum.semenov.intershop.mapper.ItemMapper;
import com.praktikum.semenov.intershop.repository.ItemRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    // Получение страницы товаров с учетом поиска
    public Page<Item> getItems(String search, Pageable pageable) {
        if (search == null || search.isEmpty()) {
            return itemRepository.findAll(pageable); // Если нет поиска, возвращаем все товары
        } else {
            return itemRepository.findByTitleContainingIgnoreCase(search, pageable); // Поиск по названию
        }
    }

    // Получение товара по ID
    public Optional<Item> getItemById(Long id) {
        return itemRepository.findById(id);
    }

    // Обновление существующего товара
    public Item updateItem(Long id, Item itemDetails) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found with id " + id));

        item.setTitle(itemDetails.getTitle());
        item.setDescription(itemDetails.getDescription());
        item.setImgPath(itemDetails.getImgPath());
        item.setCount(itemDetails.getCount());
        item.setPrice(itemDetails.getPrice());

        return itemRepository.save(item);
    }

    // Удаление товара
    public void deleteItem(Long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found with id " + id));
        itemRepository.delete(item);
    }

    public List<ItemDto> findAllItemByIds(Iterable<Long> ids) {
        List<Item> allById = itemRepository.findAllById(ids);
        return allById
                .stream()
                .map(itemMapper::toItemDto)
                .toList();
    }
}



