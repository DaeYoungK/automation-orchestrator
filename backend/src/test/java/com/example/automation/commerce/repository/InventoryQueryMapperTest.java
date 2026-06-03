package com.example.automation.commerce.repository;

import com.example.automation.commerce.domain.Inventory;
import com.example.automation.commerce.domain.Product;
import com.example.automation.commerce.domain.ProductStatus;
import com.example.automation.commerce.dto.LowStockProductResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class InventoryQueryMapperTest {

    @Autowired InventoryQueryMapper inventoryQueryMapper;
    @Autowired ProductRepository productRepository;
    @Autowired InventoryRepository inventoryRepository;

    private void saveProductWithInventory(Product product, Inventory inventory) {
        productRepository.save(product);
        inventoryRepository.save(inventory);
    }

    @Test
    void 재고_부족_상품만_조회된다() throws Exception {
        //given
        Product product1 = Product.create("상품1", 1000);
        Product product2 = Product.create("상품2", 2000);
        Product product3 = Product.create("상품3", 3000);

        Inventory inventory1 = Inventory.create(product1, 10);
        Inventory inventory2 = Inventory.create(product2, 20);
        Inventory inventory3 = Inventory.create(product3, 30);

        saveProductWithInventory(product1, inventory1);
        saveProductWithInventory(product2, inventory2);
        saveProductWithInventory(product3, inventory3);

        inventory1.reserve(1);
        inventory1.decrease(1);
        inventory2.reserve(16);
        inventory2.decrease(16);
        inventory3.reserve(26);
        inventory3.decrease(26);

        productRepository.flush();
        inventoryRepository.flush();

        //when
        List<LowStockProductResponse> lowStockProducts = inventoryQueryMapper.findLowStockProducts(5);

        //then
        assertThat(lowStockProducts).hasSize(2);
        assertThat(lowStockProducts).allMatch(product -> product.getAvailableQuantity() <= 5);
    }

    @Test
    void SOLD_OUT_상품도_조회된다() throws Exception {
        //given
        Product product1 = Product.create("상품1", 1000);
        Product product2 = Product.create("상품2", 2000);
        Product product3 = Product.create("상품3", 3000);

        Inventory inventory1 = Inventory.create(product1, 10);
        Inventory inventory2 = Inventory.create(product2, 20);
        Inventory inventory3 = Inventory.create(product3, 30);

        saveProductWithInventory(product1, inventory1);
        saveProductWithInventory(product2, inventory2);
        saveProductWithInventory(product3, inventory3);

        inventory1.reserve(10);
        inventory1.decrease(10);
        inventory2.reserve(16);
        inventory2.decrease(16);
        inventory3.reserve(26);
        inventory3.decrease(26);

        productRepository.flush();
        inventoryRepository.flush();

        //when
        List<LowStockProductResponse> lowStockProducts = inventoryQueryMapper.findLowStockProducts(5);

        //then
        assertThat(lowStockProducts).hasSize(3);
        assertThat(lowStockProducts)
                .extracting(LowStockProductResponse::getProductStatus)
                .contains(ProductStatus.SOLD_OUT);
    }
}