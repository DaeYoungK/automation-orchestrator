package com.example.automation.commerce.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class PaymentTest {

    private Order order;

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
    void 결제_생성시_PENDING_상태다() throws Exception {
        //given

        //when
        Payment payment = Payment.create(order);

        //then
        assertThat(payment.getPaymentStatus()).isEqualTo(PaymentStatus.PENDING);
    }

    @Test
    void success_호출_시_SUCCESS_되고_paidAt_저장된다() throws Exception {
        //given
        Payment payment = Payment.create(order);
        //when

        payment.success();

        //then
        assertThat(payment.getPaymentStatus()).isEqualTo(PaymentStatus.SUCCESS);
        assertThat(payment.getPaidAt()).isNotNull();
    }

    @Test
    void fail_호출_시_FAILED_되고_사유_저장된다() throws Exception {
        //given
        Payment payment = Payment.create(order);
        String failedReason = "결제실패!";

        //when
        payment.fail(failedReason);

        //then
        assertThat(payment.getPaymentStatus()).isEqualTo(PaymentStatus.FAILED);
        assertThat(payment.getFailedReason()).isEqualTo(failedReason);
    }

    @Test
    void cancel_호출_시_CANCELED_된다() throws Exception {
        //given
        Payment payment = Payment.create(order);

        //when
        payment.cancel();

        //then
        assertThat(payment.getPaymentStatus()).isEqualTo(PaymentStatus.CANCELED);
    }

}