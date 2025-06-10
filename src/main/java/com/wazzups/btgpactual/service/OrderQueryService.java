package com.wazzups.btgpactual.service;

import com.wazzups.btgpactual.entity.OrderEntity;
import com.wazzups.btgpactual.repository.OrderRepository;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class OrderQueryService {

    private final OrderRepository orderRepository;

    public OrderQueryService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public BigDecimal getOrderTotal(Long orderId) {
        return orderRepository.findById(orderId)
            .map(OrderEntity::getTotal)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Not found orderId: " + orderId
            ));
    }

    public long countOrdersForCustomer(Long customerId) {
        return orderRepository.countByCustomerId(customerId);
    }

    public List<OrderEntity> listOrdersForCustomer(Long customerId) {
        return orderRepository.findAllByCustomerId(customerId);
    }

}
