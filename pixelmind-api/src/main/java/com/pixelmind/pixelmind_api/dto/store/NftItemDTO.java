package com.pixelmind.pixelmind_api.dto.store;

import com.pixelmind.pixelmind_api.model.store.NftItem;
import com.pixelmind.pixelmind_api.enums.NftTier;
import com.pixelmind.pixelmind_api.model.User;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class NftItemDTO {
    private Long id;
    private String title;
    private String imageUrl;
    private BigDecimal price;
    private boolean forSale;
    private Long ownerId;
    private Long collectionId;
    private String tier; // Novo campo

    public NftItem toEntity(User owner) {
        NftItem nft = new NftItem();
        nft.setId(this.id);
        nft.setTitle(this.title);
        nft.setImageUrl(this.imageUrl);
        nft.setPrice(this.price);
        nft.setForSale(this.forSale);
        nft.setOwner(owner);
        nft.setCollectionId(this.collectionId);
        nft.setTier(this.tier != null ? NftTier.valueOf(this.tier) : NftTier.COMMON);
        return nft;
    }

    public static NftItemDTO fromEntity(NftItem nft) {
        NftItemDTO dto = new NftItemDTO();
        dto.setId(nft.getId());
        dto.setTitle(nft.getTitle());
        dto.setImageUrl(nft.getImageUrl());
        dto.setPrice(nft.getPrice());
        dto.setForSale(nft.isForSale());
        dto.setOwnerId(nft.getOwner() != null ? nft.getOwner().getId() : null);
        dto.setCollectionId(nft.getCollectionId());
        dto.setTier(nft.getTier() != null ? nft.getTier().name() : NftTier.COMMON.name());
        return dto;
    }
}
