package com.sartora.Sartora_backend.repository;

import com.sartora.Sartora_backend.entity.Product;
import org.springframework.data.repository.ListCrudRepository;

public interface ProductRepository extends ListCrudRepository<Product, Long> {
}
