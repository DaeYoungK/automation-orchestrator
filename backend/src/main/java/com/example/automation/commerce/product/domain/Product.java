package com.example.automation.commerce.product.domain;

import com.example.automation.commerce.inventory.domain.Inventory;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "product")
public class Product {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    private String name;

    private int price;

    @Enumerated(value = EnumType.STRING)
    private ProductStatus productStatus;

    @OneToOne(mappedBy = "product", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    private Inventory inventory;

    public void assignInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    protected Product(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public static Product create(String name, int price) {
        Product product = new Product(name, price);
        product.productStatus = ProductStatus.PREPARATION;

        return product;
    }

    public void changeToOnSale() {
        if (this.productStatus != ProductStatus.ON_SALE) {
            this.productStatus = ProductStatus.ON_SALE;
        }
    }

    public void changeToSoldOut() {
        if (this.productStatus != ProductStatus.SOLD_OUT) {
            this.productStatus = ProductStatus.SOLD_OUT;
        }
    }

    public void changeToPreparation() {
        if (this.productStatus != ProductStatus.PREPARATION) {
            this.productStatus = ProductStatus.PREPARATION;
        }
    }
}
