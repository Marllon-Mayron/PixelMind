package com.pixelmind.pixelmind_api.repository;

import com.pixelmind.pixelmind_api.model.store.StorePromotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StorePromotionRepository extends JpaRepository<StorePromotion, Long> {
    // Pode criar métodos customizados como findByActiveTrue()
}
