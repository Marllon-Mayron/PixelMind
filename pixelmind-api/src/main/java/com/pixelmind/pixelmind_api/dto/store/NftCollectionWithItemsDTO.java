package com.pixelmind.pixelmind_api.dto.store;

import lombok.Data;

import java.util.List;

@Data
public class NftCollectionWithItemsDTO {
    private Long id;
    private String name;
    private String description;
    private String imageUrl;
    private List<NftItemDTO> items;
}
