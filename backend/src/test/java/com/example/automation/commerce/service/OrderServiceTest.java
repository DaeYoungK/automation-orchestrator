package com.example.automation.commerce.service;


import com.example.automation.commerce.domain.*;
import com.example.automation.commerce.dto.CreateOrderItemRequest;
import com.example.automation.commerce.dto.CreateOrderRequest;
import com.example.automation.commerce.dto.CreateProductRequest;
import com.example.automation.commerce.repository.OrderRepository;
import com.example.automation.commerce.repository.ProductRepository;
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
class OrderServiceTest {

    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;
    @Autowired ProductService productService;
    @Autowired ProductRepository productRepository;

    private List<CreateOrderItemRequest> orderItems = new ArrayList<>();

    @BeforeEach
    void init() {
        //give
        CreateProductRequest request1 = new CreateProductRequest("상품1", 1000, 10);
        CreateProductRequest request2 = new CreateProductRequest("상품2", 2000, 10);
        Long productId1 = productService.createProduct(request1);
        Long productId2 = productService.createProduct(request2);

        CreateOrderItemRequest orderItemRequest = new CreateOrderItemRequest(productId1, 1);
        CreateOrderItemRequest orderItemRequest2 = new CreateOrderItemRequest(productId2, 2);
        orderItems.add(orderItemRequest);
        orderItems.add(orderItemRequest2);
    }

    @Test
    public void shouldCreateOrder() {
        //given
        CreateOrderRequest OrderRequest = new CreateOrderRequest("주소", orderItems);

        //when
        Long orderId = orderService.createOrder(OrderRequest);
        Order order = orderRepository.findById(orderId).orElseThrow();

        //then
        assertThat(order).isNotNull();
        assertThat(order.getOrderItems()).allMatch(orderItem -> orderItem.getOrder().equals(order));
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.CREATED);
    }

    @Test
    public void 주문을_생성하면_재고가_줄고_예약재고가_오른다() {
        //given
        CreateOrderRequest OrderRequest = new CreateOrderRequest("주소", orderItems);

        //when
        Long orderId = orderService.createOrder(OrderRequest);
        Order order = orderRepository.findById(orderId).orElseThrow();

        //then


    }

    @Test
    public void shouldCancelOrder() {
        //given
        CreateOrderRequest OrderRequest = new CreateOrderRequest("주소", orderItems);
        Long orderId = orderService.createOrder(OrderRequest);
        Order order = orderRepository.findById(orderId).orElseThrow();

        //when
        orderService.cancelOrder(order.getId());

        //then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.CANCELED);
        assertThatThrownBy(() -> orderService.cancelOrder(order.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 수량이 올바르지 않습니다.");
    }

}