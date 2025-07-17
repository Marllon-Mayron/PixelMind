package com.pixelmind.pixelmind_api.model.store;

import com.pixelmind.pixelmind_api.model.User;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "user_nfts")
public class UserNft {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private User user;

    @ManyToOne(optional = false)
    private NftItem nftItem;

    private LocalDateTime earnedAt = LocalDateTime.now(); // opcional
}
