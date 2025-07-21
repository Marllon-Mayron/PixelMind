package com.pixelmind.pixelmind_api.controller;

import com.pixelmind.pixelmind_api.dto.store.StoreItemDTO;
import com.pixelmind.pixelmind_api.dto.store.StorePromotionDTO;
import com.pixelmind.pixelmind_api.service.StoreService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/store")
public class StoreController {

    private final StoreService storeService;

    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    // Listar itens
    @GetMapping("/items")
    public ResponseEntity<List<StoreItemDTO>> getAllItems() {
        return ResponseEntity.ok(storeService.listStoreItems());
    }

    // Criar/Atualizar item
    @PostMapping("/items")
    public ResponseEntity<StoreItemDTO> saveItem(@Valid @RequestBody StoreItemDTO dto) {
        return ResponseEntity.ok(storeService.saveStoreItem(dto));
    }

    // Listar promoções
    @GetMapping("/promotions")
    public ResponseEntity<List<StorePromotionDTO>> getAllPromotions() {
        return ResponseEntity.ok(storeService.listStorePromotions());
    }

    // Criar/Atualizar promoção
    @PostMapping("/promotions")
    public ResponseEntity<StorePromotionDTO> savePromotion(@Valid @RequestBody StorePromotionDTO dto) {
        return ResponseEntity.ok(storeService.saveStorePromotion(dto));
    }

    // Deletar item da loja
    @DeleteMapping("/items/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        storeService.deleteStoreItem(id);
        return ResponseEntity.noContent().build();
    }

    // Deletar promoção da loja
    @DeleteMapping("/promotions/{id}")
    public ResponseEntity<Void> deletePromotion(@PathVariable Long id) {
        storeService.deleteStorePromotion(id);
        return ResponseEntity.noContent().build();
    }
}
