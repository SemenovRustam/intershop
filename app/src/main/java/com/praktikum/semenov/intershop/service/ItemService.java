package com.praktikum.semenov.intershop.service;

import com.praktikum.semenov.intershop.dto.ItemDto;
import com.praktikum.semenov.intershop.dto.ItemsPageDto;
import com.praktikum.semenov.intershop.entity.Item;
import com.praktikum.semenov.intershop.exception.ResourceNotFoundException;
import com.praktikum.semenov.intershop.mapper.ItemMapper;
import com.praktikum.semenov.intershop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    @Cacheable(value = "itemsCache", key = "T(java.util.Objects).hash(#search, #pageable.pageNumber, #pageable.pageSize, #pageable.sort.toString())")
    public Mono<ItemsPageDto> getItems(String search, Pageable pageable) {
        log.info("+++++ cache MISS");

        Flux<Item> itemFlux = (search == null || search.isEmpty()) ? itemRepository.findAllBy(pageable) :
                itemRepository.findByTitleContainingIgnoreCase(search, pageable);

        Mono<Long> totalMono = (search == null || search.isEmpty())
                ? itemRepository.count()
                : itemRepository.countByTitleContainingIgnoreCase(search);

        return Mono.zip(itemFlux.collectList(), totalMono)
                .map(tuple -> new ItemsPageDto(
                                tuple.getT1(),
                                pageable.getPageNumber(),
                                pageable.getPageSize(),
                                tuple.getT2(),
                                pageable.getSort().toString()
                        )
                );
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



