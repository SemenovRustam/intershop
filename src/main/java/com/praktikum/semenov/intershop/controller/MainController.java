package com.praktikum.semenov.intershop.controller;

import com.praktikum.semenov.intershop.dto.CartAction;
import com.praktikum.semenov.intershop.dto.PagingModel;
import com.praktikum.semenov.intershop.dto.SortDto;
import com.praktikum.semenov.intershop.entity.Item;
import com.praktikum.semenov.intershop.service.CartService;
import com.praktikum.semenov.intershop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Controller
@RequestMapping("/main/items")
@RequiredArgsConstructor
public class MainController {

    private final ItemService itemService;
    private final CartService cartService;

    @GetMapping("/")
    public Mono<String> getItems(
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "NO") SortDto sort,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "1") int pageNumber,
            Model model) {

        // Преобразуем pageNumber в индекс страницы
        PageRequest pageRequest = PageRequest.of(pageNumber - 1, pageSize, getSortOrder(sort));

//        // Получаем страницу товаров
        return itemService.getItems(search, pageRequest)
                .map(items -> {
                    model.addAttribute("items", items.getContent());
                    model.addAttribute("search", search);
                    model.addAttribute("sort", sort);
                    model.addAttribute("paging", createPagingModel(items));
                    return "main";
                });
    }

    @PostMapping("{itemId}")
    public Mono<String> changeItemCount(@PathVariable Long itemId, ServerWebExchange exchange) {
        return exchange.getFormData()
                .flatMap(formData -> {
                    String action = formData.getFirst("action");
                    if (action == null) {
                        return Mono.error(new IllegalArgumentException("Missing action parameter"));
                    }
                    CartAction cartAction = CartAction.valueOf(action.toUpperCase());
                    return cartService.changeItemCount(itemId, cartAction)
                            .thenReturn("redirect:/main/items/");
                });
    }

    private Sort getSortOrder(SortDto sort) {
       return switch (sort) {
            case ALPHA -> Sort.by("title").ascending();
            case PRICE -> Sort.by("price").ascending();
            default -> Sort.unsorted();
        };
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

