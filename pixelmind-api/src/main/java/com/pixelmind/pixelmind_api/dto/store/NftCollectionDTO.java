package com.pixelmind.pixelmind_api.dto.store;

import lombok.Data;

@Data
public class NftCollectionDTO {
    private Long id;
    private String name;
    private String description;
    private String imageUrl;
}

