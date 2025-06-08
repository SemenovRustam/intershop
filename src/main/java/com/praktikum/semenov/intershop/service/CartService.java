package com.praktikum.semenov.intershop.service;

import org.springframework.stereotype.Service;

@Service
public class CartService {
    public void addItem(Long id, int quantity) { /* ... */ }
    public void removeItem(Long id, int quantity) { /* ... */ }
    public void removeItemCompletely(Long id) { /* ... */ }
}
