package com.wazzups.btgpactual.controller;

import com.wazzups.btgpactual.entity.OrderEntity;
import com.wazzups.btgpactual.service.OrderQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Orders", description = "Operations related to order retrieval and metrics")
@Slf4j
@RestController()
@RequestMapping("/api")
public class OrderController {

    private final OrderQueryService queryService;

    public OrderController(OrderQueryService queryService) {
        this.queryService = queryService;
    }

    @Operation(
        summary = "Get a list of orders for a customer",
        description = "Returns the orders for a specific customer by ID"
    )
    @GetMapping("/customer/{customerId}/orders")
    public ResponseEntity<List<OrderEntity>> listOrders(@PathVariable(name = "customerId") Long customerId) {

        List<OrderEntity> orders = queryService.listOrdersForCustomer(customerId);

        return ResponseEntity.ok(orders);
    }

    @Operation(
        summary = "Get total for an order",
        description = "Returns the total amount for the specified order ID"
    )
    @GetMapping("/orders/{orderId}/total")
    public ResponseEntity<Map<String, BigDecimal>> getOrderTotal(@PathVariable(name = "orderId") Long orderId) {
        BigDecimal total = queryService.getOrderTotal(orderId);
        return ResponseEntity.ok(Collections.singletonMap("total", total));
    }

    @Operation(
        summary = "Get order count for a customer",
        description = "Returns the number of orders fora customer"
    )
    @GetMapping("/customers/{customerId}/orders/count")
    public ResponseEntity<Map<String, Long>> getOrdersCountByCustomer(@PathVariable(name = "customerId") Long customerId) {
        long orderCount = queryService.countOrdersForCustomer(customerId);
        return ResponseEntity.ok(Collections.singletonMap("count", orderCount));
    }
}
