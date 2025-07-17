package com.pixelmind.pixelmind_api.repository;

import com.pixelmind.pixelmind_api.model.store.StoreItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreItemRepository extends JpaRepository<StoreItem, Long> {
    // Pode colocar métodos customizados se precisar buscar por status, promoção, etc
}
