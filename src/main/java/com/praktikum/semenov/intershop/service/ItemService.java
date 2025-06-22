package com.praktikum.semenov.intershop.service;

import com.praktikum.semenov.intershop.dto.ItemDto;
import com.praktikum.semenov.intershop.entity.Item;
import com.praktikum.semenov.intershop.exception.ResourceNotFoundException;
import com.praktikum.semenov.intershop.mapper.ItemMapper;
import com.praktikum.semenov.intershop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    // Получение страницы товаров с учетом поиска
    public Flux<Page<Item>> getItems(String search, Pageable pageable) {
        if (search == null || search.isEmpty()) {
            return itemRepository.findAllBy(pageable); // Если нет поиска, возвращаем все товары
        } else {
            return itemRepository.findByTitleContainingIgnoreCase(search, pageable); // Поиск по названию
        }
    }

    // Получение товара по ID
    public Mono<Item> getItemById(Long id) {
        return itemRepository.findById(id);
    }

    // Обновление существующего товара
    public Mono<Item> updateItem(Long id, Item itemDetails) {
        return itemRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Item not found with id " + id)))
                .flatMap(item -> {
                    item.setTitle(itemDetails.getTitle());
                    item.setDescription(itemDetails.getDescription());
                    item.setImgPath(itemDetails.getImgPath());
                    item.setCount(itemDetails.getCount());
                    item.setPrice(itemDetails.getPrice());

                    return itemRepository.save(item);
                });
    }

    // Удаление товара
    public Mono<Void> deleteItem(Long id) {
        return itemRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Item not found with id " + id)))
                .flatMap(itemRepository::delete);
    }

    public Flux<ItemDto> findAllItemByIds(Iterable<Long> ids) {
        return itemRepository.findAllById(ids)
                .map(itemMapper::toItemDto);

    }
}



