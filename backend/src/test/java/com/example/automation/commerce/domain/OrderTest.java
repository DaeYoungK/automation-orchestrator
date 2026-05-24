package com.example.automation.commerce.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class OrderTest {

    private List<OrderItem> orderItems = new ArrayList<>();

    @BeforeEach
    void init() {
        Product product1 = Product.create("상품1", 1000);
        Product product2 = Product.create("상품2", 2000);
        OrderItem orderItem1 = OrderItem.create(product1, product1.getName(), product1.getPrice(), 1);
        OrderItem orderItem2 = OrderItem.create(product2, product2.getName(), product2.getPrice(), 2);

        orderItems.add(orderItem1);
        orderItems.add(orderItem2);
    }

    @Test
    void create() {
        Order order = Order.create(orderItems, "배달주소");

        assertThat(order.getAddress()).isEqualTo("배달주소");
        assertThat(order.getOrderItems())
                .hasSize(2)
                .extracting("orderProductName")
                .containsExactly("상품1", "상품2");
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.CREATED);
        assertThat(order.getOrderItems())
                .allMatch(orderItem -> orderItem.getOrder().equals(order));
        assertThat(order.getOrderItems())
                .extracting(OrderItem::getOrder)
                .containsOnly(order);
    }

    @Test
    void createException() {
        List<OrderItem> orderItems = new ArrayList<>();

        assertThatThrownBy(() -> Order.create(orderItems, "배달주소"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문상품이 존재하지 않습니다.");
    }

    @Test
    void cancel() {
        Order order = Order.create(orderItems, "배달주소");

        order.cancel();
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.CANCELED);
    }

    @Test
    void cancelException() {
        Order order = Order.create(orderItems, "배달주소");

        order.cancel();

        assertThatThrownBy(() -> order.cancel())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 취소된 주문입니다.");
    }
}