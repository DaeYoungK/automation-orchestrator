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

    // TODO: 2026-05-14
    // pay() 상태
    // complete() 상태

}
