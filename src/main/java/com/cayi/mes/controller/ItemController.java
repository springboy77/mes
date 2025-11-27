package com.cayi.mes.controller;

import com.cayi.mes.entity.Item;
import com.cayi.mes.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/items")
public class ItemController {
    @Autowired
    private ItemRepository itemRepository;

    @GetMapping
    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Item> getItemById(@PathVariable Long id) {
        Optional<Item> item = itemRepository.findById(id);
        return item.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Item createItem(@RequestBody Item item) {
        return itemRepository.save(item);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Item> updateItem(@PathVariable Long id, @RequestBody Item itemDetails) {
        Optional<Item> optionalItem = itemRepository.findById(id);
        if (optionalItem.isPresent()) {
            Item item = optionalItem.get();
            item.setCode(itemDetails.getCode());
            item.setName(itemDetails.getName());
            item.setType(itemDetails.getType());
            item.setOnHandQty(itemDetails.getOnHandQty());
            item.setDescription(itemDetails.getDescription());
            item.setActive(itemDetails.getActive());
            return ResponseEntity.ok(itemRepository.save(item));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        if (itemRepository.existsById(id)) {
            itemRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<Item> getItemByCode(@PathVariable String code) {
        Optional<Item> item = itemRepository.findByCode(code);
        return item.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/active")
    public List<Item> getActiveItems() {
        return itemRepository.findByActiveTrue();
    }
}