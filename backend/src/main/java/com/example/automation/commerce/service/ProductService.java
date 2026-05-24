package com.example.automation.commerce.service;

import com.example.automation.commerce.domain.Inventory;
import com.example.automation.commerce.domain.Product;
import com.example.automation.commerce.dto.CreateProductRequest;
import com.example.automation.commerce.repository.InventoryRepository;
import com.example.automation.commerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;

    @Transactional
    public Long createProduct(CreateProductRequest request) {
        Product product = Product.create(request.getName(), request.getPrice());
        Inventory inventory = Inventory.create(product, request.getQuantity());

        productRepository.save(product);
        inventoryRepository.save(inventory);

        return product.getId();
    }

}
