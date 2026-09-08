package com.example.automation.commerce.payment.application;

import com.example.automation.commerce.order.domain.Order;
import com.example.automation.commerce.order.domain.OrderStatus;
import com.example.automation.commerce.payment.application.PaymentService;
import com.example.automation.commerce.payment.domain.Payment;
import com.example.automation.commerce.payment.domain.PaymentStatus;
import com.example.automation.commerce.order.dto.CreateOrderItemRequest;
import com.example.automation.commerce.order.dto.CreateOrderRequest;
import com.example.automation.commerce.product.dto.CreateProductRequest;
import com.example.automation.commerce.order.application.OrderService;
import com.example.automation.commerce.order.repository.OrderRepository;
import com.example.automation.commerce.product.application.ProductService;
import com.example.automation.commerce.payment.repository.PaymentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class PaymentServiceTest {

    @Autowired
    ProductService productService;
    @Autowired
    PaymentService paymentService;
    @Autowired PaymentRepository paymentRepository;
    @Autowired OrderRepository orderRepository;
    @Autowired
    OrderService orderService;
    Order order;

    /**
     * 주문 생성
     */
    void createOrderFixture() {
        List<CreateOrderItemRequest> itemRequests = new ArrayList<>();

        Long productId = productService.createProduct(new CreateProductRequest("상품1", 1000, 10));
        CreateOrderItemRequest itemRequest = new CreateOrderItemRequest(productId, 3);
        itemRequests.add(itemRequest);


        Long orderId = orderService.createOrder(new CreateOrderRequest("주소", itemRequests));
        order = orderRepository.findById(orderId).orElseThrow();
    }

    @Test
    void 결제를_생성하면_PENDING_상태이고_주문과_연결된다() throws Exception {
        //given
        createOrderFixture();
        Long paymentId = paymentService.createPayment(order.getId());

        //when
        Payment payment = paymentRepository.findById(paymentId).orElseThrow();

        //then
        assertThat(payment.getPaymentStatus()).isEqualTo(PaymentStatus.PENDING);
        assertThat(payment.getOrder()).isEqualTo(order);
        assertThat(payment.getAmount()).isEqualTo(order.calculateTotalPrice());
    }

    @Test
    void 주문이_존재하지_않으면_결제도_생성되지_않는다() throws Exception {
        //given
        Long orderId = Long.MAX_VALUE;

        //when

        //then
        assertThatThrownBy(() -> paymentService.createPayment(orderId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문이 존재하지 않습니다.");
    }
    
    @Test
    void 결제가_완료되면_payment_상태는_SUCCESS_이고_order_상태는_PAID_이며_재고가_감소한다() throws Exception {
        //given
        createOrderFixture();
        Long paymentId = paymentService.createPayment(order.getId());

        //when
        Long savedPaymentId = paymentService.successPayment(paymentId);
        Payment payment = paymentRepository.findById(savedPaymentId).orElseThrow(() -> new IllegalArgumentException("결제 미존재"));

        //then
        assertThat(payment.getPaymentStatus()).isEqualTo(PaymentStatus.SUCCESS);
        assertThat(payment.getOrder().getOrderStatus()).isEqualTo(OrderStatus.PAID);
        assertThat(payment.getOrder().getOrderItems().get(0).getProduct().getInventory().getReservedQuantity()).isEqualTo(0);
        assertThat(payment.getOrder().getOrderItems().get(0).getProduct().getInventory().getAvailableQuantity()).isEqualTo(7);
    }

    @Test
    void 존재하지_않는_결제는_생성되지_않는다() throws Exception {
        //given
        Long notExistPaymentId = Long.MAX_VALUE;

        //when

        //then
        assertThatThrownBy(() -> paymentService.successPayment(notExistPaymentId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("결제가 존재하지 않습니다.");
    }

    @Test
    void 이미_결제가_성공했으면_재성공은_불가능하다() throws Exception {
        //given
        createOrderFixture();
        Long paymentId = paymentService.createPayment(order.getId());

        //when
        Long savedPaymentId = paymentService.successPayment(paymentId);

        //then
        assertThatThrownBy(() -> paymentService.successPayment(savedPaymentId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("결제 대기 상태에서만 처리할 수 있습니다.");
    }

    @Test
    void 이미_결제가_생성됐으면_중복생성은_불가능하다() throws Exception {
        //given
        createOrderFixture();
        paymentService.createPayment(order.getId());

        //when

        //then
        assertThatThrownBy(() -> paymentService.createPayment(order.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 결제가 생성되었습니다.");
    }
}