package com.praktikum.semenov.intershop.controller;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.praktikum.semenov.intershop.dto.CartAction;
import com.praktikum.semenov.intershop.dto.ItemDto;
import com.praktikum.semenov.intershop.service.CartService;
import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CartController.class)
class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartService cartService;

    private List<ItemDto> sampleItems;

    @BeforeEach
    void setUp() {
        // Пример данных
        ItemDto item1 = new ItemDto();
        item1.setId(1L);
        item1.setPrice(new BigDecimal("10.00"));
        sampleItems = List.of(item1);
    }

    @Test
    void getCartItems_ShouldReturnCartViewWithModel() throws Exception {
        when(cartService.getAllCartItems()).thenReturn(sampleItems);
        when(cartService.getTotalPrice(sampleItems)).thenReturn(new BigDecimal("10.00"));
        when(cartService.isCartEmpty()).thenReturn(false);

        mockMvc.perform(get("/cart/items"))
                .andExpect(status().isOk())
                .andExpect(view().name("cart"))
                .andExpect(model().attribute("items", sampleItems))
                .andExpect(model().attribute("total", new BigDecimal("10.00")))
                .andExpect(model().attribute("empty", false));

        verify(cartService).getAllCartItems();
        verify(cartService).getTotalPrice(sampleItems);
        verify(cartService).isCartEmpty();
    }

    @Test
    void changeItemCount_ShouldCallServiceAndRedirect() throws Exception {
        Long itemId = 5L;
        String action = "plus";

        mockMvc.perform(post("/cart/items/{itemId}", itemId)
                        .param("action", action))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/main/items/"));

        verify(cartService).changeItemCount(itemId, CartAction.valueOf(action.toUpperCase()));
    }
}
