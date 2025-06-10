package com.wazzups.btgpactual.integration;

import com.wazzups.btgpactual.dto.OrderEvent;
import com.wazzups.btgpactual.entity.OrderEntity;
import com.wazzups.btgpactual.repository.OrderRepository;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

@Testcontainers
@SpringBootTest
public class OrderTest {

    @Container
    static MongoDBContainer mongo = new MongoDBContainer("mongo:6.0");

    @Container
    static RabbitMQContainer rabbit = new RabbitMQContainer("rabbitmq:3-management");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongo::getReplicaSetUrl);
        registry.add("spring.rabbitmq.addresses", rabbit::getAmqpUrl);
        registry.add("btg.rabbitmq.order-queue", () -> "btg.pactual.orders.queue");
    }

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private OrderRepository orderRepository;

    @BeforeEach
    void cleanup() {
        orderRepository.deleteAll();
    }

    @Test
    void whenOrderMessageSent_thenOrderIsSaved() {
        OrderEvent.OrderItem item1 = new OrderEvent.OrderItem("pen", 2, BigDecimal.valueOf(10.5));
        OrderEvent.OrderItem item2 = new OrderEvent.OrderItem("book", 1, BigDecimal.valueOf(8));
        OrderEvent order = new OrderEvent(1L, 103L, List.of(item1, item2));

        rabbitTemplate.convertAndSend("btg.pactual.orders.queue", order);

        await().atMost(Duration.ofSeconds(5))
            .untilAsserted(() -> {
                Optional<OrderEntity> saved = orderRepository.findById(1L);
                assertThat(saved).isPresent();
                OrderEntity o = saved.get();
                assertThat(o.getCustomerId()).isEqualTo(103L);
                assertThat(o.getTotal()).isEqualByComparingTo(BigDecimal.valueOf((10.5 * 2) + 8));
            });
    }
}
