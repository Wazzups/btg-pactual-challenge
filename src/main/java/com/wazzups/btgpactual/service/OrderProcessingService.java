package com.wazzups.btgpactual.service;

import com.wazzups.btgpactual.dto.OrderEvent;
import com.wazzups.btgpactual.entity.OrderEntity;
import com.wazzups.btgpactual.entity.OrderItem;
import com.wazzups.btgpactual.repository.OrderRepository;
import java.math.BigDecimal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderProcessingService {

    private final OrderRepository orderRepository;

    public OrderProcessingService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional
    public void processingOrder(OrderEvent orderEvent) {
        orderRepository.save(fromEvent(orderEvent));
    }

    public static OrderEntity fromEvent(OrderEvent orderEvent) {
        OrderEntity order = new OrderEntity();
        order.setOrderId(orderEvent.orderId());
        order.setCustomerId(orderEvent.clientId());
        order.setOrderItems(orderEvent.items().stream()
            .map(i -> new OrderItem(i.productId(), i.quantity(), i.unitPrice())).toList());
        order.setTotal(orderEvent.items().stream().map(i -> i.unitPrice().multiply(BigDecimal.valueOf(i.quantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add));

        return order;
    }
}
