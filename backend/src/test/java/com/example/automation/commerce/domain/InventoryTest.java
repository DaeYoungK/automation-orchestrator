package com.example.automation.commerce.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class InventoryTest {

    Product product;

    @BeforeEach
    void init() {
        product = Product.create("상품", 10000);
    }

    @Test
    void createInventory() {
        Inventory inventory = Inventory.create(product, 10);

        assertThat(inventory.getProduct()).isEqualTo(product);
        assertThat(inventory.getProduct().getProductStatus()).isEqualTo(ProductStatus.ON_SALE);
    }

    @Test
    void reservedInventory() {
        Inventory inventory = Inventory.create(product, 10);

        inventory.reserve(1);

        assertThat(inventory.getAvailableQuantity()).isEqualTo(9);
        assertThat(inventory.getReservedQuantity()).isEqualTo(1);
        assertThatThrownBy(() -> inventory.reserve(11))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 가능한 수량이 부족합니다.");
    }

    @Test
    void releaseInventory() {
        Inventory inventory = Inventory.create(product, 10);
        inventory.reserve(1);

        inventory.release(1);

        assertThat(inventory.getReservedQuantity()).isEqualTo(0);
        assertThat(inventory.getAvailableQuantity()).isEqualTo(10);
        assertThatThrownBy(() -> inventory.release(100))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 수량이 올바르지 않습니다.");
    }

    @Test
    void 재고를_남은수량보다_많이_감소시키면_예외가_발생한다() {
        Inventory inventory = Inventory.create(product, 10);
        inventory.reserve(1);

        inventory.decrease(1);

        assertThat(inventory.getReservedQuantity()).isEqualTo(0);
        assertThat(inventory.getAvailableQuantity()).isEqualTo(9);
        assertThatThrownBy(() -> inventory.decrease(100))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 가능한 수량이 부족합니다.");
    }

    @Test
    void 재고가_모두_소진되면_상품은_품절상태가_된다() {
        Inventory inventory = Inventory.create(product, 10);
        inventory.reserve(10);
        inventory.decrease(10);

        assertThat(inventory.getProduct().getProductStatus()).isEqualTo(ProductStatus.SOLD_OUT);
    }

    @Test
    void 재고가_추가되면_상품은_판매상태가_된다() {
        Inventory inventory = Inventory.create(product, 0);
        inventory.increase(10);

        assertThat(inventory.getProduct().getProductStatus()).isEqualTo(ProductStatus.ON_SALE);
    }
}