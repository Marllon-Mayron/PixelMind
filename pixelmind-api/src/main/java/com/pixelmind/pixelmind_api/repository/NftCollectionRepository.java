package com.pixelmind.pixelmind_api.repository;

import com.pixelmind.pixelmind_api.model.store.NftCollection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NftCollectionRepository extends JpaRepository<NftCollection, Long> {
}
