package com.pixelmind.pixelmind_api.repository;

import com.pixelmind.pixelmind_api.dto.NftItemWithDateDTO;
import com.pixelmind.pixelmind_api.model.User;
import com.pixelmind.pixelmind_api.model.store.NftItem;
import com.pixelmind.pixelmind_api.model.store.UserNft;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserNftRepository extends JpaRepository<UserNft, Long> {
    @Query("SELECT u FROM UserNft u WHERE u.user.id = :userId")
    List<UserNft> findAllByUserIdWithNft(@Param("userId") Long userId);
    boolean existsByUserAndNftItem(User user, NftItem nftItem);
}
