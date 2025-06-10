package com.wazzups.btgpactual.listener;

import com.wazzups.btgpactual.dto.OrderEvent;
import com.wazzups.btgpactual.service.OrderProcessingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderListener {

    private final OrderProcessingService orderProcessingService;

    public OrderListener(OrderProcessingService orderProcessingService) {
        this.orderProcessingService = orderProcessingService;
    }

    @RabbitListener(queues = "${btg.rabbitmq.order-queue}")
    public void receiveOrder(Message<OrderEvent> message) {
        log.info("Message consumed: {}", message);
        orderProcessingService.processingOrder(message.getPayload());
    }
}
