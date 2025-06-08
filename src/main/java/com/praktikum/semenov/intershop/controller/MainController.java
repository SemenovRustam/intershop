package com.praktikum.semenov.intershop.controller;

import com.praktikum.semenov.intershop.dto.PagingModel;
import com.praktikum.semenov.intershop.entity.Item;
import com.praktikum.semenov.intershop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/main")
@RequiredArgsConstructor
public class MainController {

    private final ItemService itemService;

    @GetMapping("/")
    public String redirectToMainItems() {
        return "redirect:/main/items";
    }

    @GetMapping("/items")
    public String getItems(
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "NO") String sort,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "1") int pageNumber,
            Model model) {

        // Преобразуем pageNumber в индекс страницы
        PageRequest pageRequest = PageRequest.of(pageNumber - 1, pageSize, getSortOrder(sort));

        // Получаем страницу товаров
        Page<Item> itemsPage = itemService.getItems(search, pageRequest);

        // Подготавливаем данные для отображения в шаблоне
        model.addAttribute("items", itemsPage.getContent());
        model.addAttribute("search", search);
        model.addAttribute("sort", sort);
        model.addAttribute("paging", createPagingModel(itemsPage));

        return "main"; // Возвращаем имя шаблона
    }

    private Sort getSortOrder(String sort) {
        switch (sort) {
            case "ALPHA":
                return Sort.by("title").ascending();
            case "PRICE":
                return Sort.by("price").ascending();
            default:
                return Sort.unsorted(); // NO - не сортируем
        }
    }

    private PagingModel createPagingModel(Page<Item> itemsPage) {
        return PagingModel.builder()
                .pageNumber(itemsPage.getNumber() + 1)
                .hasNext(itemsPage.hasNext())
                .hasPrevious(itemsPage.hasPrevious())
                .pageSize(itemsPage.getSize())
                .build();
    }
}

