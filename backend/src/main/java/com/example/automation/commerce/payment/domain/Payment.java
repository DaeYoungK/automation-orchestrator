package com.example.automation.commerce.payment.domain;

import com.example.automation.commerce.order.domain.Order;
import com.example.automation.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "payment")
@NoArgsConstructor
@Getter
public class Payment extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @Enumerated(value = EnumType.STRING)
    private PaymentStatus paymentStatus;

    private int amount;

    private LocalDateTime paidAt;

    private String failedReason;

    protected Payment(Order order) {
        this.order = order;
        this.amount = order.calculateTotalPrice();
        this.paymentStatus = PaymentStatus.PENDING;
    }

    public void mapOrder(Order order) {
        order.assignPayment(this);
    }

    public static Payment create(Order order) {
        Payment payment = new Payment(order);
        payment.mapOrder(order);

        return payment;
    }

    public void success() {
        validatePending();
        this.paymentStatus = PaymentStatus.SUCCESS;
        this.paidAt = LocalDateTime.now();
    }

    public void cancel() {
        validatePending();
        this.paymentStatus = PaymentStatus.CANCELED;
    }

    public void fail(String reason) {
        validatePending();
        this.paymentStatus = PaymentStatus.FAILED;
        this.failedReason = reason;
    }

    private void validatePending() {
        if (this.paymentStatus != PaymentStatus.PENDING) {
            throw new IllegalArgumentException("결제 대기 상태에서만 처리할 수 있습니다.");
        }
    }
}
