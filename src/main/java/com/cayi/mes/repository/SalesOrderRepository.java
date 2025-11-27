package com.cayi.mes.repository;

import com.cayi.mes.entity.SalesOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SalesOrderRepository extends JpaRepository<SalesOrder, Long> {
    Optional<SalesOrder> findByOrderNumber(String orderNumber);
    List<SalesOrder> findByStatus(String status);
    List<SalesOrder> findByItemId(Long itemId);
    boolean existsByOrderNumber(String orderNumber);
}