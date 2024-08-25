package com.sartora.Sartora_backend.controller.order;

import com.sartora.Sartora_backend.entity.LocalUser;
import com.sartora.Sartora_backend.entity.WebOrder;
import com.sartora.Sartora_backend.service.WebOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private WebOrderService webOrderService;

    @GetMapping
    public List<WebOrder> getOrders(@AuthenticationPrincipal LocalUser user) {
        return webOrderService.getOrders(user);
    }

}
