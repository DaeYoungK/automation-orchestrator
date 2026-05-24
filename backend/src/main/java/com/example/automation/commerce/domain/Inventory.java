package com.example.automation.commerce.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "inventory")
@NoArgsConstructor
@Getter
public class Inventory {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inventory_id")
    private Long id;

    private long availableQuantity;
    private long reservedQuantity;

    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;

    protected Inventory(Product product, long availableQuantity) {
        this.product = product;
        this.availableQuantity = availableQuantity;
    }

    public void mapProduct(Product product) {
        product.assignInventory(this);
    }

    public static Inventory create(Product product, long availableQuantity) {
        validateCreatable(availableQuantity);

        Inventory inventory = new Inventory(product, availableQuantity);
        inventory.mapProduct(product);
        inventory.reservedQuantity = 0;

        if (availableQuantity > 0) {
            product.changeToOnSale();
        } else {
            product.changeToPreparation();
        }
        
        return inventory;
    }

    private static void validateCreatable(long availableQuantity) {
        if (availableQuantity < 0) {
            throw new IllegalArgumentException("재고는 음수일 수 없습니다.");
        }
    }

    public void increase(long quantity) {
        this.availableQuantity += quantity;
        product.changeToOnSale();
    }

    public void reserve(long quantity) {
        validateReservable(quantity);
        this.availableQuantity -= quantity;
        this.reservedQuantity += quantity;
    }

    private void validateReservable(long quantity) {
        if (this.availableQuantity < quantity) {
            throw new IllegalArgumentException("주문 가능한 수량이 부족합니다.");
        }
    }

    public void release(long quantity) {
        validateReleasable(quantity);
        this.availableQuantity += quantity;
        this.reservedQuantity -= quantity;
    }

    private void validateReleasable(long quantity) {
        if (this.reservedQuantity < quantity) {
            throw new IllegalArgumentException("주문 수량이 올바르지 않습니다.");
        }
    }

    public void decrease(long quantity) {
        validateDecreasable(quantity);
        this.reservedQuantity -= quantity;
        if (this.availableQuantity == 0
           && this.reservedQuantity == 0) {
            this.product.changeToSoldOut();
        }
    }

    private void validateDecreasable(long quantity) {
        if (this.reservedQuantity < quantity) {
            throw new IllegalArgumentException("주문 가능한 수량이 부족합니다.");
        }
    }
}
