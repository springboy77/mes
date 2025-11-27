package com.cayi.mes.repository;

import com.cayi.mes.entity.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OperationRepository extends JpaRepository<Operation, Long> {
    List<Operation> findByRoutingIdOrderByStep(Long routingId);
    List<Operation> findByWorkCenterId(Long workCenterId);
}