package com.pixelmind.pixelmind_api.dto;

import lombok.Data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateDTO {
    private String name;
    private String userName;
    private String email;
    private String password;
    private String phone;
    private String uf;
    private Integer age;

}
