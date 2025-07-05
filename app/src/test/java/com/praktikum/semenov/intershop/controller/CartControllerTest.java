package com.praktikum.semenov.intershop.controller;

import com.praktikum.semenov.intershop.dto.ItemDto;
import com.praktikum.semenov.intershop.service.CartService;
import com.praktikum.semenov.intershop.service.PayService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebFluxTest(CartController.class)
class CartControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private CartService cartService;

    @MockBean
    private PayService payService;
//
    @Test
    void getCartItems_ShouldReturnCartViewWithAttributes() {
        ItemDto item1 = new ItemDto(1L, "Item 1", "описание", "", 1, BigDecimal.ONE);
        ItemDto item2 = new ItemDto(1L, "Item 2", "описание 2", "", 1, BigDecimal.TWO);
        List<ItemDto> mockItems = List.of(item1, item2);
        BigDecimal mockTotal = BigDecimal.valueOf(300);
        BigDecimal balance = BigDecimal.valueOf(5_000);
        boolean mockEmpty = false;

        when(cartService.getAllCartItems()).thenReturn(Mono.just(mockItems));
        when(cartService.getTotalPrice(any(Mono.class))).thenReturn(Mono.just(mockTotal));
        when(cartService.isCartEmpty()).thenReturn(Mono.just(mockEmpty));
        when(payService.checkBalance()).thenReturn(Mono.just(balance));


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
        when(cartService.changeItemCount(1L, "plus"))
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
