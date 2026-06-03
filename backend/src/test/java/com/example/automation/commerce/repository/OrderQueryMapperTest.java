package com.example.automation.commerce.repository;

import com.example.automation.commerce.domain.Order;
import com.example.automation.commerce.domain.OrderItem;
import com.example.automation.commerce.domain.OrderStatus;
import com.example.automation.commerce.domain.Product;
import com.example.automation.commerce.dto.OrderSummaryResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderQueryMapperTest {

    @Autowired ProductRepository productRepository;
    @Autowired OrderRepository orderRepository;
    @Autowired OrderQueryMapper orderQueryMapper;
    Order order;

    @BeforeEach
    void createOrder() {
        List<OrderItem> orderItems = new ArrayList<>();
        Product product1 = Product.create("상품1", 1000);
        Product product2 = Product.create("상품2", 2000);
        OrderItem orderItem1 = OrderItem.create(product1, product1.getName(), product1.getPrice(), 1);
        OrderItem orderItem2 = OrderItem.create(product2, product2.getName(), product2.getPrice(), 1);
        orderItems.add(orderItem1);
        orderItems.add(orderItem2);

        order = Order.create(orderItems, "주소1");
        productRepository.save(product1);
        productRepository.save(product2);
        orderRepository.save(order);
    }

    @Test
    void 주문을_조회하면_건수_총금액_주소_주문상태가_정상적으로_출력된다() throws Exception {
        //given

        //when
        List<OrderSummaryResponse> orderSummaries = orderQueryMapper.findOrderSummaries();

        //then
        assertThat(orderSummaries).hasSize(1);

        OrderSummaryResponse orderSummaryResponse = orderSummaries.get(0);

        assertThat(orderSummaryResponse.getOrderStatus()).isEqualTo(OrderStatus.CREATED);
        assertThat(orderSummaryResponse.getAddress()).isEqualTo("주소1");
        assertThat(orderSummaryResponse.getTotalPrice()).isEqualTo(3000);
    }
}