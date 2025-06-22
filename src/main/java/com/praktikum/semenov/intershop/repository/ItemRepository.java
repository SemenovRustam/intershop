package com.praktikum.semenov.intershop.repository;

import com.praktikum.semenov.intershop.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ItemRepository extends R2dbcRepository<Item, Long> {

    Flux<Page<Item>> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    Flux<Page<Item>> findAllBy(Pageable pageable);

    Mono<Long> countByTitleContainingIgnoreCase(String title);

}
