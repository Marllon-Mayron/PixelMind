package com.pixelmind.pixelmind_api.dto;

import com.pixelmind.pixelmind_api.model.User;
import com.pixelmind.pixelmind_api.model.store.NftItem;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class UserDTO {
    private String name;
    private String userName;
    private String email;
    private String phone;
    private String uf;
    private Integer age;
    private Integer tickets;
    private String privileges;

    public static User toEntity(UserDTO dto) {
        if (dto == null) return null;

        User user = new User();
        user.setName(dto.getName());
        user.setUserName(dto.getUserName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setUf(dto.getUf());
        user.setAge(dto.getAge());
        user.setTicket(dto.getTickets());
        user.setPrivileges(dto.getPrivileges());

        return user;
    }

    public static UserDTO fromEntity(User user) {
        if (user == null) return null;

        UserDTO dto = new UserDTO();
        dto.setName(user.getName());
        dto.setUserName(user.getUserName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setUf(user.getUf());
        dto.setAge(user.getAge());
        dto.setTickets(user.getTicket());
        dto.setPrivileges(user.getPrivileges());

        return dto;
    }
}
