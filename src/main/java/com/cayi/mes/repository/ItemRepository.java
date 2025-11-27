package com.cayi.mes.repository;

import com.cayi.mes.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    Optional<Item> findByCode(String code);
    List<Item> findByActiveTrue();
    List<Item> findByType(Item.ItemType type);
    boolean existsByCode(String code);
}