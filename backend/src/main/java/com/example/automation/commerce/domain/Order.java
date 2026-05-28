package com.example.automation.commerce.domain;

import com.example.automation.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor
public class Order extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private String address; //배송 주소

    @OneToMany(mappedBy = "order", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(mappedBy = "order", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Payment payment;

    /**
     * 연관관계 편의 메서드
     **/
    public void assignPayment(Payment payment) {
        this.payment = payment;
    }

    public void addOrderItem(OrderItem orderItem) {
        this.orderItems.add(orderItem);
        orderItem.assignOrder(this);
    }

    public static Order create(List<OrderItem> orderItems, String address) {
        validateCreatable(orderItems);

        Order order = new Order();

        order.address = address;
        for (OrderItem item : orderItems) {
            order.addOrderItem(item);
        }

        order.orderStatus = OrderStatus.CREATED;

        return order;
    }

    public void cancel() {
        validateCancelable();
        this.orderStatus = OrderStatus.CANCELED;
    }

    private static void validateCreatable(List<OrderItem> items) {
        if ( items == null || items.isEmpty()) {
            throw new IllegalArgumentException("주문상품이 존재하지 않습니다.");
        }
    }

    private void validateCancelable() {
        if (this.orderStatus == OrderStatus.CANCELED) {
            throw new IllegalArgumentException("이미 취소된 주문입니다.");
        }

        // TODO: 2026-05-14
        //배송완료 후 취소불가 추후 추가 (배송상태 필드 추가시)
        //결제 완료 상태 정책 추후 추가 (결제 상태 필드 추가시)
        //환불 상태 추후 추가 (환불 상태 추후 추가시?)
    }

    public int calculateTotalPrice() {
        return orderItems.stream()
                .mapToInt(OrderItem::calculateTotalPrice)
                .sum();
    }


    public void pay() {
        validatePayable();
        this.orderStatus = OrderStatus.PAID;
    }

    private void validatePayable() {
        if (this.orderStatus != OrderStatus.CREATED) {
            throw new IllegalArgumentException("주문 생성 상태에서만 결제할 수 있습니다.");
        }
    }

    public void complete() {
        validatePaid();
        this.orderStatus = OrderStatus.COMPLETED;
    }

    private void validatePaid() {
        if (this.orderStatus != OrderStatus.PAID) {
            throw new IllegalArgumentException("결제 완료 상태에서만 처리할 수 있습니다.");
        }
    }


}
