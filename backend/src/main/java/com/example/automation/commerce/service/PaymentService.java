package com.example.automation.commerce.service;

import com.example.automation.commerce.domain.Order;
import com.example.automation.commerce.domain.Payment;
import com.example.automation.commerce.repository.OrderRepository;
import com.example.automation.commerce.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PaymentService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;

    @Transactional
    public Long createPayment(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("주문이 존재하지 않습니다."));

        if (order.getPayment() != null) {
            throw new IllegalArgumentException("이미 결제가 생성되었습니다.");
        }

        Payment payment = Payment.create(order);

        paymentRepository.save(payment);

        return payment.getId();
    }

    @Transactional
    public Long successPayment(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId).orElseThrow(() -> new IllegalArgumentException("결제가 존재하지 않습니다."));
        Order order = payment.getOrder();

        payment.success();
        order.pay();

        order.getOrderItems().stream().forEach(item ->
                item.getProduct().getInventory().decrease(item.getQuantity())
        );

        return payment.getId();
    }

}
