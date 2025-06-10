package com.wazzups.btgpactual.controller;

import com.wazzups.btgpactual.entity.OrderEntity;
import com.wazzups.btgpactual.service.OrderQueryService;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class OrderController {

    private final OrderQueryService queryService;

    public OrderController(OrderQueryService queryService) {
        this.queryService = queryService;
    }

    @GetMapping("/customer/{customerId}/orders")
    public ResponseEntity<List<OrderEntity>> listOrders(@PathVariable(name = "customerId") Long customerId,
        @RequestParam(name = "page", defaultValue = "0") Integer page,
        @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {

        List<OrderEntity> orders = queryService.listOrdersForCustomer(customerId);

        return ResponseEntity.ok(orders);
    }

    @GetMapping("/orders/{orderId}/total")
    public ResponseEntity<Map<String, BigDecimal>> getOrderTotal(@PathVariable(name = "orderId") Long orderId) {
        BigDecimal total = queryService.getOrderTotal(orderId);
        return ResponseEntity.ok(Collections.singletonMap("total", total));
    }

    @GetMapping("/customers/{customerId}/orders/count")
    public ResponseEntity<Map<String, Long>> getOrdersCountByCustomer(@PathVariable(name = "customerId") Long customerId) {
        long orderCount = queryService.countOrdersForCustomer(customerId);
        return ResponseEntity.ok(Collections.singletonMap("count", orderCount));
    }
}
