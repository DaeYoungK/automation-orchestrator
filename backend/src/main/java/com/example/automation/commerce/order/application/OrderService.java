package com.example.automation.commerce.order.application;

import com.example.automation.commerce.order.domain.Order;
import com.example.automation.commerce.order.domain.OrderItem;
import com.example.automation.commerce.product.domain.Product;
import com.example.automation.commerce.order.dto.CreateOrderRequest;
import com.example.automation.commerce.order.repository.OrderRepository;
import com.example.automation.commerce.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Transactional
    public Long createOrder(CreateOrderRequest request) {
        List<OrderItem> orderItems = request.getItems().stream().map(item -> {
            Product product = productRepository.findById(item.getProductId()).orElseThrow(() -> new IllegalArgumentException("해당 상품이 존재하지 않습니다."));

            product.getInventory().reserve(item.getQuantity());
            return OrderItem.create(product, product.getName(), product.getPrice(), item.getQuantity());
        }).toList();

        Order order = Order.create(orderItems, request.getAddress());
        Order saveOrder = orderRepository.save(order);

        return saveOrder.getId();
    }

    @Transactional
    public Long cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("주문이 존재하지 않습니다."));
        order.getOrderItems().stream().forEach(item ->
                item.getProduct().getInventory().release(item.getQuantity())
        );
        order.cancel();

        return order.getId();
    }
}
