package com.cayi.mes.repository;

import com.cayi.mes.entity.Routing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoutingRepository extends JpaRepository<Routing, Long> {
    List<Routing> findByItemId(Long itemId);
    Optional<Routing> findByItemIdAndIsDefaultTrue(Long itemId);
    List<Routing> findByItemIdAndVersion(Long itemId, String version);
}