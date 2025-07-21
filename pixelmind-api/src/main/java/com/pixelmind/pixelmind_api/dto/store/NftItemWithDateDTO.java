package com.pixelmind.pixelmind_api.dto.store;

import com.pixelmind.pixelmind_api.model.store.NftItem;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class NftItemWithDateDTO {
    private NftItem nftItem;
    private LocalDateTime earnedAt;
}
