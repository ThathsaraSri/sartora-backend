package com.sartora.Sartora_backend.service;

import com.sartora.Sartora_backend.entity.Product;
import com.sartora.Sartora_backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public List<Product> getProducts() {
        return productRepository.findAll();
    }
}
