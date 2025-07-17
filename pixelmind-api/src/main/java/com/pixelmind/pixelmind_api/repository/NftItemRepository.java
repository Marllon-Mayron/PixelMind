package com.pixelmind.pixelmind_api.repository;

import com.pixelmind.pixelmind_api.model.store.NftItem;
import com.pixelmind.pixelmind_api.model.store.UserNft;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NftItemRepository extends JpaRepository<NftItem, Long> {
    List<NftItem> findByForSale(boolean forSale);
}
