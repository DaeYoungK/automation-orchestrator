package com.example.automation.commerce.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class OrderTest {

    Order order = new Order();

    @BeforeEach
    void init() {
        List<OrderItem> orderItems = new ArrayList<>();
        Product product1 = Product.create("상품1", 1000);
        Product product2 = Product.create("상품2", 2000);
        OrderItem orderItem1 = OrderItem.create(product1, product1.getName(), product1.getPrice(), 1);
        OrderItem orderItem2 = OrderItem.create(product2, product2.getName(), product2.getPrice(), 2);

        orderItems.add(orderItem1);
        orderItems.add(orderItem2);

        order = Order.create(orderItems, "배달주소");
    }

    @Test
    void create() {

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
        order.cancel();
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.CANCELED);
    }

    @Test
    void cancelException() {
        order.cancel();

        assertThatThrownBy(() -> order.cancel())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 취소된 주문입니다.");
    }

    @Test
    void pay_호출_시_PAID_된다() throws Exception {
        //given

        //when
        order.pay();

        //then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.PAID);
    }

    @Test
    void complete_호출_시_COMPLETE_된다() throws Exception {
        //given

        //when
        order.pay();
        order.complete();

        //then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COMPLETED);
    }

    @Test
    void CANCELED_상태에서는_pay_할_수_없다() throws Exception {
        //given

        //when
        order.cancel();

        //then
        assertThatThrownBy(() -> order.pay())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 생성 상태에서만 결제할 수 있습니다.");
    }

    @Test
    void CREATE_상태에서는_complete_할_수_없다() throws Exception {
        //given

        //when

        //then
        assertThatThrownBy(() -> order.complete())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("결제 완료 상태에서만 처리할 수 있습니다.");
    }


}