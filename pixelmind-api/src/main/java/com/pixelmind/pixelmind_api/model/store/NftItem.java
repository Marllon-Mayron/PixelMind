package com.pixelmind.pixelmind_api.model.store;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pixelmind.pixelmind_api.enums.NftTier;
import com.pixelmind.pixelmind_api.model.User;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Data
@Table(name = "nft_items")
public class NftItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String imageUrl;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @Column(name = "for_sale", nullable = false)
    private boolean forSale = false;

    @Column(name = "collection_id")
    private Long collectionId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NftTier tier = NftTier.COMMON;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User owner;

    @OneToMany(mappedBy = "nftItem", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<UserNft> userNfts;
}
