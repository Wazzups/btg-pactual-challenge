package com.wazzups.btgpactual.repository;

import com.wazzups.btgpactual.entity.OrderEntity;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderRepository extends MongoRepository<OrderEntity, Long> {
    List<OrderEntity> findAllByCustomerId(Long customerId);
    long countByCustomerId(Long customerId);
}
