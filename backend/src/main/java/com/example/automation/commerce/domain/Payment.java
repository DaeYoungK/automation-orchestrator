package com.example.automation.commerce.domain;

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

    public void assignOrder(Order order) {
        order.assignPayment(this);
    }

    public static Payment create(Order order) {
        Payment payment = new Payment(order);
        payment.assignOrder(order);

        return payment;
    }

    public void success() {
        if (this.paymentStatus == PaymentStatus.PENDING) {
            this.paymentStatus = PaymentStatus.SUCCESS;
        }
        this.paidAt = LocalDateTime.now();
    }

    public void cancel() {
        if (this.paymentStatus != PaymentStatus.SUCCESS) {
            this.paymentStatus = PaymentStatus.CANCELED;
        }
    }

    public void fail(String reason) {
        if (this.paymentStatus == PaymentStatus.PENDING) {
            this.paymentStatus = PaymentStatus.FAILED;
            this.failedReason = reason;
        }
    }

}
