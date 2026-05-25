package com.example.automation.commerce.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "order_item")
@Getter
@NoArgsConstructor
public class OrderItem {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderProductName;
    private int orderPrice;
    private int quantity;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    protected void assignOrder(Order order) {
        this.order = order;
    }

    protected OrderItem(Product product, String orderProductName, int orderPrice, int quantity) {
        this.product = product;
        this.orderProductName = orderProductName;
        this.orderPrice = orderPrice;
        this.quantity = quantity;
    }

    public static OrderItem create(Product product, String orderProductName, int orderPrice, int quantity) {
        return new OrderItem(product, orderProductName, orderPrice, quantity);
    }

    public int calculateTotalPrice() {
        return orderPrice * quantity;
    }
}
