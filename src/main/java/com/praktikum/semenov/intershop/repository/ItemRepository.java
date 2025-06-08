package com.praktikum.semenov.intershop.repository;

import com.praktikum.semenov.intershop.entity.Item;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
    Page<Item> findByTitleContainingIgnoreCase(String title, Pageable pageable);
}
