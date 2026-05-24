package com.example.automation.commerce.service;

import com.example.automation.commerce.domain.Inventory;
import com.example.automation.commerce.domain.Product;
import com.example.automation.commerce.dto.CreateProductRequest;
import com.example.automation.commerce.repository.InventoryRepository;
import com.example.automation.commerce.repository.ProductRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ProductServiceTest {

    @Autowired ProductService productService;
    @Autowired ProductRepository productRepository;

    CreateProductRequest request;

    @BeforeEach
    void init() {
        request = new CreateProductRequest("상품1", 1000, 5);
    }

    @Test
    void 상품을_생성하면_인벤토리_생성() {
        //given

        //when
        Long productId = productService.createProduct(request);
        Product findProduct = productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다."));
        Inventory inventory = findProduct.getInventory();

        //then
        assertThat(inventory.getAvailableQuantity()).isEqualTo(5);
        assertThat(inventory.getReservedQuantity()).isEqualTo(0);
    }
}