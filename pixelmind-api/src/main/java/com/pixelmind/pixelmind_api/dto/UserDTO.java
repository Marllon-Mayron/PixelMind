package com.pixelmind.pixelmind_api.dto;

import com.pixelmind.pixelmind_api.model.store.NftItem;
import lombok.Data;

import java.util.List;

@Data
public class UserDTO {
    private String name;
    private String userName;
    private String email;
    private String company;
    private String position;
    private String phone;
    private String uf;
    private Integer age;
    private String privileges;
    private List<NftItem> nftItems;
}
