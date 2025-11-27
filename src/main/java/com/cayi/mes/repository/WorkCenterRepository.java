package com.cayi.mes.repository;

import com.cayi.mes.entity.WorkCenter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkCenterRepository extends JpaRepository<WorkCenter, Long> {
    Optional<WorkCenter> findByCode(String code);
    List<WorkCenter> findByActiveTrue();
    boolean existsByCode(String code);
}