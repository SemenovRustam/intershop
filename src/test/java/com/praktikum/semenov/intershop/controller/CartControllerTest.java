package com.praktikum.semenov.intershop.controller;

import com.praktikum.semenov.intershop.dto.ItemDto;
import com.praktikum.semenov.intershop.service.CartService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@WebFluxTest(CartController.class)
class CartControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private CartService cartService;

    @Test
    void getCartItems_ShouldReturnCartViewWithAttributes() {
        ItemDto item1 = new ItemDto(1L, "Item 1", "описание", "", 1, BigDecimal.ONE);
        ItemDto item2 = new ItemDto(1L, "Item 2", "описание 2", "", 1, BigDecimal.TWO);
        List<ItemDto> mockItems = List.of(item1, item2);
        BigDecimal mockTotal = BigDecimal.valueOf(300);
        boolean mockEmpty = false;

        Mockito.when(cartService.getAllCartItems()).thenReturn(Mono.just(mockItems));
        Mockito.when(cartService.getTotalPrice(any(Mono.class))).thenReturn(Mono.just(mockTotal));
        Mockito.when(cartService.isCartEmpty()).thenReturn(Mono.just(mockEmpty));


        webTestClient.get()
                .uri("/cart/items")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .consumeWith(result -> {
                    String html = result.getResponseBody();
                    assert html != null;
                    assert html.contains("Item 1");
                    assert html.contains("3");
                });
    }

    @Test
    void changeItemCount_ShouldRedirectOnValidAction() {
        Mockito.when(cartService.changeItemCount(1L, "plus"))
                .thenReturn(Mono.empty());

        webTestClient.post()
                .uri("/cart/items/1?action=plus")
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueEquals("Location", "/main/items");
    }

    @Test
    void changeItemCount_ShouldReturnBadRequestOnInvalidAction() {
        webTestClient.post()
                .uri("/cart/items/1?action=invalid")
                .exchange()
                .expectStatus().is5xxServerError();
    }
}
