package com.praktikum.semenov.intershop.controller;

import com.praktikum.semenov.intershop.entity.Order;
import com.praktikum.semenov.intershop.service.OrderService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/buy")
    public RedirectView createOrder() {
        Order order = orderService.createOrder();
        String redirectUrl = String.format("/orders/%d?newOrder=true", order.getId());
        return new RedirectView(redirectUrl);
    }

    @GetMapping("/orders")
    public String getOrders(Model model) {
        List<Order> allOrders = orderService.getAllOrders();

        model.addAttribute("orders", allOrders);
        return "orders";
    }

    @GetMapping("/orders/{id}")
    public String getOrderInfo(
            @PathVariable Long id,
            Model model,
            @RequestParam(required = false, defaultValue = "false") Boolean newOrder
    ) {
        Order order = orderService.findById(id);

        model.addAttribute("order", order);
        model.addAttribute("newOrder", newOrder);

        return "order";
    }
}
