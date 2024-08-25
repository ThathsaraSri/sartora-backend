package com.sartora.Sartora_backend.service;

import com.sartora.Sartora_backend.entity.LocalUser;
import com.sartora.Sartora_backend.entity.WebOrder;
import com.sartora.Sartora_backend.repository.WebOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WebOrderService {
    @Autowired
    private WebOrderRepository webOrderRepository;

    public List<WebOrder> getOrders(LocalUser user) {
       return webOrderRepository.findByUser(user);
    }
}
